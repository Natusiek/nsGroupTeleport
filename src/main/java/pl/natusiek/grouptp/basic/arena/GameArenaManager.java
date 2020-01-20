package pl.natusiek.grouptp.basic.arena;

import java.util.List;
import java.util.UUID;

public interface GameArenaManager {

  void addArena(GameArena arena);

  List<GameArena> getArenas();

  GameArena findArenaByPlayer(UUID uuid);

}
