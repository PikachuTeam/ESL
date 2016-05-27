package essential.esl.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;

import essential.esl.R;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.ui.fragment.HomeFragment;
import essential.esl.ui.fragment.SplashFragment;
import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.util.CloseAppHandler;

public class MainActivity extends MyBaseActivity implements CloseAppHandler.OnCloseAppListener {
    private ObjectAnimator logoScaleX, logoScaleY, logoTransTop, logoShowX, logoShowX1, logoHideX, logoHideX1;
    private ImageView logo;
    private CloseAppHandler closeAppHandler;
    private FrameLayout fragmentParent;
    private int DURATION_ANIM = 600;

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
        animSplashLogo();
        replaceParentFragment();

    }

    @Override
    protected MyBaseFragment getFragmentContent() {
        return new SplashFragment();
    }

    private void replaceParentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        transaction.setCustomAnimations(R.anim.top_enter, R.anim.fade_out);
        transaction.replace(getParentFragmentContainerId(), fragment, fragment.getClass().getName());
        transaction.commit();
    }

    public void animSplashLogo() {
        logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 0.5f);
        logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 0.5f);
        logoTransTop = ObjectAnimator.ofFloat(logo, "translationY", 0, -(int) (logo.getHeight() * 1.2));
        logoScaleX.setDuration(DURATION_ANIM);
        logoScaleY.setDuration(DURATION_ANIM);
        logoTransTop.setDuration(DURATION_ANIM);
        logoTransTop.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(logoScaleX).with(logoScaleY).with(logoTransTop);
        animatorSet.start();
    }

    public void animShowLogo() {
        logoShowX = ObjectAnimator.ofFloat(logo, "translationX", -logo.getWidth() * 3, logo.getWidth() / 5);
        logoShowX1 = ObjectAnimator.ofFloat(logo, "translationX", logo.getWidth() / 5, 0);
        logoShowX.setDuration(DURATION_ANIM - DURATION_ANIM / 4);
        logoShowX1.setDuration(DURATION_ANIM / 5);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(logoShowX).before(logoShowX1);
        animatorSet.start();
    }

    public void animHideLogo() {
        logoHideX1 = ObjectAnimator.ofFloat(logo, "translationX", 0, logo.getWidth() / 6);
        logoHideX = ObjectAnimator.ofFloat(logo, "translationX", logo.getWidth() / 4, -logo.getWidth() * 4);
        logoHideX.setDuration(DURATION_ANIM - DURATION_ANIM / 3);
        logoHideX1.setDuration(DURATION_ANIM / 6);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(logoHideX1).before(logoHideX);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentCount > 0) {
            getCurrentFragment().onBackPress();
            if (fragmentCount == 1)
                animShowLogo();
        } else if (!getStatusLoading()) closeAppHandler.setKeyBackPress(this);
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
