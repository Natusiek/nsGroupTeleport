package pl.natusiek.grouptp.helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationHelper {
	
	public static String toString(Location location) { 
		return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
	}
	
	public static Location fromString(String string) {
		final String[] parts = string.split(";");
		final World world = Bukkit.getWorld(parts[0]);
		final double x = Double.parseDouble(parts[1]);
		final double y = Double.parseDouble(parts[2]);    
		final double z = Double.parseDouble(parts[3]);
		return new Location(world, x, y, z);
	}
	
	private LocationHelper() {
	}
	
}
