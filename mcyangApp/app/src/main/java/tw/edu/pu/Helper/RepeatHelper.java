package tw.edu.pu.Helper;

import android.os.Handler;
import android.os.Looper;

public class RepeatHelper {
    private boolean started = false;
    private int timer;
    private final Handler handler;
    private final Runnable runnable;

    public RepeatHelper(NewRunner newRunner) {
        handler = new Handler(Looper.getMainLooper());
        runnable = () -> {
            try {
                newRunner.newRunner();
                if (started)
                    start();

            } catch (ExceptionInInitializerError | Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, timer);
    }

    public void start(int timer) {
        this.timer = timer;
        started = true;
        handler.postDelayed(runnable, timer);
    }

    public interface NewRunner {
        void newRunner();
    }
}
