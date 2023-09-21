package nsu.ccfit.ru.tihomolov.server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;
import static nsu.ccfit.ru.tihomolov.context.Context.*;

public class SpeedScheduler {
    private final Timer timer;
    private long summarySize = 0;
    private long currentSize = 0;
    private final Long startTime;
    private Long lastTimeUpdate;
    private Long timeSinceLastUpdate;

    public SpeedScheduler() {
        this.startTime = System.currentTimeMillis();
        this.lastTimeUpdate = System.currentTimeMillis();
        this.timer = new Timer();
    }

    public void initSpeedTracker() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                System.out.println("Current speed: " + (double)currentSize / timeSinceLastUpdate * MILLI_TO_SECONDS);
                System.out.println("Average speed: " + (double)summarySize / (lastTimeUpdate - startTime) * MILLI_TO_SECONDS / 1_000_000);
            }
        };
        timer.scheduleAtFixedRate(task, DELAY, PERIOD);
    }

    public void updateData(int dataSize) {
        this.currentSize = dataSize;
        this.summarySize += dataSize;
        this.timeSinceLastUpdate = System.currentTimeMillis() - lastTimeUpdate;
        this.lastTimeUpdate = System.currentTimeMillis();
    }

    public void stopSpeedTracker() {
        try {
            sleep(BEFORE_CANSEL);
            timer.cancel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
