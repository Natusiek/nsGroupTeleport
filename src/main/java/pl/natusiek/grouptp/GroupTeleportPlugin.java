package pl.natusiek.grouptp;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.arena.impl.GameArenaManagerImpl;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataManager;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataSaver;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.basic.kit.impl.KitManagerImpl;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.basic.spectate.impl.GameSpectateImpl;
import pl.natusiek.grouptp.command.AdminKitCommand;
import pl.natusiek.grouptp.command.KitCommand;
import pl.natusiek.grouptp.command.SetArenaCommand;
import pl.natusiek.grouptp.config.ConfigManager;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.config.system.ConfigHelper;
import pl.natusiek.grouptp.listener.*;

public final class GroupTeleportPlugin extends JavaPlugin {


    private KitManager kitManager;
    private GameSpectate gameSpectate;
    private GameArenaManager arenaManager;
    private EquipmentDataSaver dataSaver;
    private ConfigManager configManager;
    private EquipmentDataManager dataManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configManager.load();
        ConfigHelper.create(new File(this.getDataFolder(), "messages.yml"), MessagesConfig.class);

        this.kitManager = new KitManagerImpl();
        this.arenaManager = new GameArenaManagerImpl();
        this.gameSpectate = new GameSpectateImpl();
        this.arenaManager.getArenas().forEach(GameArena::restart);

        this.dataManager = new EquipmentDataManager();
        this.dataSaver = new EquipmentDataSaver(this.dataManager, this.kitManager);
        this.dataSaver.load();


        this.getServer().getLogger().info("Dostepnych aren: " + this.arenaManager.getArenas().size());
        this.getServer().getLogger().info("Dostepnych kitow: " + this.kitManager.getKits().size());

        this.getCommand("kita").setExecutor(new KitCommand(this.arenaManager));
        this.getCommand("setarena").setExecutor(new SetArenaCommand(this));
        this.getCommand("adminkit").setExecutor(new AdminKitCommand(this.dataManager, this.dataSaver));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getPluginManager().registerEvents(new ChoosingAnArenaListener(this, this.arenaManager, this.kitManager), this);
        this.getServer().getPluginManager().registerEvents(new ClosingTheArenaListener(this, this.arenaManager, this.kitManager, this.gameSpectate), this);
        this.getServer().getPluginManager().registerEvents(new DuringGamePlayerListener(this.arenaManager), this);
        this.getServer().getPluginManager().registerEvents(new ProtectingGameListener(this.arenaManager, this.gameSpectate), this);
        this.getServer().getPluginManager().registerEvents(new WatchingTheArenaListener(this, this.gameSpectate), this);
    }

    public KitManager getKitManager() { return kitManager; }

    public GameSpectate getGameSpectate() { return gameSpectate; }

    public ConfigManager getConfigManager() { return configManager; }

    public GameArenaManager getGameArenaManager() { return arenaManager; }

}
