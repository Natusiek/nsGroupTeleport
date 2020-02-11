package pl.natusiek.grouptp;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        if (this.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            getLogger().warning("not found: WorldEdit ");
            getPluginLoader().disablePlugin(this);
            return;
        }
        if (this.getServer().getPluginManager().getPlugin("SmartInvs") == null) {
            getLogger().warning("not found: SmartInvs ");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.saveDefaultConfig();
        ConfigHelper.create(new File(this.getDataFolder(), "messages.yml"), MessagesConfig.class);

        this.arenaManager = new ArenaManagerImpl();
        this.arenaManager.getArenas().forEach(Arena::restart);
        this.arenaManager.loadArenas();

        this.kitManager = new KitManagerImpl();
        this.getLogger().info("Zaladowanych kitow: " + this.kitManager.getKits().size());

        this.arenaSpectate = new ArenaSpectateImpl();

        this.dataManager = new KitDataSaverImpl(this.kitManager);
        this.dataManager.load();

        this.getLogger().info("Zaladowanych aren: " + this.arenaManager.getArenas().size());

        this.getCommand("kit").setExecutor(new KitCommand(this.arenaManager, this.kitManager, dataManager));
        this.getCommand("setarena").setExecutor(new SetArenaCommand(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.registerEvents(
                new ChoosingAnArenaListener(this.kitManager, this.arenaManager),
                new ClosingTheArenaListener(this.kitManager, this.arenaManager),
                new DuringGamePlayerListener(this.arenaManager),
                new ProtectingGameListener(this.arenaManager),
                new SpectetorsArenaListener(this.arenaManager, this.arenaSpectate));
    }

    private void registerEvents(Listener... listeners) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        Arrays.stream(listeners)
                .forEachOrdered(listener ->  pluginManager.registerEvents(listener, this));
    }

    public ArenaManager getArenaManager() { return arenaManager; }

    public KitManager getKitManager() { return kitManager; }

    public ArenaSpectate getSpectate() { return arenaSpectate; }

}
