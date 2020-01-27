package pl.natusiek.grouptp.game.kit.impl;

import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.game.kit.Kit;

import java.util.Arrays;

public class KitImpl implements Kit {

    private final String name;
    private final int rows, column;
    private final ItemStack icon;
    private final ItemStack[] content, armorContent;

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
    public int getRows() { return rows; }

    @Override
    public int getColumn() { return column; }

    @Override
    public ItemStack getIcon() { return icon; }

    @Override
    public ItemStack[] getContent() { return content; }

    @Override
    public ItemStack[] getArmorContent() { return armorContent; }

    @Override
    public int hashCode() {
        int result = 31 * Arrays.hashCode(armorContent) + Arrays.hashCode(content);
        return result;
    }
}
