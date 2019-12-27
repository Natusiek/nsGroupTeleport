package pl.natusiek.grouptp.basic.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.arena.GameArena;

import pl.natusiek.grouptp.helper.ItemBuilder;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class allGameInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("allGame")
            .provider(new allGameInventory())
            .size(5, 9)
            .title(colored("&6Lista aren do obserwowania."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final GameArena availableArena = plugin.getGameArenaManager().getArenas()
                .stream()
                .filter(arena -> arena.getState() == GameArena.ArenaStates.IN_GAME)
                .findFirst()
                .orElse(null);
        plugin.getGameArenaManager().
                getArenas().
                stream().
                forEach(gameArena -> {
                    if (gameArena == availableArena) {
                        ItemStack skull = new ItemBuilder(Material.WOOL, 1, (short) 5).withName(" &8(&2ON&8) &7Arena: &f" + gameArena.getNames(plugin.getGameArenaManager().getArenas())).build();
                        contents.add(ClickableItem.of(new ItemStack(skull), e -> {

                            plugin.getGameSpectate().joinSpectate(player, gameArena);
                            player.closeInventory();
                        }));
                    } else {
                        ItemStack skull = new ItemBuilder(Material.WOOL, 1, (short) 14).withName(" &8(&4OFF&8) &7Arena: &f" + gameArena.getNames(plugin.getGameArenaManager().getArenas())).build();
                        contents.add(ClickableItem.of(new ItemStack(skull), e -> {
                            player.closeInventory();
                        }));
                    }
                });
    }

        @Override
        public void update (Player player, InventoryContents contents){
        }

    }

