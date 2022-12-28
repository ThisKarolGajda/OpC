package me.opkarol.opc.api.misc;

public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public long getTotalTime() {
        return (endTime - startTime);
    }

    @Override
    public String toString() {
        return String.format("Total time: %d microseconds / %d milliseconds", getTotalTime(), getTotalTime() / 1000);
    }
}
