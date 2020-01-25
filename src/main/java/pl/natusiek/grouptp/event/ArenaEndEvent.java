package pl.natusiek.grouptp.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.natusiek.grouptp.game.arena.Arena;

import java.util.UUID;


public class ArenaEndEvent extends Event {

    private final Arena arena;
    private final UUID winner;

    private static final HandlerList handlerList = new HandlerList();

    public ArenaEndEvent(Arena arena, UUID winner) {
        this.arena = arena;
        this.winner = winner;
    }

    public Arena getArena() { return arena; }

    public UUID getWinner() { return winner; }

    @Override
    public HandlerList getHandlers() {  return handlerList; }

    public static HandlerList getHandlerList() { return handlerList; }
}
