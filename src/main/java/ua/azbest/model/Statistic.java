package ua.azbest.model;

public class Statistic {

    private int id;
    private int on;
    private long time;
    private String percents;

    public Statistic(int id, int on, long time, String percents) {
        this.id = id;
        this.on = on;
        this.time = time;
        this.percents = percents;
    }

    public int getId() {
        return id;
    }

    public int getOn() {
        return on;
    }

    public long getTime() {
        return time;
    }

    public String getPercents() {
        return percents;
    }
}
