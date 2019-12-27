package pl.natusiek.grouptp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.gui.kitInventory;
import pl.natusiek.grouptp.config.MessagesConfig;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitCommand implements CommandExecutor {

    private final GameArenaManager arenaManager;

    public KitCommand(GameArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = ((Player) sender);
        if (this.arenaManager.findArenaByPlayer(player.getUniqueId()) != null) {
            player.sendMessage(colored(MessagesConfig.KIT$BLOCK_CHANGE_KIT));
            return false;
        }
        kitInventory.INVENTORY_KIT.open(player);
    return true;
    }
}
