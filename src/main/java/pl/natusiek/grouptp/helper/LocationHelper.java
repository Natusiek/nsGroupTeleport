package pl.natusiek.grouptp.helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationHelper {

    private final World world;
    private final double x, y, z;
    private final float yaw, pitch;

    public LocationHelper(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location toLocation() { return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch); }

    public static LocationHelper fromString(String string) {
        final String[] parts = string.split(", ");

        final World world = Bukkit.getWorld(parts[0]);
        final double x = Double.parseDouble(parts[1]);
        final double y = Double.parseDouble(parts[2]);
        final double z = Double.parseDouble(parts[3]);
        final float yaw = Float.parseFloat(parts[4]);
        final float pitch = Float.parseFloat(parts[5]);

        return new LocationHelper(world, x, y, z, yaw, pitch);
    }

    public static LocationHelper toString(Location location) {
        return new LocationHelper(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
