package pl.natusiek.grouptp.basic.kit.equipment;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.basic.kit.impl.KitImpl;
import pl.natusiek.grouptp.helper.Serializer;

public class EquipmentDataSaver {

    private final KitManager kitManager;
    private final EquipmentDataManager dataManager;

    private final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
    private final File dir = new File(plugin.getDataFolder(), "kits");

    public EquipmentDataSaver(EquipmentDataManager dataManager, KitManager kitManager) {
        this.kitManager = kitManager;
        this.dataManager = dataManager;
    }

    public void save() {
        this.dataManager.getEquipments().forEach((name, data) -> {
            final File file = new File(this.dir, name + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", data.getName());
            config.set("rows", data.getRows());
            config.set("column", data.getColumn());
            config.set("icon", data.getIcon());
            config.set("inventory", Serializer.serializeInventory(data.getContent()));
            config.set("armor", Serializer.serializeInventory(data.getArmorContent()));

            try {
                config.save(file);
                this.kitManager.addKit(new KitImpl(data.getName(), data.getRows(), data.getColumn(), data.getIcon(), data.getContent(), data.getArmorContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(String name) {
            final File file = new File(this.dir, name + ".yml");
            if (file.exists()) {
                file.delete();
            } else {
                plugin.getLogger().info("Nie ma takiego kitu ;c ");
            }
    }

    public void load() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        if (this.dir.listFiles() == null) {
            return;
        }

        for (File file : Objects.requireNonNull(this.dir.listFiles(), "Files are null")) {
            try {
                final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                final EquipmentData data = this.dataManager.createEquipmentData(StringUtils.replace(file.getName(), ".yml", ""));

                data.setName(config.getString("name"));
                data.setRows(config.getInt("rows"));
                data.setColumn(config.getInt("column"));
                data.setIcon(config.getItemStack("icon"));
                data.setContent(Serializer.deserializeInventory(config.getString("inventory")));
                data.setArmorContent(Serializer.deserializeInventory(config.getString("armor")));

                this.kitManager.addKit(new KitImpl(data.getName(), data.getRows(), data.getColumn(), data.getIcon(), data.getContent(), data.getArmorContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
