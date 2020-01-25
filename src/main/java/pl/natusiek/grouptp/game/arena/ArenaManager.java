package pl.natusiek.grouptp.game.arena;

import pl.natusiek.grouptp.game.arena.Arena;

import java.util.List;
import java.util.UUID;

public interface ArenaManager {

    void addArena(Arena arena);

    List<Arena> getArenas();

    Arena findArenaByPlayer(UUID uuid);
}
