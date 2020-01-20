package pl.natusiek.grouptp.basic.arena.impl;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;

public class GameArenaManagerImpl implements GameArenaManager {

  private final List<GameArena> arenas = new ArrayList<>();

  @Override
  public void addArena(GameArena arena) { this.arenas.add(arena); }

  @Override
  public List<GameArena> getArenas() { return new ArrayList<>(arenas); }

  @Override
  public GameArena findArenaByPlayer(UUID uuid) {
    return this.arenas
        .stream()
        .filter(arena -> arena.isPlaying(uuid))
        .findFirst()
        .orElse(null);
  }
  
}
