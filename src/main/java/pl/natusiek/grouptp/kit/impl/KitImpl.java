package pl.natusiek.grouptp.kit.impl;

import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.kit.Kit;

import java.util.List;

public class KitImpl implements Kit {

    private final String name;
    private final int rows, column;
    private final List<ItemStack> content;
    private final ItemStack icon;
    private final ItemStack[] armorContent;

    public KitImpl(String name, int rows, int column, List<ItemStack> content, ItemStack icon, ItemStack... armorContent) {
        this.name = name;
        this.rows = rows;
        this.column = column;
        this.content = content;
        this.icon = icon;
        this.armorContent = armorContent;
    }

    @Override
    public ItemStack[] getArmorContent() {
        return armorContent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ItemStack> getContent() {
        return content;
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public int getRows() { return rows; }

    @Override
    public int getColumn() { return column; }
}
