package essential.esl.ui.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.io.File;

import essential.esl.BuildConfig;
import essential.esl.R;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyDialog;
import essential.esl.ui.fragment.HomeFragment;
import essential.esl.ui.fragment.SplashFragment;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.ads.AdsBigBannerHandler;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;
import tatteam.com.app_common.util.CloseAppHandler;

public class MainActivity extends MyBaseActivity implements CloseAppHandler.OnCloseAppListener, BillingProcessor.IBillingHandler {

    private static final String DEV_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAndABnSj0c2ac57R6fn8tXgTt0k2QXltVmuOf5ChtTniLhgBvXzW9IKhEb5C6tZJxxIIzblKlfZyMAno5xyeMRKKo0kj4pUbaWtJggWWXpm73syj5LVUiU2UuspHK3nWrgyJwn+HDoLcOItCTyr5m5R46/6dh+SBjwglglVwmGUtPxx65BlnUaPWEAKLSTiOT8KlDCZvBy8vXgwiDBDi4SoOOtYfMqdULU2vd599LsHOk21z6lmEemI6Qr13zqGCQsmAd3AKxDeaTi5IRpC51tuQmEXMN+EoOwUwn9Vyk1GChXH2ffEFfOXYSc23Byz0ldTe+cnikyhA+3beud0p6IwIDAQAB";
    private static final String PURCHASE_PRO_VERSION_ID = "to_pro_version";

    public static AppConstant.AdsType ADS_TYPE_SMALL;
    public static AppConstant.AdsType ADS_TYPE_BIG;
    public final static int BIG_ADS_SHOWING_INTERVAL = 8;
    public static int adsCounter;
    public final static int RATE_APP_INTERVAL = 10;
    public static int rateAppCounter = 1;
    private AppRate appRate;

    private final int PERMISSION_REQUEST_CODE = 1;
    public static final String ESL = "englishsecondlanguage";
    public static final String IS_PRO_VERSION = "is_pro_version";
    private ObjectAnimator logoScaleX, logoScaleY, logoTransTop, logoShowX, logoShowX1, logoHideX, logoHideX1;
    private ImageView logo;
    private CloseAppHandler closeAppHandler;
    private FrameLayout fragmentParent;
    private int DURATION_ANIM = 600;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private BillingProcessor billingProcessor;
    private AdsBigBannerHandler adsBigBannerHandler;
    private int hackCounter;

    @Override
    protected int getParentFragmentContainerId() {
        return R.id.parent_fragment;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isProVersion() {
        return sharedPref.getBoolean(IS_PRO_VERSION, false);
    }

    public void setProVersion(boolean isProVersion) {
        editor.putBoolean(IS_PRO_VERSION, isProVersion);
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    onCloseActivity();
                }
                break;
        }
    }

    @Override
    protected void onCreateContentView() {
        addFragmentContent();
        logo = (ImageView) findViewById(R.id.logo);
        fragmentParent = (FrameLayout) findViewById(getParentFragmentContainerId());
        closeAppHandler = new CloseAppHandler(this, false);
        closeAppHandler.setListener(this);
        sharedPref = this.getSharedPreferences(
                ESL, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isProVersion()) {
                    hackCounter++;
                    if (hackCounter % 10 == 0) {
                        setProVersion(true);
                        getCurrentFragment().onAppUpgraded();
                    }
                }
            }
        });

        if (!isProVersion() && BillingProcessor.isIabServiceAvailable(this)) {
            billingProcessor = new BillingProcessor(this, DEV_KEY, this);
        }

    }


    @Override
    protected void onInitAppCommon() {
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "eslquizzes.db");

        if (!isProVersion()) {
            if (BuildConfig.DEBUG) {
                MainActivity.ADS_TYPE_SMALL = AppConstant.AdsType.SMALL_BANNER_TEST;
                MainActivity.ADS_TYPE_BIG = AppConstant.AdsType.BIG_BANNER_TEST;
            } else {
                MainActivity.ADS_TYPE_SMALL = AppConstant.AdsType.SMALL_BANNER_LANGUAGE_LEARNING;
                MainActivity.ADS_TYPE_BIG = AppConstant.AdsType.BIG_BANNER_LANGUAGE_LEARNING;
            }
            AppCommon.getInstance().syncAdsIfNeeded(MainActivity.ADS_TYPE_SMALL, MainActivity.ADS_TYPE_BIG);
        }

    }

    @Override
    protected void onFinishInitAppCommon() {
        enableOnBackPressed();
        animSplashLogo();
//        setProVersion(true);
        replaceParentFragment();
        requestPermission();

        adsBigBannerHandler = new AdsBigBannerHandler(this, MainActivity.ADS_TYPE_BIG);
        adsBigBannerHandler.setup();
    }

    @Override
    protected MyBaseFragment getFragmentContent() {
        return new SplashFragment();
    }

    private void replaceParentFragment() {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
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
        } else if (getCurrentFragment() instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) getCurrentFragment();
            if (homeFragment.actionsMenu.isExpanded()) homeFragment.actionsMenu.collapse();
            else if (!getStatusLoading()) closeAppHandler.setKeyBackPress(this);
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

    public void showUpgradeProVersionDialog() {
        MyDialog.getInstance(this).show();
    }

    public void requestUpgradeToProVersion() {
        if (billingProcessor != null && billingProcessor.isInitialized()) {
            if (!billingProcessor.isPurchased(PURCHASE_PRO_VERSION_ID)) {
                billingProcessor.purchase(this, PURCHASE_PRO_VERSION_ID);
            }
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (PURCHASE_PRO_VERSION_ID.equals(productId)) {
            setProVersion(true);
            getCurrentFragment().onAppUpgraded();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        setProVersion(billingProcessor.isPurchased(PURCHASE_PRO_VERSION_ID));
        if (isProVersion()) {
            getCurrentFragment().onAppUpgraded();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingProcessor != null && !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }

        super.onDestroy();
    }

    public void showBigAdsIfNeeded() {
        if (!isProVersion()) {
            adsCounter++;
            if (adsCounter % BIG_ADS_SHOWING_INTERVAL == 0) {
                adsBigBannerHandler.show();
            }
        }
    }

    public void showRateAppIfNeeded() {
        if (!AppLocalSharedPreferences.getInstance().isRatedApp() && !isProVersion()) {
            if (rateAppCounter % RATE_APP_INTERVAL == 0) {
                if (appRate == null) {
                    appRate = AppRate.with(this)
                            .setInstallDays(0) // default 10, 0 means install day.
                            .setLaunchTimes(0) // default 10
                            .setRemindInterval(0) // default 1
                            .setShowLaterButton(false)
                            .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                                @Override
                                public void onClickButton(int which) {
                                    if (which == -1) {//rate
                                        AppLocalSharedPreferences.getInstance().setIsRateApp(true);
                                    } else {//close
                                        appRate.clearAgreeShowDialog();
                                        AppLocalSharedPreferences.getInstance().setRateAppRemindInterval();
                                    }
                                }
                            });
                }
                appRate.showRateDialog(this);
            }
            rateAppCounter++;
        }
    }
}
