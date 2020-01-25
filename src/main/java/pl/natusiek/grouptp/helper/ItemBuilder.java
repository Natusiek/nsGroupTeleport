package pl.natusiek.grouptp.helper;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ItemBuilder {
        private ItemStack item;

        public ItemBuilder(Material material) {
            if (material == Material.AIR) {
                throw new IllegalArgumentException("Material cannot be AIR!");
            }
            this.item = new ItemStack(material);
        }

        public ItemBuilder(Material material, int amount, short durability) {
            if (material == Material.AIR) {
                throw new IllegalArgumentException("Material cannot be AIR!");
            }
            this.item = new ItemStack(material, amount, durability);
        }

        public ItemBuilder(ItemStack item) {
            this.item = item;
        }

        public ItemBuilder withType(Material material) {
            if (material == Material.AIR) {
                throw new IllegalArgumentException("Material cannot be AIR!");
            }
            if (this.item == null) {
                this.item = new ItemStack(material);
                return this;
            }
            this.item.setType(material);
            return this;
        }

        public ItemBuilder withName(String name) {
            final ItemMeta meta = this.item.getItemMeta();
            meta.setDisplayName(colored(name));
            this.item.setItemMeta(meta);
            return this;
        }

        public ItemBuilder withLore(String... lore) {
            return this.withLore(Arrays.asList(lore));
        }

        public ItemBuilder withLore(List<String> lore) {
            final ItemMeta meta = this.item.getItemMeta();
            meta.setLore(colored(lore));
            this.item.setItemMeta(meta);
            return this;
        }

        public ItemBuilder withAmount(int amount) {
            this.item.setAmount(amount);
            return this;
        }

        public ItemBuilder withDurability(short durability) {
            this.item.setDurability(durability);
            return this;
        }

        public ItemBuilder addEnchantment(Enchantment enchant, int level) {
            final ItemMeta meta = this.item.getItemMeta();
            meta.addEnchant(enchant, level, true);
            this.item.setItemMeta(meta);
            return this;
        }

        public ItemStack build() { return this.item; }

        public static ItemBuilder withSection(ConfigurationSection section) {
            final ItemBuilder builder = new ItemBuilder(Material.GRASS);

            if (section.isString("material")) {
                final Material material = Material.matchMaterial(section.getString("material"));
                if (material != null) {
                    builder.withType(material);
                }
            }
            if (section.isString("name")) {
                builder.withName(section.getString("name"));
            }
            if (section.isList("lore")) {
                builder.withLore(section.getStringList("lore"));
            }
            if (section.isInt("amount")) {
                builder.withAmount(section.getInt("amount"));
            }
            if (section.isInt("data") || section.isInt("durability")) {
                builder.withDurability((short)section.getInt("data", section.getInt("durability")));
            }
            if (section.isList("enchants")) {
                for (String enchant : section.getStringList("enchants")) {
                    int level; String[] part = enchant.split(";");
                    if (part.length < 1) {
                        continue;
                    }

                    Enchantment enchantment = Enchantment.getByName(part[0]);
                    if (enchantment == null) {
                        continue;
                    }
                    try {
                        level = Integer.parseInt(part[1]);
                    }
                    catch (NumberFormatException ex) {
                        continue;
                    }
                    builder.addEnchantment(enchantment, level);
                }
            }
            return builder;
        }

}
