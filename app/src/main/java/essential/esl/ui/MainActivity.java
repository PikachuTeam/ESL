package essential.esl.ui;

import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import essential.esl.R;
import essential.esl.app.BaseESLActivity;
import essential.esl.base_v4.BaseFragment_v4;
import essential.esl.ui.parentfragment.MainFragment;
import essential.esl.ui.parentfragment.SplashFragment;
import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;

public class MainActivity extends BaseESLActivity {


    @Override
    protected int getParentFragmentContainerId() {
        return R.id.parent_fragment;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {
        addFragmentContent();
    }

    @Override
    protected void onInitAppCommon() {
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "eslquizzes.db");
    }

    @Override
    protected void onFinishInitAppCommon() {
        replaceParentFragment();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected BaseFragment_v4 getFragmentContent() {
        return new SplashFragment();
    }

    private void replaceParentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainFragment fragment = new MainFragment();
        transaction.setCustomAnimations(R.anim.top_enter, R.anim.fade_out);
        transaction.replace(getParentFragmentContainerId(), fragment, fragment.getClass().getName());
        transaction.commit();
    }
}
