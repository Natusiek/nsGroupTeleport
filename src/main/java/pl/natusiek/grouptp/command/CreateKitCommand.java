package pl.natusiek.grouptp.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentData;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataManager;
import pl.natusiek.grouptp.basic.kit.equipment.EquipmentDataSaver;
import pl.natusiek.grouptp.config.MessagesConfig;

import java.io.IOException;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class CreateKitCommand implements CommandExecutor {

    private final EquipmentDataSaver dataSaver;
    private final EquipmentDataManager dataManager;

    public CreateKitCommand(EquipmentDataManager dataManager, EquipmentDataSaver dataSaver) {
        this.dataSaver = dataSaver;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = ((Player) sender);
        if (player.hasPermission("nsGroupTeleport.createkit")) {
            if (args.length > 2) {
                final EquipmentData data = this.dataManager.createEquipmentData(args[0]);
                final PlayerInventory inventory = player.getInventory();

                if (player.getItemInHand() == null) {
                    player.sendMessage(colored(MessagesConfig.COMMAND$CREATE_KIT$ITEM_AIR));
                    return false;
                }
                int rows;
                int column;
                try {
                    rows = Integer.parseInt(args[1]);
                    column = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(colored(MessagesConfig.COMMAND$NO_INT));
                    return false;
                }
                data.setName(args[0]);
                data.setRows(rows);
                data.setColumn(column);
                data.setIcon(player.getItemInHand());
                data.setContent(inventory.getContents());
                data.setArmorContent(inventory.getArmorContents());
                player.sendMessage(colored(MessagesConfig.COMMAND$CREATE_KIT$CREATED).replace("{KIT}", args[0]));
                this.dataSaver.save();
                return true;
            } else {
                player.sendMessage(colored(MessagesConfig.COMMAND$CREATE_KIT$USE));
            }
        } else {
            player.sendMessage(colored(MessagesConfig.COMMAND$NO_PERMISSION));
        }
        return false;
    }

}
