package pl.natusiek.grouptp.game.spectate;

import org.bukkit.entity.Player;
import pl.natusiek.grouptp.game.arena.Arena;

import java.util.UUID;

public interface ArenaSpectate {

    void joinSpectate(Player player, Arena arena);

    void leaveSpectate(Player player);

}
