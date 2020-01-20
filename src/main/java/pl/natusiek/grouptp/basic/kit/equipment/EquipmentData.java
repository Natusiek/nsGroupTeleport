package pl.natusiek.grouptp.basic.kit.equipment;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EquipmentData {

    private String name;
    private int rows, column;
    private ItemStack icon;
    private ItemStack[] armorContent, content;

    //name
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    //rows
    public int getRows() { return rows; }

    public void setRows(int rows) { this.rows = rows; }

    //column
    public int getColumn() { return column; }

    public void setColumn(int column) { this.column = column; }

    //icon
    public ItemStack getIcon() { return icon; }

    public void setIcon(ItemStack icon) { this.icon = icon; }

    //inventory
    public ItemStack[] getContent() { return content; }

    public void setContent(ItemStack[] content) { this.content = content; }

    //armor
    public ItemStack[] getArmorContent() { return armorContent; }

    public void setArmorContent(ItemStack[] armorContent) { this.armorContent = armorContent; }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EquipmentData data = (EquipmentData) o;
        return
                Arrays.equals(armor, data.armor)
                    &&
                Arrays.equals(inventory, data.inventory);
    } */

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(armorContent);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

}
