package essential.esl.base_v4;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public abstract class BaseSplashActivity_v4 extends AppCompatActivity {
    private boolean isLoading = true;
    private Thread commonThread;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(getLayoutResIdContentView());
        onCreateContentView();

        initAppCommon();

        waitUntilFinishInitAppCommon();
    }

    private void initAppCommon() {
        commonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                onInitAppCommon();
            }
        });
        commonThread.start();
    }

    private void waitUntilFinishInitAppCommon() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (commonThread != null) {
                    try {
                        commonThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onFinishInitAppCommon();
            }
        }, getSplashDuration());
    }

    public void enableOnBackPressed() {
        isLoading = false;
    }

    public boolean getStatusLoading() {
        return isLoading;
    }

    @Override
    public void onBackPressed() {
        if (!isLoading)
            super.onBackPressed();
    }

    protected long getSplashDuration() {
        return 2000l;
    }

    protected abstract int getLayoutResIdContentView();

    protected abstract void onCreateContentView();

    protected abstract void onInitAppCommon();

    protected abstract void onFinishInitAppCommon();

}
