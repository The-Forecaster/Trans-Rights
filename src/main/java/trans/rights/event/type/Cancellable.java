package trans.rights.event.type;

/**
 * Default implementation of {@link ICancellable}
 * 
 * @author Austin
 */
public abstract class Cancellable implements ICancellable {
    private boolean cancelled;

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
