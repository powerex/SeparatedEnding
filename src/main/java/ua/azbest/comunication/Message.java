package ua.azbest.comunication;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.toIntExact;

public class Message implements Delayed {

    private long startTime;
    private long sourceId;
    private double taskSize;
    private final long DELAY = 150;

    public Message(long sourceId, double taskSize) {
        this.sourceId = sourceId;
        this.taskSize = taskSize;
        this.startTime = System.currentTimeMillis() + 150;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public double getTaskSize() {
        return taskSize;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return toIntExact(
                this.startTime - ((Message) o).startTime);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sourceId=" + sourceId +
                ", taskSize=" + taskSize +
                '}';
    }
}
