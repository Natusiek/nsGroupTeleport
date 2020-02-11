package pl.natusiek.grouptp.game.kit.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.game.kit.Kit;
import pl.natusiek.grouptp.game.kit.KitDataSaver;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.helper.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class KitDataSaverImpl implements KitDataSaver {

    private final KitManager kitManager;

    private final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
    private final File dir = new File(plugin.getDataFolder(), "kits");

    public KitDataSaverImpl(KitManager kitManager) { this.kitManager = kitManager; }

    @Override
    public void save(String name) {
        this.kitManager.getKits().forEach(kit -> {

            final File file = new File(this.dir, name + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("name", kit.getName());
            configuration.set("rows", kit.getRows());
            configuration.set("column", kit.getColumn());
            configuration.set("icon", kit.getIcon());
            configuration.set("inventory", Serializer.serializeInventory(kit.getContent()));
            configuration.set("armor", Serializer.serializeInventory(kit.getArmorContent()));
            try {
                configuration.save(file);
            } catch (IOException ex) {
                throw new RuntimeException("Cannot save player equipment data!", ex);
            }
        });
    }

    @Override
    public void delete(String name) {
        final File file = new File(this.dir, name + ".yml");

        if (file.exists()) {
            try {
                file.delete();
                plugin.getLogger().info("Usunieto kit: " + name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void load() {
        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdirs();
        if (!this.dir.exists())  this.dir.mkdirs();
        if (this.dir.listFiles() == null)  return;

        for (File file : Objects.requireNonNull(this.dir.listFiles(), "Files are null")) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                this.kitManager.addKit(new KitImpl(config.getString("name"), config.getInt("rows"), config.getInt("column"), config.getItemStack("icon"), Serializer.deserializeInventory(config.getString("inventory")), Serializer.deserializeInventory(config.getString("armor"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
