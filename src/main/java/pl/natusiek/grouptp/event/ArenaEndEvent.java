package pl.natusiek.grouptp.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.natusiek.grouptp.game.arena.Arena;

import java.util.UUID;


public class ArenaEndEvent extends Event {

    private final Arena arena;
    private final UUID winner, losser;

    private static final HandlerList handlerList = new HandlerList();

    public ArenaEndEvent(Arena arena, UUID winner, UUID losser) {
        this.arena = arena;
        this.winner = winner;
        this.losser = losser;
    }

    public Arena getArena() { return arena; }

    public UUID getWinner() { return winner; }

    public UUID getLosser() { return losser; }

    @Override
    public HandlerList getHandlers() {  return handlerList; }

    public static HandlerList getHandlerList() { return handlerList; }

}
