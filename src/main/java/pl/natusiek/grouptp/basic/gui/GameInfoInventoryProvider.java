package pl.natusiek.grouptp.basic.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;

import pl.natusiek.grouptp.helper.ItemBuilder;
import java.util.Objects;

import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class GameInfoInventoryProvider implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("game")
            .provider(new GameInfoInventoryProvider())
            .size(5, 9)
            .title(colored("&7Lista aren."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final GameArena Arena = plugin.getGameArenaManager()
                .getArenas()
                .stream()
                .filter(arena -> arena.getState() == GameArena.ArenaStates.IN_GAME)
                .findFirst()
                .orElse(null);
        plugin.getGameArenaManager()
                .getArenas()
                .stream()
                .forEach(gameArena -> {
                    if (gameArena == Arena) {
                        final String playersName = Arena.getPlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).map(players -> players.getName())
                                .collect(Collectors.joining(colored("&8, &f")));

                        final ItemStack open = new ItemBuilder(Material.WOOL).withDurability((short) 5)
                                .withName(" &8(&2ON&8) &7Arena: &f" + gameArena.getNames(plugin.getGameArenaManager().getArenas()))
                                .withLore("&7Lista:", "&8* &f" + playersName).build();

                        contents.add(ClickableItem.of(new ItemStack(open), e -> {
                            plugin.getGameSpectate().joinSpectate(player, gameArena);
                            player.closeInventory();
                        }));
                    } else {
                        final ItemStack close = new ItemBuilder(Material.WOOL).withDurability((short) 14).withName(" &8(&4OFF&8) &7Arena: &f" + gameArena.getNames(plugin.getGameArenaManager().getArenas())).build();
                        contents.add(ClickableItem.of(new ItemStack(close), e -> {
                            player.sendMessage(colored("&7Na arenie nic sie nie dzieje"));
                            player.closeInventory();
                        }));
                    }
                });
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}