package pl.natusiek.grouptp.helper;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.game.arena.Arena;

import java.lang.reflect.Constructor;

public final class BorderHelper {

    public static void setBorder(Arena arena, Player player, LocationHelper center, int radius) {
        setBorder(player, arena.setBorder(player.getUniqueId(), center, radius));
    }

    private static void setBorder(Player player, WorldBorder border) {
       try {
            Object sizeEnum = ReflectionHelper.getNMSClass("PacketPlayOutWorldBorder").getDeclaredClasses()[1].getField("SET_SIZE").get(null);
            Object centerEnum = ReflectionHelper.getNMSClass("PacketPlayOutWorldBorder").getDeclaredClasses()[1].getField("SET_CENTER").get(null);

            Constructor<?> packetConstructor = ReflectionHelper.getNMSClass("PacketPlayOutWorldBorder")
                    .getConstructor(ReflectionHelper.getNMSClass("WorldBorder"), ReflectionHelper.getNMSClass("PacketPlayOutWorldBorder").getDeclaredClasses()[1]);

            Object sizePacket = packetConstructor.newInstance(border.getNMSBorder(), sizeEnum);
            Object centerPacket = packetConstructor.newInstance(border.getNMSBorder(), centerEnum);


            ReflectionHelper.sendPacket(player, sizePacket);
            ReflectionHelper.sendPacket(player, centerPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBorder(Player player, Location center, int radius) {
        final WorldBorder border = new WorldBorder();

        border.setCenter(center.getX(), center.getZ());
        border.setSize(radius);
        setBorder(player, border);
    }

    public static class WorldBorder {
        private double x,z, size;

        public void setCenter(double x, double z) {
            this.x = x;
            this.z = z;
        }

        public void setSize(double size) {
            this.size = size;
        }

        public double getCenterX() {
            return x;
        }

        public double getCenterZ() {
            return z;
        }

        public double getSize() {
            return size;
        }

        public Object getNMSBorder() {
            Object borderObject;
            try {
                borderObject = ReflectionHelper.getNMSClass("WorldBorder").getDeclaredConstructors()[0].newInstance();
                borderObject.getClass().getMethod("setCenter", double.class, double.class).invoke(borderObject, x, z);
                borderObject.getClass().getMethod("setSize", double.class).invoke(borderObject, size);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            return borderObject;
        }
    }

    private BorderHelper(){
    }


}
