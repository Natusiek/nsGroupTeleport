package pl.natusiek.grouptp.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.GroupTeleportPlugin;

import java.io.File;
import java.io.IOException;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class SetArenaCommand implements CommandExecutor {

    private final GroupTeleportPlugin plugin;

    public SetArenaCommand(GroupTeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = ((Player) sender);
        if (!player.hasPermission("nsGroupTeleport.setarena")) {
            player.sendMessage("&4Sory, ale nie masz uprawienien");
            return false;
        }
        if (args.length < 2) {
            player.sendMessage(colored("&4Ups! &cPoprawne uzycie: &f/setarena (nazwa) (wielkosc)"));
            return false;
        }
        final String name = args[0];
        int size = 20;
        try {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(colored("&cArgument nie jest liczba!"));
        }
        final Location location = player.getLocation();
        final FileConfiguration config = plugin.getConfig();
        final ConfigurationSection configurationSection = plugin.getConfig().createSection("arenas."+name);
        configurationSection.set(".location", location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ());
        configurationSection.set(".size", size);
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
            player.sendMessage(colored("&7Arena zostala dodana pomyslnie"));
            plugin.reloadConfig();
        } catch (IOException ex) {
            player.sendMessage(colored("&4Cos sie zjebalo, blad:" + ex.getMessage()));
        } finally {
            plugin.reloadConfig();
        }
        return false;
    }

}
