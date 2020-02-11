package pl.natusiek.grouptp.helper;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageHelper {

    public static String colored(String text) { return ChatColor.translateAlternateColorCodes('&', text); }

    public static List<String> colored(List<String> texts) {
        return texts
                .stream()
                .map(MessageHelper::colored)
                .collect(Collectors.toList());
    }

    public static void sendActionBar(Player player, String message) {
        final CraftPlayer craftPlayer = ((CraftPlayer) player);
        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + colored(message) + "\"}"), (byte)2));
    }

    private MessageHelper(){
    }

}
