package pl.natusiek.grouptp.helper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        message = colored(message);
        String nmsVersion = (nmsVersion = Bukkit.getServer().getClass().getPackage().getName()).substring(nmsVersion.lastIndexOf(".") + 1);
        try {
            Object ppoc;
            Class<?> c2, c3,
                    c4 = ReflectionHelper.getNMSClass("PacketPlayOutChat");
            Object o;
            if ((nmsVersion.equalsIgnoreCase("v1_8_R1") || !nmsVersion.startsWith("v1_8_")) && !nmsVersion.startsWith("v1_9_")) {
                c2 = ReflectionHelper.getNMSClass("ChatSerializer");
                c3 = ReflectionHelper.getNMSClass("IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                o = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
            } else {
                c2 =  ReflectionHelper.getNMSClass("ChatComponentText");
                c3 =  ReflectionHelper.getNMSClass("IChatBaseComponent");
                o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            }
            ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            ReflectionHelper.sendPacket(player, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private MessageHelper(){
    }

}
