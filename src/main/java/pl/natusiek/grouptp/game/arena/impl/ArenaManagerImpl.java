package pl.natusiek.grouptp.game.arena.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;

public class ArenaManagerImpl implements ArenaManager {

    private final List<Arena> arenas = new ArrayList<>();

    @Override
    public void addArena(Arena arena) { this.arenas.add(arena); }

    @Override
    public List<Arena> getArenas() { return new ArrayList<>(arenas); }

    @Override
    public Arena findArenaByPlayer(UUID uuid) {
        return this.arenas
                .stream()
                .filter(arena -> arena.isPlaying(uuid))
                .findFirst()
                .orElse(null);
    }

}
