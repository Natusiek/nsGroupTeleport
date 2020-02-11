package pl.natusiek.grouptp.game.arena.impl;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.helper.LocationHelper;

public class ArenaManagerImpl implements ArenaManager {

    private final Set<Arena> arenas = new HashSet<>();

    @Override
    public void loadArenas() {
        final FileConfiguration config = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class).getConfig();

        config.getConfigurationSection("arenas").getKeys(false).forEach(name -> {
            final ConfigurationSection section = config.getConfigurationSection("arenas." + name);
            final int size = section.getInt("size", 50);
            final LocationHelper center = LocationHelper.fromString(section.getString("location", "world, 100.0, 80.0, 100.0, 0.0f, 1.0f"));
            this.addArena(new ArenaImpl(name, size, center));
        });
    }

    @Override
    public void addArena(Arena arena) { this.arenas.add(arena); }

    @Override
    public List<Arena> getArenas() { return new ArrayList<>(arenas); }

    @Override
    public Arena findArenaByPlayer(UUID uuid) {
        return this.arenas
                .stream()
                .filter(arena -> arena.isPlaying(uuid))
                .findFirst()
                .orElse(null);
    }

}
