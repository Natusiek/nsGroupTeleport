package pl.natusiek.grouptp.game.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.helper.ItemBuilder;

import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ArenaInfoInventoryProvider implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("game")
            .provider(new ArenaInfoInventoryProvider())
            .size(5, 9)
            .title(colored("&7Lista wszystkich aren."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final Arena arena = plugin.getArenaManager().getArenas().stream()
                .filter(gameArena -> gameArena.getState() == Arena.ArenaStates.IN_GAME)
                .findFirst()
                .orElse(null);
        plugin.getArenaManager().getArenas().forEach(gameArena -> {
            if (gameArena == arena) {
                String playersName = gameArena.getPlayers().stream()
                        .map(Bukkit::getPlayer)
                        .map(Player::getName)
                        .collect(Collectors.joining(colored("&8, &f")));
                final ItemStack open = new ItemBuilder(Material.WOOL).withDurability((short) 5)
                        .withName(" &8(&aON&8) &7Arena: &f" + gameArena.getName())
                        .withLore(" ", " &7Lista graczy: ", playersName).build();

                contents.add(ClickableItem.of(new ItemStack(open), event -> {
                    plugin.getSpectate().joinSpectate(player, arena);
                    player.closeInventory();
                }));
            } else {
                final ItemStack close = new ItemBuilder(Material.WOOL).withDurability((short) 14)
                        .withName(" &8(&cOFF&8) &7Arena: &f" + gameArena.getName())
                        .withLore(" ", " &7cArena jest zamknięta.", " ").build();
                contents.add(ClickableItem.of(new ItemStack(close), event -> {
                    player.sendMessage(colored("&7Arena jest zamknięta."));
                    player.closeInventory();
                }));
            }
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
