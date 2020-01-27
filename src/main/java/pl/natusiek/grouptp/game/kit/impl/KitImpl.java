package pl.natusiek.grouptp.game.kit.impl;

import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.game.kit.Kit;

import java.util.Arrays;

public class KitImpl implements Kit {

    private String name;
    private int rows, column;
    private ItemStack icon;
    private ItemStack[] content, armorContent;

    public KitImpl(String name, int rows, int column, ItemStack icon, ItemStack[] content, ItemStack... armorContent) {
        this.name = name;
        this.rows = rows;
        this.column = column;
        this.icon = icon;
        this.content = content;
        this.armorContent = armorContent;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void setName(String name) { this.name = name; }

    @Override
    public int getRows() { return rows; }

    @Override
    public void setRows(int rows) { this.rows = rows; }

    @Override
    public int getColumn() { return column; }

    @Override
    public void setColumn(int column) { this.column = column; }

    @Override
    public ItemStack getIcon() { return icon; }

    @Override
    public void setIcon(ItemStack icon) { this.icon = icon; }

    @Override
    public ItemStack[] getContent() { return content; }

    @Override
    public void setContent(ItemStack[] content) { this.content = content; }

    @Override
    public ItemStack[] getArmorContent() { return armorContent; }

    @Override
    public void setArmorContent(ItemStack[] armorContent) { this.armorContent = armorContent; }

    @Override
    public int hashCode() {
        int result = 31 * Arrays.hashCode(armorContent) + Arrays.hashCode(content);
        return result;
    }
}
