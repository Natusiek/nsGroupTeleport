package pl.natusiek.grouptp.game.arena.impl;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;
import pl.natusiek.grouptp.helper.SchematicHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ArenaImpl implements Arena {

    private final String name;
    private final int size;
    private final LocationHelper center;
    private final File schematicFile;

    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> spectators = new ArrayList<>();
    private final Map<UUID, WorldBorder> worldBorders = new HashMap<>();

    private int state = ArenaStates.AVAILABLE;

    public ArenaImpl(String name, int size, LocationHelper center) {
        this.name = name;
        this.size = size;
        this.center = center;

        final File folder = new File(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class).getDataFolder(), "arenas");
        this.schematicFile = new File(folder, "map_" + this.name + ".schematic");
        if (!folder.exists()) folder.mkdirs();
        if (!this.schematicFile.exists()) {
            try {
                this.schematicFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        this.state = ArenaStates.IN_GAME;
        this.players.stream()
                .map(Bukkit::getPlayer)
                .forEach(player -> {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(this.center.toLocation());
                    BorderHelper.setBorder(this, player, this.center, this.size);
                    String playersName = this.players.stream()
                            .map(Bukkit::getPlayer)
                            .map(Player::getName)
                            .collect(Collectors.joining(colored("&8, &f")));
                    player.sendMessage(colored(MessagesConfig.ARENA$JOINED.replace("{ARENA}", this.name).replace("{PLAYERS}", playersName)));
                });
    }

    @Override
    public void restart() {
        SchematicHelper.pasteSchematic(this.schematicFile, this.center.toLocation(), true);
        Bukkit.getScheduler().runTask(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class), () -> {
            this.center.toLocation().getWorld().getEntities()
                    .stream()
                    .filter(entity -> entity instanceof Item && entity.getLocation().distance(this.center.toLocation()) < this.size + MessagesConfig.ARENA$RADIUS$REMOVE_ITEMS)
                    .forEach(Entity::remove);
            this.players.clear();
            this.spectators.stream()
                    .map(Bukkit::getPlayer)
                    .forEach(players -> {
                        PlayerHelper.addItemFromLobby(players);
                        GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class).getSpectate().leaveSpectate(players);
                        players.sendMessage(colored(MessagesConfig.SPECTATOR$ARENA_CLOSE));
                    });
            this.spectators.clear();
            this.state = ArenaStates.AVAILABLE;
        });
    }

    @Override
    public List<UUID> getPlayers() { return new ArrayList<>(players); }

    @Override
    public List<UUID> getSpectators() { return new ArrayList<>(spectators); }

    @Override
    public void addPlayer(UUID uuid) { this.players.add(uuid); }

    @Override
    public void removePlayer(UUID uuid) { this.players.remove(uuid); }

    @Override
    public boolean isPlaying(UUID uuid) { return this.players.contains(uuid); }

    @Override
    public void addSpectators(UUID uuid) { this.spectators.add(uuid); }

    @Override
    public void removeSpectators(UUID uuid) { this.spectators.remove(uuid); }

    @Override
    public boolean isSpectators(UUID uuid) { return this.spectators.contains(uuid); }

    @Override
    public String getName() { return name; }

    @Override
    public LocationHelper getCenter() { return center; }

    @Override
    public int getSize() { return size; }

    @Override
    public int getState() { return state; }

    @Override
    public void setState(int state) { this.state = state; }

    @Override
    public WorldBorder getBorder(UUID uuid) { return this.worldBorders.get(uuid); }

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

}