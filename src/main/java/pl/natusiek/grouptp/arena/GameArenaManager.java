package pl.natusiek.grouptp.arena;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface GameArenaManager {

  void addArena(GameArena arena);

  List<GameArena> getArenas();

  GameArena findArenaByPlayer(UUID uuid);

}
