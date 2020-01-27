package pl.natusiek.grouptp.helper;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.game.arena.Arena;

public final class BorderHelper {

    public static void setBorder(Arena arena, Player player, LocationHelper center, int radius) {
        final WorldBorder border = arena.setBorder(player.getUniqueId(), center, radius);

        final Packet setSize = new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
        final Packet setCenter = new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);

        final CraftPlayer craftPlayer = (CraftPlayer) player;

        craftPlayer.getHandle().playerConnection.sendPacket(setSize);
        craftPlayer.getHandle().playerConnection.sendPacket(setCenter);
    }

    public static void setBorder(Player player, Location center, int radius) {
        final WorldBorder border = new WorldBorder();
        border.setCenter(center.getX(), center.getZ());
        border.setSize(radius);

        final Packet setSize = new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
        final Packet setCenter = new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);
        final CraftPlayer craftPlayer = (CraftPlayer) player;

        craftPlayer.getHandle().playerConnection.sendPacket(setSize);
        craftPlayer.getHandle().playerConnection.sendPacket(setCenter);
    }

    private BorderHelper() {
    }

}
