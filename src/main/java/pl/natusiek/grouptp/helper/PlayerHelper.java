package pl.natusiek.grouptp.helper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public final class PlayerHelper {

    private static GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);

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
        final List<Player> players = Bukkit.getOnlinePlayers()
                .stream()
                .filter(player ->
                        player.getLocation().distance(base.getLocation()) <= radius && !player.getUniqueId().equals(base.getUniqueId()))
                .limit(limit)
                .collect(Collectors.toCollection(() -> new ArrayList<>(limit)));

        return players;
    }

    public static void TeleportPlayerToServer(Player player, String server) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream output = new DataOutputStream(b);
        final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);

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
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, plugin.getConfigManager().getItemKits());
        player.getInventory().setItem(3, plugin.getConfigManager().getItemSpectator());
        player.getInventory().setItem(8, plugin.getConfigManager().getItemLeaveServer());
    }

}