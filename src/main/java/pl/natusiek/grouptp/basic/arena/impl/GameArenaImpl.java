package pl.natusiek.grouptp.basic.arena.impl;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;

import net.minecraft.server.v1_8_R3.WorldBorder;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.helper.SchematicHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class GameArenaImpl implements GameArena {

    private final String name;
    private final int size;
    private final LocationHelper center;

    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> spectators = new ArrayList<>();

    private final Map<UUID, WorldBorder> worldBorders = new HashMap<>();

    private final File schematicFile;

    private int state = ArenaStates.AVAILABLE;

    public GameArenaImpl(String name, int size, LocationHelper center) {
        this.name = name;
        this.size = size;
        this.center = center;
        this.schematicFile = new File(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class).getDataFolder(), "map_" + this.name + ".schematic");
        if (!this.schematicFile.exists()) {
            try {
                this.schematicFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        final String playersName = players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).map(HumanEntity::getName).collect(Collectors.joining(colored("&8, &f")));
        this.state = ArenaStates.IN_GAME;
        this.players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> {
                    player.teleport(this.center.toLocation());
                    player.setGameMode(GameMode.SURVIVAL);
                    BorderHelper.setBorder(this, player, this.center, this.size);
                    player.sendMessage(colored(MessagesConfig.ARENA$JOINED.replace("{ARENA}", this.name).replace("{PLAYERS}", playersName)));
                });
    }

    @Override
    public void restart() {
        Bukkit.getScheduler().runTask(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class), () -> {
            this.center.toLocation().getWorld().getEntities()
                    .stream()
                    .filter(entity ->
                            entity instanceof Item && entity.getLocation().distance(this.center.toLocation()) < this.size + MessagesConfig.ARENA$RADIUS$REMOVE_ITEMS)
                    .forEach(Entity::remove);
            this.players.clear();
            SchematicHelper.pasteSchematic(this.schematicFile, this.center, true);
            this.state = ArenaStates.AVAILABLE;
        });
    }

    @Override
    public WorldBorder getBorder(UUID uuid) {
        return this.worldBorders.get(uuid);
    }

    @Override
    public WorldBorder setBorder(UUID uuid, LocationHelper center, int size) {
        WorldBorder border = this.worldBorders.get(uuid);
        if (border == null) {
            this.worldBorders.put(uuid, border = new WorldBorder());
        }
        border.setCenter(center.toLocation().getX(), center.toLocation().getZ());
        border.setSize(size);
        return border;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getState() { return state; }

    @Override
    public void setState(int state) { this.state = state; }

    @Override
    public LocationHelper getCenter() { return center; }

    @Override
    public int getSize() { return size; }

    @Override
    public List<UUID> getPlayers() { return new ArrayList<>(players); }

    @Override
    public List<UUID> getSpectetors() { return new ArrayList<>(spectators); }

    @Override
    public void addPlayer(UUID uuid) { this.players.add(uuid); }

    @Override
    public void removePlayer(UUID uuid) { this.players.remove(uuid); }

    @Override
    public boolean isPlaying(UUID uuid) { return this.players.contains(uuid); }
}
