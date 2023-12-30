package ltd.rymc.form.tpa.event;

import ltd.rymc.form.tpa.tpa.TpaMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TpaFormReceiveEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player targetPlayer;
    private final Player fromPlayer;
    private final TpaMode mode;
    private boolean cancelled = false;

    public TpaFormReceiveEvent(Player targetPlayer, Player fromPlayer, TpaMode mode){
        this.targetPlayer = targetPlayer;
        this.fromPlayer = fromPlayer;
        this.mode = mode;
    }

    public TpaMode getMode(){
        return mode;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
