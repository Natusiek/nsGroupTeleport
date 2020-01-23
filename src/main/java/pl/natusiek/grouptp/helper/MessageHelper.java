package pl.natusiek.grouptp.helper;


import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

	public static void sendTitle(Player player, String messagetitle, String messagesubtitle) {
		final IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + colored(messagetitle) + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");
		final IChatBaseComponent subchatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + colored(messagesubtitle) + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

		final PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
		final PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subchatTitle);

		PacketPlayOutTitle length = new PacketPlayOutTitle(5, 60, 5);

		final CraftPlayer craftPlayer = (CraftPlayer) player;
		
		craftPlayer.getHandle().playerConnection.sendPacket(title);
		craftPlayer.getHandle().playerConnection.sendPacket(subtitle);

		craftPlayer.getHandle().playerConnection.sendPacket(length);
	}

	public static void sendActionbar(Player player, String text) {
		final IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + colored(text) + "\"}");
		final PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
		final CraftPlayer craftPlayer = (CraftPlayer) player;

		craftPlayer.getHandle().playerConnection.sendPacket(bar);
	}

	private MessageHelper() {
	}
}
