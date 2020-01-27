package pl.natusiek.grouptp.helper;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerHelper {

    public static void spawnFirework(Location location) {
        final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();

        Color[] colors = { Color.YELLOW, Color.BLUE, Color.RED, Color.PURPLE, Color.LIME };

        final FireworkEffect effectBall = FireworkEffect.builder().withColor(colors).with(FireworkEffect.Type.BALL_LARGE).build();
        final FireworkEffect effectStar = FireworkEffect.builder().withColor(colors).with(FireworkEffect.Type.STAR).build();

        fireworkMeta.addEffects(effectBall, effectStar);
        fireworkMeta.setPower(2);

        firework.setFireworkMeta(fireworkMeta);
    }

    public static List<Player> findPlayersInRadius(Player base, int radius, int limit) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player ->
                        player.getLocation().distance(base.getLocation()) <= radius && !player.getUniqueId().equals(base.getUniqueId()))
                .limit(limit)
                .collect(Collectors.toCollection(() -> new ArrayList<>(limit)));
    }

    public static void TeleportPlayerToServer(Player player, String server) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream output = new DataOutputStream(b);
        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);

        player.sendMessage(colored(MessagesConfig.BUNGEE$MESSAGE_LEAVE));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                output.writeUTF("Connect");
                output.writeUTF(server);
            } catch (IOException ex) {
                ex.printStackTrace();
                player.sendMessage(colored("&4Upss, coś sie zespuło: &f" + ex.getMessage()));

            } finally {
                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                player.sendMessage(colored(MessagesConfig.BUNGEE$CONNECT_SERVER.replace("{SERVER}", server)));
            }
        }, 20L);
    }

    public static void addItemFromLobby(Player player) {
        player.setFireTicks(0);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFallDistance(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.teleport(LocationHelper.fromString("world, 200.0, 80.0, 200.0, 0.0f, 1.0f").toLocation());
        player.getInventory().setItem(0, new ItemBuilder(Material.BOOK).withName("&8* &6Wybor zestaw'u &8*").build());
        player.getInventory().setItem(4, new ItemBuilder(Material.COMPASS).withName("&8* &aObserwuj innych graczy &8*").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.FENCE_GATE).withName("&8* &cWyjscie z serwer'a &8*").build());
    }

}
