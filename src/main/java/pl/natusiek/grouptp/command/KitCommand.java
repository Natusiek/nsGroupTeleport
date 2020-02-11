package pl.natusiek.grouptp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.PlayerInventory;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.gui.KitInventoryProvider;
import pl.natusiek.grouptp.game.kit.KitDataSaver;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.game.kit.impl.KitImpl;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitCommand implements CommandExecutor {

    private final ArenaManager arenaManager;
    private final KitManager kitManager;
    private final KitDataSaver dataSaver;

    public KitCommand(ArenaManager arenaManager, KitManager kitManager, KitDataSaver dataSaver) {
        this.kitManager = kitManager;
        this.dataSaver = dataSaver;
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = ((Player) sender);
        if (player.hasPermission("nsGroupTeleport.admin")) {

            String method = args[0];
            if (method.equalsIgnoreCase("create")) {
                if (args.length > 3) {
                    final PlayerInventory inventory = player.getInventory();
                    int rows = Integer.parseInt(args[2]);
                    int column = Integer.parseInt(args[3]);

                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$CREATED).replace("{KIT}", args[1]));
                    this.kitManager.addKit(new KitImpl(args[1], rows, column, player.getItemInHand(), inventory.getContents(), inventory.getArmorContents()));
                    this.dataSaver.save(args[1]);
                } else {
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$USE));
                }
            }

            if (method.equalsIgnoreCase("delete")) {
                if (args.length > 1) {
                    if (kitManager.findByName(args[1]) == null) return false;

                    this.dataSaver.delete(args[1]);
                    this.kitManager.removeKit(kitManager.findByName(args[1]));
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$DELETE));
                }
            }
        } else {
            if (this.arenaManager.findArenaByPlayer(player.getUniqueId()) != null) {
                player.sendMessage(colored(MessagesConfig.KIT$BLOCK_CHANGE_KIT));
                return false;
            }
        }
        KitInventoryProvider.INVENTORY.open(player);
        return true;
    }


}
