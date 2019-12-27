package pl.natusiek.grouptp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.arena.GameSpectate;
import pl.natusiek.grouptp.arena.impl.GameArenaImpl;
import pl.natusiek.grouptp.arena.impl.GameArenaManagerImpl;
import pl.natusiek.grouptp.arena.impl.GameSpectateImpl;
import pl.natusiek.grouptp.basic.nametag.KitManagerImplWrapper;
import pl.natusiek.grouptp.command.KitCommand;
import pl.natusiek.grouptp.command.SetArenaCommand;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.config.system.ConfigHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.kit.KitManager;
import pl.natusiek.grouptp.kit.impl.KitImpl;
import pl.natusiek.grouptp.kit.impl.KitManagerImpl;
import pl.natusiek.grouptp.listener.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.DIAMOND;
import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public final class GroupTeleportPlugin extends JavaPlugin implements PluginMessageListener {

    private GameArenaManager gameArenaManager;
    private KitManager kitManager;
    private GameArena gameArena;
    private GameSpectate gameSpectate;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        ConfigHelper.create(new File(this.getDataFolder(), "messages.yml"), MessagesConfig.class);

        this.gameArenaManager = new GameArenaManagerImpl();
        this.gameSpectate = new GameSpectateImpl(gameArenaManager, gameArena);
        this.gameArenaManager.getArenas().forEach(GameArena::restart);

        this.loadArenas();
        this.getServer().getLogger().info("Dostepnych aren: " + this.gameArenaManager.getArenas().size());

        if (this.getServer().getPluginManager().getPlugin("NametagEdit") != null) {
            this.kitManager = new KitManagerImplWrapper();
            this.getLogger().warning("Wykryto plugin NametagEdit, uzywam innej implementacji dla kitmanager..");
        } else {
            this.kitManager = new KitManagerImpl();
        }

        final List<ItemStack> diaxContent = Arrays.asList(
              new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 2).addEnchantment(Enchantment.DAMAGE_ALL, 3).build(),
              new ItemStack(Material.GOLDEN_APPLE, 2, (short) 1),
              new ItemStack(Material.GOLDEN_APPLE, 12),
              new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.KNOCKBACK, 2).build(),
              new ItemStack(Material.ENDER_PEARL, 2),
              new ItemStack(Material.WATER_BUCKET),
              new ItemStack(Material.COOKED_BEEF, 32),
              new ItemStack(Material.COBBLESTONE, 64),
              new ItemStack(Material.FISHING_ROD),
              new ItemStack(Material.WOOD, 64),
              new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 5).addEnchantment(Enchantment.DURABILITY, 2).build(),
              new ItemBuilder(Material.DIAMOND_AXE).addEnchantment(Enchantment.DIG_SPEED, 4).addEnchantment(Enchantment.DURABILITY, 2).build());
        this.kitManager.addKit(new KitImpl("&bDiaxowe", 1,3, diaxContent, new ItemBuilder(DIAMOND).withName("&bDiaxowe").build(),
              new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build()));

        final List<ItemStack> zelakContent = Arrays.asList(
              new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 1).addEnchantment(Enchantment.DAMAGE_ALL, 3).build(),
              new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1),
              new ItemStack(Material.GOLDEN_APPLE, 12),
              new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 2).build(),
              new ItemStack(Material.ENDER_PEARL, 2),
              new ItemStack(Material.WATER_BUCKET),
              new ItemStack(Material.COOKED_BEEF, 32),
              new ItemStack(Material.COBBLESTONE, 64),
              new ItemStack(Material.FISHING_ROD),
              new ItemStack(Material.WOOD, 64),
              new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 5).addEnchantment(Enchantment.DURABILITY, 2).build(),
              new ItemBuilder(Material.DIAMOND_AXE).addEnchantment(Enchantment.DIG_SPEED, 4).addEnchantment(Enchantment.DURABILITY, 2).build());
        this.kitManager.addKit(new KitImpl("&fZelaki", 1,4, zelakContent, new ItemBuilder(Material.IRON_INGOT).withName("&fZelaki").build(),
              new ItemBuilder(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build(),
              new ItemBuilder(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build()));

        final List<ItemStack> wedkarzContent = Arrays.asList(
              new ItemStack(Material.FISHING_ROD),
              new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build(),
              new ItemBuilder(Material.GOLDEN_APPLE, 8, (short) 0).withName("&e&lZmutowane Jablko").build(),
              new ItemStack(Material.WATER_BUCKET),
              new ItemStack(Material.ENDER_PEARL, 3),
              new ItemStack(Material.COOKED_BEEF, 32),
              new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_FIRE, 1).build(),
              new ItemStack(Material.ARROW, 24),
              new ItemStack(Material.COBBLESTONE, 64));
        this.kitManager.addKit(new KitImpl("&dWedkarz", 1, 5, wedkarzContent, new ItemBuilder(Material.FISHING_ROD).withName("&dWedkarz").build(),
              new ItemBuilder(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 1).build(),
              new ItemBuilder(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 1).build(),
              new ItemBuilder(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 1).build(),
              new ItemBuilder(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 1).build()));

        final Material button = Material.matchMaterial(this.getConfig().getString("blocks.button", "WOOD_BUTTON"));
        final Material base = Material.matchMaterial(this.getConfig().getString("blocks.base", "JUKE_BOX"));

        this.getCommand("kit").setExecutor(new KitCommand(this.gameArenaManager));
        this.getCommand("setarena").setExecutor(new SetArenaCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerChangeArenaListener(this.gameArenaManager, this.kitManager, button == null ? Material.WOOD_BUTTON : button, base == null ? Material.JUKEBOX : base), this);
        this.getServer().getPluginManager().registerEvents(new PlayerGameArenaListener(this.gameArenaManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerCloseArenaListener(this.gameArenaManager, this.kitManager, this.gameSpectate), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKitsListener(this.kitManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveServerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSpectatorsArenaListener(this.gameSpectate), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    private void loadArenas() {
        final FileConfiguration config = this.getConfig();
        for (String id : config.getConfigurationSection("arenas").getKeys(false)) {
            final ConfigurationSection section = config.getConfigurationSection("arenas." + id);
            final Location center = LocationHelper.fromString(section.getString("location", "0.3;80.2;0.9"));
            final int size = section.getInt("size", 10);
            this.gameArenaManager.addArena(new GameArenaImpl(id, size, center));
        }
    }

    public GameArena getGameArena() { return gameArena; }

    public GameArenaManager getGameArenaManager() { return gameArenaManager; }

    public KitManager getKitManager() { return kitManager; }

    public GameSpectate getGameSpectate() { return gameSpectate; }

    public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {  }

}
