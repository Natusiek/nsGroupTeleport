package pl.natusiek.grouptp.helper;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

    public static void sendActionbar(Player player, String text) {
        final IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + colored(text) + "\"}");
        final PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        final CraftPlayer craftPlayer = ((CraftPlayer) player);

        craftPlayer.getHandle().playerConnection.sendPacket(bar);
    }

    private MessageHelper(){
    }
}
