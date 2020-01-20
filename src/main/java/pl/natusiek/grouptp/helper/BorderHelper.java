package pl.natusiek.grouptp.helper;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_8_R3.WorldBorder;

import pl.natusiek.grouptp.basic.arena.GameArena;

public final class BorderHelper {

	public static void setBorder(GameArena arena, Player player, Location center, int radius) {
		final WorldBorder border = arena.setBorder(player.getUniqueId(), center, radius);

		final Packet setSize = new PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_SIZE);
		final Packet setCenter = new PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_CENTER);

		final CraftPlayer craftPlayer = (CraftPlayer) player;

		craftPlayer.getHandle().playerConnection.sendPacket(setSize);
		craftPlayer.getHandle().playerConnection.sendPacket(setCenter);
	}

	public static void setBorder(Player player, Location center, int radius) {
		final WorldBorder border = new WorldBorder();
		border.setCenter(center.getX(), center.getZ());
		border.setSize(radius);

		final Packet setSize = new PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_SIZE);
		final Packet setCenter = new PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_CENTER);
		final CraftPlayer craftPlayer = (CraftPlayer) player;

		craftPlayer.getHandle().playerConnection.sendPacket(setSize);
		craftPlayer.getHandle().playerConnection.sendPacket(setCenter);
	}

	private BorderHelper() {
	}
}
