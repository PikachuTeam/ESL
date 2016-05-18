package essential.esl.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import essential.esl.R;
import essential.esl.app.BaseESLActivity;
import essential.esl.base_v4.BaseFragment_v4;
import essential.esl.ui.parentfragment.MainFragment;
import essential.esl.ui.parentfragment.SplashFragment;
import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.util.CloseAppHandler;

public class MainActivity extends BaseESLActivity implements CloseAppHandler.OnCloseAppListener {
    private ObjectAnimator logoScaleX, logoScaleY, logoTransTop;
    private ImageView logo;
    private CloseAppHandler closeAppHandler;
    private FrameLayout fragmentParent;

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
        logo = (ImageView) findViewById(R.id.logo);
        fragmentParent = (FrameLayout) findViewById(getParentFragmentContainerId());
        closeAppHandler = new CloseAppHandler(this, false);
        closeAppHandler.setListener(this);
    }

    @Override
    protected void onInitAppCommon() {
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "eslquizzes.db");
    }

    @Override
    protected void onFinishInitAppCommon() {
        enableOnBackPressed();
        animLogo();
        replaceParentFragment();

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

    public void animLogo() {
        logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 0.5f);
        logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 0.5f);
        logoTransTop = ObjectAnimator.ofFloat(logo, "translationY", 0, -(int) (logo.getHeight() * 1.2));
        logoScaleX.setDuration(600);
        logoScaleY.setDuration(600);
        logoTransTop.setDuration(600);
        logoTransTop.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(logoScaleX).with(logoScaleY).with(logoTransTop);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentParentFragment() != null) {
            int childFragmentCount = getCurrentParentFragment().getChildFragmentManager().getBackStackEntryCount();
            if (childFragmentCount == 0) {
                closeAppHandler.setKeyBackPress(this);
            } else getCurrentParentFragment().getChildFragmentManager().popBackStack();
        }
    }

    @Override
    public void onRateAppDialogClose() {
        finish();
    }

    @Override
    public void onTryToCloseApp() {
        makeMessage(fragmentParent, R.string.press_again_to_exit);
    }

    @Override
    public void onReallyWantToCloseApp() {
        finish();
    }
}
