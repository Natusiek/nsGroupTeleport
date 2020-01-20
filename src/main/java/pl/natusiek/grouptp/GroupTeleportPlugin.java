package pl.natusiek.grouptp;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.arena.impl.GameArenaImpl;
import pl.natusiek.grouptp.basic.arena.impl.GameArenaManagerImpl;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataManager;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataSaver;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.basic.kit.impl.KitManagerImpl;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.basic.spectate.impl.GameSpectateImpl;
import pl.natusiek.grouptp.command.CreateKitCommand;
import pl.natusiek.grouptp.command.KitCommand;
import pl.natusiek.grouptp.command.SetArenaCommand;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.config.system.ConfigHelper;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.listener.*;

public final class GroupTeleportPlugin extends JavaPlugin {

    private KitManager kitManager;
    private GameSpectate gameSpectate;
    private GameArenaManager arenaManager;
    private EquipmentDataSaver dataSaver;
    private EquipmentDataManager dataManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        ConfigHelper.create(new File(this.getDataFolder(), "messages.yml"), MessagesConfig.class);

        this.arenaManager = new GameArenaManagerImpl();
        this.arenaManager.getArenas().forEach(GameArena::restart);

        this.dataManager = new EquipmentDataManager();
        this.kitManager = new KitManagerImpl();
        this.dataSaver = new EquipmentDataSaver(this.dataManager, this.kitManager);

        this.dataSaver.load();
        this.getServer().getLogger().info("Dostepnych kitow: " + this.kitManager.getKits().size());

        this.gameSpectate = new GameSpectateImpl();
        this.loadArenas();
        this.getServer().getLogger().info("Dostepnych aren: " + this.arenaManager.getArenas().size());

        final Material button = Material.matchMaterial(this.getConfig().getString("blocks.button", "WOOD_BUTTON"));
        final Material base = Material.matchMaterial(this.getConfig().getString("blocks.base", "JUKE_BOX"));

        this.getCommand("kit").setExecutor(new KitCommand(this.arenaManager));
        this.getCommand("setarena").setExecutor(new SetArenaCommand(this));
        this.getCommand("createkit").setExecutor(new CreateKitCommand(this.dataManager, this.dataSaver));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getPluginManager().registerEvents(new PlayerChangeArenaListener(this.arenaManager, this.kitManager, button == null ? Material.WOOD_BUTTON : button, base == null ? Material.JUKEBOX : base), this);
        this.getServer().getPluginManager().registerEvents(new PlayerChangeKitsListener(this.kitManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerCloseArenaListener(this.arenaManager, this.kitManager, this.gameSpectate), this);
        this.getServer().getPluginManager().registerEvents(new PlayerGameArenaListener(this.arenaManager, this.gameSpectate), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHubListener(this.arenaManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSpectatorsArenaListener(this.gameSpectate), this);
    }

    private void loadArenas() {
        final FileConfiguration config = this.getConfig();
        config.getConfigurationSection("arenas").getKeys(false).forEach(name -> {
            final ConfigurationSection section = config.getConfigurationSection("arenas." + name);
            final Location center = LocationHelper.fromString(section.getString("location", "0.1;80.1;0.1"));
            final int size = section.getInt("size", 50);
            this.arenaManager.addArena(new GameArenaImpl(name, size, center));
        });
    }

    public KitManager getKitManager() { return kitManager; }

    public GameSpectate getGameSpectate() { return gameSpectate; }

    public GameArenaManager getGameArenaManager() { return arenaManager; }

}
