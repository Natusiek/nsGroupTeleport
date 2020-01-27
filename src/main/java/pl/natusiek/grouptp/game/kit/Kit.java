package pl.natusiek.grouptp.game.kit;

import org.bukkit.inventory.ItemStack;

public interface Kit {

    String getName();

    void setName(String name);

    int getRows();

    void setRows(int rows);

    int getColumn();

    void setColumn(int column);

    ItemStack getIcon();

    void setIcon(ItemStack icon);

    ItemStack[] getContent();

    void setContent(ItemStack[] content);

    ItemStack[] getArmorContent();

    void setArmorContent(ItemStack[] armorContent);

    int hashCode();
}
