package essential.esl.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import essential.esl.R;


/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public abstract class MyBaseSplashActivity extends AppCompatActivity {
    private boolean isLoading = true;
    private Thread commonThread;
    private HashMap<String, Object> objectHolder;


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        objectHolder = new HashMap<>();
        setContentView(getLayoutResIdContentView());
        onCreateContentView();

        initAppCommon();

        waitUntilFinishInitAppCommon();
    }

    public HashMap<String, Object> getMapHolder() {
        return objectHolder;
    }

    public Object getHolder(String key) {
        return objectHolder.get(key);
    }

    public void putHolder(String key, Object value) {
        objectHolder.put(key, value);
    }

    public boolean containHolder(String key) {
        return objectHolder.containsKey(key);
    }

    public void removeHolder(String key) {
        objectHolder.remove(key);
    }

    public void clearAllHolder() {
        objectHolder.clear();
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


    public void onCloseActivity() {
        this.finish();
    }

    public MyBaseFragment findFragment(String fragmentTag) {
        return (MyBaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    public void popFragment() {
        onBackPressed();
    }

    public void popToFragment(String transactionTag) {
        getFragmentManager().popBackStack(transactionTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void popToFirstFragment() {
        getFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void replaceFragment(MyBaseFragment fragment, String fragmentTag, String transactionTag, boolean needCommitAllowingStateLoss) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_exit,
                R.anim.left_enter, R.anim.right_exit);
        transaction.replace(getParentFragmentContainerId(), fragment, fragmentTag).addToBackStack(transactionTag);
        if (needCommitAllowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }


    protected long getSplashDuration() {
        return 2000l;
    }

    protected abstract int getLayoutResIdContentView();

    protected abstract void onCreateContentView();

    protected abstract void onInitAppCommon();

    protected abstract void onFinishInitAppCommon();

    protected abstract int getParentFragmentContainerId();

}
