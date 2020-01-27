package pl.natusiek.grouptp.helper;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public final class Serializer {

    public static String serializeInventory(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(items);
            String encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            outputStream.close();
            dataOutput.close();
            return encoded;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save itemstack array", e);
        }
    }

    public static ItemStack[] deserializeInventory(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] read = (ItemStack[]) dataInput.readObject();
            inputStream.close();
            dataInput.close();
            return read;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to read class type", e);
        }
    }

    private Serializer() {
    }

}
