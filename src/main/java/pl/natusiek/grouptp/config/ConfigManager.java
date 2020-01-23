package pl.natusiek.grouptp.config;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.impl.GameArenaImpl;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.LocationHelper;

public class ConfigManager {

    private FileConfiguration config;
    private final GroupTeleportPlugin plugin;

    public ConfigManager(GroupTeleportPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    private int rowsKit, columnKit, rowsSpectator, columnSpectator;
    private String nameKit, nameSpectator;
    private Location spawnLocation;
    private ItemStack itemKits, itemSpectator, itemLeaveServer, itemLeaveSpectator, itemCompass;
    private Material button, base;

    public void load() {
        loadArenas();

        this.spawnLocation = LocationHelper.fromString(config.getString("spawn.location", "world, 0.0, 80.0, 0.0, 0.0f, 0.0f")).toLocation();

        this.itemKits = ItemBuilder.withSection(config.getConfigurationSection("items.kits")).build();
        this.itemSpectator = ItemBuilder.withSection(config.getConfigurationSection("items.spectator")).build();
        this.itemLeaveServer = ItemBuilder.withSection(config.getConfigurationSection("items.leave-server")).build();
        this.itemLeaveSpectator = ItemBuilder.withSection(config.getConfigurationSection("items.leave-spectator")).build();
        this.itemCompass = ItemBuilder.withSection(config.getConfigurationSection("items.compass")).build();

        this.button = Material.matchMaterial(config.getString("blocks.button", "WOOD_BUTTON"));
        this.base = Material.matchMaterial(config.getString("blocs.base", "JUKE_BOX"));

        this.nameKit = config.getString("gui.kit.name", "&7Wybierz swoj kit");
        this.rowsKit = config.getInt("gui.kit.rows", 1);
        this.columnKit = config.getInt("gui.kit.column", 3);

        this.nameSpectator = config.getString("gui.spectator.name", "&7Lista wszystkich aren");
        this.rowsSpectator = config.getInt("gui.spectator.rows", 1);
        this.columnSpectator = config.getInt("gui.spectator.column", 4);

    }

    private void loadArenas() {
        config.getConfigurationSection("arenas").getKeys(false).forEach(name -> {
            final ConfigurationSection section = config.getConfigurationSection("arenas." + name);
            final LocationHelper center = LocationHelper.fromString(section.getString("location", "world, 100.0, 80.0, 100.0, 0.0f, 1.0f"));
            final int size = section.getInt("size", 50);
            this.plugin.getGameArenaManager().addArena(new GameArenaImpl(name, size, center));
        });
    }

    public void saveConfig() {
        try {
            this.config.save(this.plugin.getDataFolder());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Location getSpawnLocation() { return spawnLocation; }

    public ItemStack getItemKits() { return itemKits; }

    public ItemStack getItemSpectator() { return itemSpectator; }

    public ItemStack getItemLeaveServer() { return itemLeaveServer; }

    public ItemStack getItemLeaveSpectator() { return itemLeaveSpectator; }

    public ItemStack getItemCompass() { return itemCompass; }

    public Material getButton() { return button; }

    public Material getBase() { return base; }

    public int getRowsKit() { return rowsKit; }

    public int getColumnKit() { return columnKit; }

    public String getNameKit() { return nameKit; }

    public int getRowsSpectator() { return rowsSpectator; }

    public int getColumnSpectator() { return columnSpectator; }

    public String getNameSpectator() { return nameSpectator; }

}
