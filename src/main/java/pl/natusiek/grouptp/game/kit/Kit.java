package pl.natusiek.grouptp.game.kit;

import org.bukkit.inventory.ItemStack;

public interface Kit {

    String getName();

    int getRows();

    int getColumn();

    ItemStack getIcon();

    ItemStack[] getContent();

    ItemStack[] getArmorContent();

    int hashCode();
}
