package pl.natusiek.grouptp.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.Helper;
import pl.natusiek.grouptp.helper.LocationHelper;

import java.io.File;
import java.io.IOException;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class SetArenaCommand implements CommandExecutor {

    private final GroupTeleportPlugin plugin;

    public SetArenaCommand(GroupTeleportPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = ((Player) sender);
        if (player.hasPermission("nsGroupTeleport.setarena")) {
            if (args.length > 1) {

                final String arenaName = args[0];
                int arenaSize = Integer.parseInt(args[1]);

                final Location location = player.getLocation();
                final ConfigurationSection section = plugin.getConfig().createSection("arenas." + arenaName);
                section.set(".size", arenaSize);
                section.set(".location", location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + location.getYaw() + ", " + location.getPitch());
                try {
                    plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
                    player.sendMessage(colored(MessagesConfig.COMMAND$SET_ARENA$ADD));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(colored(MessagesConfig.COMMAND$SET_ARENA$USE));
                return false;
            }
        } else {
            player.sendMessage(colored(MessagesConfig.COMMAND$NO_PERMISSION));
            return false;
        }
        return false;
    }

}