package me.bkrmt.bkcore.textanimator;

import org.bukkit.scheduler.BukkitRunnable;

public class CancellableTask {
    private final Runnable runnable;
    private BukkitRunnable bukkitRunnable;
    private final SchedulerRunnable schedulerRunnable;
    private boolean cancelled;

    public CancellableTask(Runnable runnable, SchedulerRunnable schedulerRunnable) {
        this.runnable = runnable;
        this.schedulerRunnable = schedulerRunnable;
        this.cancelled = true;
    }

    public BukkitRunnable getBukkitRunnable() {
        return bukkitRunnable;
    }

    public SchedulerRunnable getSchedulerRunnable() {
        return schedulerRunnable;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        if (!cancelled) {
            this.cancelled = true;
            this.bukkitRunnable.cancel();
            bukkitRunnable = null;
        }
    }

    public void start() {
        if (cancelled) {
            this.cancelled = false;
            bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            };
            schedulerRunnable.run(bukkitRunnable);
        }
    }
}
