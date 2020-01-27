package pl.natusiek.grouptp.helper;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionHelper {
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        //net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction
        //net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]; // v1_8_R3

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
