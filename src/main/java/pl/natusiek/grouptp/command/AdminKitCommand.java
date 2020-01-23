package pl.natusiek.grouptp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentData;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataManager;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataSaver;
import pl.natusiek.grouptp.config.MessagesConfig;

import java.io.File;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class AdminKitCommand implements CommandExecutor {

    private final EquipmentDataSaver dataSaver;
    private final EquipmentDataManager dataManager;

    public AdminKitCommand(EquipmentDataManager dataManager, EquipmentDataSaver dataSaver) {
        this.dataSaver = dataSaver;
        this.dataManager = dataManager;
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
                    final EquipmentData data = this.dataManager.createEquipmentData(args[0]);
                    final PlayerInventory inventory = player.getInventory();

                    if (player.getItemInHand() == null) {
                        player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$ITEM_AIR));
                        return false;
                    }
                    int rows;
                    int column;
                    try {
                        rows = Integer.parseInt(args[2]);
                        column = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        player.sendMessage(colored(MessagesConfig.COMMAND$NO_INT));
                        return false;
                    }
                    data.setName(args[1]);
                    data.setRows(rows);
                    data.setColumn(column);
                    data.setIcon(player.getItemInHand());
                    data.setContent(inventory.getContents());
                    data.setArmorContent(inventory.getArmorContents());
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$CREATED).replace("{KIT}", args[0]));
                    this.dataSaver.save();
                    return true;
                } else {
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$USE));
                }
            }
            if (method.equalsIgnoreCase("delete")) {
                if (args.length > 1 ) {
                    this.dataManager.deleteEquipmentData(args[1]);
                    this.dataSaver.delete(args[1]);
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$DELETE));
                } else {
                    player.sendMessage(colored(MessagesConfig.COMMAND$ADMIN_KIT$USE));
                }
            }
        } else {
            player.sendMessage(colored(MessagesConfig.COMMAND$NO_PERMISSION));
        }
        return false;
    }

}
