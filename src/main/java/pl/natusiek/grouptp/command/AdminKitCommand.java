package pl.natusiek.grouptp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.kit.Kit;
import pl.natusiek.grouptp.game.kit.KitDataSaver;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.game.kit.impl.KitImpl;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class AdminKitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final KitDataSaver dataSaver;

    public AdminKitCommand(KitManager kitManager, KitDataSaver dataSaver) {
        this.kitManager = kitManager;
        this.dataSaver = dataSaver;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = ((Player) sender);

        if (player.hasPermission("nsGroupTeleport.admin")) {
            if (args.length == 0) {
                player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$USE));
                return false;
            }
            String method = args[0];
            if (method.equalsIgnoreCase("create")) {
                if (args.length > 3) {
                    final PlayerInventory inventory = player.getInventory();

                    int rows, column;
                    try {
                        rows = Integer.parseInt(args[2]);
                        column = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        player.sendMessage(colored(MessagesConfig.COMMAND$NO_INT));
                        return false;
                    }
                    final Kit kit = this.kitManager.addKit(new KitImpl(args[1], rows, column, player.getItemInHand(), inventory.getContents(), inventory.getArmorContents()));
                    kit.setName(args[1]);
                    kit.setRows(rows);
                    kit.setColumn(column);
                    kit.setIcon(player.getItemInHand());
                    kit.setContent(inventory.getContents());
                    kit.setArmorContent(inventory.getArmorContents());
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$CREATED).replace("{KIT}", args[1]));
                    this.dataSaver.save(kit.getName());
                    return true;
                } else {
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$USE));
                }
            }
            if (method.equalsIgnoreCase("delete")) {
                if (args.length > 1) {
                    this.dataSaver.delete(args[1]);
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$DELETE));
                }
            }
        } else {
            player.sendMessage(colored(MessagesConfig.COMMAND$NO_PERMISSION));
        }
        return false;
    }


}
