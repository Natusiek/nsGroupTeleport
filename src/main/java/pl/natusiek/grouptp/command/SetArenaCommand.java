package pl.natusiek.grouptp.command;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class SetArenaCommand implements CommandExecutor {

    private final GroupTeleportPlugin plugin;

    public SetArenaCommand(GroupTeleportPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = ((Player) sender);
        if (player.hasPermission("nsGroupTeleport.setarena")) {
            if (args.length < 2) {
                final String arenaName = args[1];
                int arenaSize;
                try {
                    arenaSize = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(colored(MessagesConfig.COMMAND$NO_INT));
                    return false;
                }
                final Location location = player.getLocation();
                final ConfigurationSection section = plugin.getConfig().createSection("arenas."+arenaName);
                section.set(".location", location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ());
                section.set(".size", arenaSize);
                try {
                    plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
                    player.sendMessage(colored(MessagesConfig.COMMAND$SET_ARENA$ADD));
                    plugin.reloadConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(colored("&4Cos sie zjebalo, blad:" + e.getMessage()));
                }
            } else {
                player.sendMessage(colored(MessagesConfig.COMMAND$SET_ARENA$USE));
            }
        } else {
            player.sendMessage(colored(MessagesConfig.COMMAND$NO_PERMISSION));
        }
        return true;
    }
}
