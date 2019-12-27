package pl.natusiek.grouptp.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.natusiek.grouptp.arena.GameArena;

public class ArenaEndEvent extends Event {

  private final GameArena arena;
  private final UUID winner;

  private static final HandlerList handlerList = new HandlerList();

  public ArenaEndEvent(GameArena arena, UUID winner) {
    this.arena = arena;
    this.winner = winner;
  }
  
  public GameArena getArena() { return arena; }

  public UUID getWinner() { return winner; }

  @Override
  public HandlerList getHandlers() {  return handlerList; }

  public static HandlerList getHandlerList() { return handlerList; }

}
