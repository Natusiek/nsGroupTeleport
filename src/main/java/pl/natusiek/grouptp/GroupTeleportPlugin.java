package pl.natusiek.grouptp;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import pl.natusiek.grouptp.command.AdminKitCommand;
import pl.natusiek.grouptp.command.KitCommand;
import pl.natusiek.grouptp.command.SetArenaCommand;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.config.system.ConfigHelper;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.arena.impl.ArenaImpl;
import pl.natusiek.grouptp.game.arena.impl.ArenaManagerImpl;
import pl.natusiek.grouptp.game.kit.KitDataSaver;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.game.kit.impl.KitDataSaverImpl;
import pl.natusiek.grouptp.game.kit.impl.KitManagerImpl;
import pl.natusiek.grouptp.game.spectate.ArenaSpectate;
import pl.natusiek.grouptp.game.spectate.impl.ArenaSpectateImpl;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.listener.*;

public final class GroupTeleportPlugin extends JavaPlugin {

    private ArenaManager arenaManager;
    private KitManager kitManager;
    private ArenaSpectate arenaSpectate;
    private KitDataSaver dataManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        ConfigHelper.create(new File(this.getDataFolder(), "messages.yml"), MessagesConfig.class);

        this.arenaManager = new ArenaManagerImpl();
        this.arenaManager.getArenas().forEach(Arena::restart);
        this.arenaSpectate = new ArenaSpectateImpl(arenaManager);
        this.kitManager = new KitManagerImpl();

        this.dataManager = new KitDataSaverImpl(this.kitManager);
        this.dataManager.load();

        this.loadArenas();
        this.getServer().getLogger().info("Zaladowanych kitow: " + this.kitManager.getKits().size());
        this.getServer().getLogger().info("Zaladowanych aren: " + this.arenaManager.getArenas().size());

        this.getCommand("kit").setExecutor(new KitCommand(this.arenaManager));
        this.getCommand("adminkit").setExecutor(new AdminKitCommand(this.kitManager, this.dataManager));
        this.getCommand("setarena").setExecutor(new SetArenaCommand(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getPluginManager().registerEvents(new ChoosingAnArenaListener(this.kitManager, this.arenaManager), this);
        this.getServer().getPluginManager().registerEvents(new ClosingTheArenaListener(this.kitManager, this.arenaManager), this);
        this.getServer().getPluginManager().registerEvents(new DuringGamePlayerListener(this.arenaManager), this);
        this.getServer().getPluginManager().registerEvents(new ProtectingGameListener(this.arenaManager, this.arenaSpectate), this);
        this.getServer().getPluginManager().registerEvents(new SpectetorsArenaListener(arenaManager, this.arenaSpectate), this);
    }

    private void loadArenas() {
        final FileConfiguration config = this.getConfig();

        config.getConfigurationSection("arenas").getKeys(false).forEach(name -> {
            final ConfigurationSection section = config.getConfigurationSection("arenas." + name);
            final int size = section.getInt("size", 50);
            final LocationHelper center = LocationHelper.fromString(section.getString("location", "world, 100.0, 80.0, 100.0, 0.0f, 1.0f"));
            this.arenaManager.addArena(new ArenaImpl(name, size, center));
        });
    }

    public ArenaManager getArenaManager() { return arenaManager; }

    public KitManager getKitManager() { return kitManager; }

    public ArenaSpectate getSpectate() { return arenaSpectate; }

}
