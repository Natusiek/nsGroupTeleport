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

    public Kit save(String name) {
        this.kitManager.getKits().forEach(kit -> {
            final File file = new File(this.dir, name + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", kit.getName());
            config.set("rows", kit.getRows());
            config.set("column", kit.getColumn());
            config.set("icon", kit.getIcon());
            config.set("inventory", Serializer.serializeInventory(kit.getContent()));
            config.set("armor", Serializer.serializeInventory(kit.getArmorContent()));

            try {
                config.save(file);
                this.kitManager.addKit(new KitImpl(kit.getName(), kit.getRows(), kit.getColumn(), kit.getIcon(), kit.getContent(), kit.getArmorContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    public Kit delete(String name) {
        final File file = new File(this.dir, name + ".yml");
        if (file.exists()) {
            file.delete();
        } else {
            plugin.getLogger().info("Nie ma takiego kitu ;c ");
        }
        return null;
    }

    public void load() {
        if (!this.plugin.getDataFolder().exists()) { this.plugin.getDataFolder().mkdirs();
        }
        if (!this.dir.exists()) { this.dir.mkdirs();
        }
        if (this.dir.listFiles() == null) { return;
        }
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
