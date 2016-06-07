package essential.esl.ui.fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import essential.esl.R;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyDialog;
import essential.esl.app.ShareUtil;
import essential.esl.ui.activity.MainActivity;
import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.ui.activity.BaseActivity;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by admin on 5/18/2016.
 */
public class HomeFragment extends MyBaseFragment implements View.OnClickListener {
    private LinearLayout btn1, btn2, btn3, btn4;
    private CardView btnUpgrade;
    public FloatingActionsMenu actionsMenu;
    protected FloatingActionButton btnShare, btnMoreApp, btnFeedback, btnFavorite;
    private RelativeLayout fabListen;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        init(rootView);
    }

    public void init(View rootView) {
        btn1 = (LinearLayout) rootView.findViewById(R.id.btn_lv1);
        btn2 = (LinearLayout) rootView.findViewById(R.id.btn_lv2);
        btn3 = (LinearLayout) rootView.findViewById(R.id.btn_lv3);
        btn4 = (LinearLayout) rootView.findViewById(R.id.btn_link_app);
        btnUpgrade = (CardView) rootView.findViewById(R.id.btn_Upgrade);
        btnFavorite = (FloatingActionButton) rootView.findViewById(R.id.btn_favorite);
        btnFeedback = (FloatingActionButton) rootView.findViewById(R.id.btn_feedback);
        btnMoreApp = (FloatingActionButton) rootView.findViewById(R.id.btn_more_app);
        btnShare = (FloatingActionButton) rootView.findViewById(R.id.btn_share);
        fabListen = (RelativeLayout) rootView.findViewById(R.id.listen_fab_menu);
        setFabMenu(rootView);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btnUpgrade.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
        btnMoreApp.setOnClickListener(this);
    }

    @Override
    public void onBackPress() {
        if (fabListen.getVisibility() == View.VISIBLE) {
            actionsMenu.collapse();
        } else
            super.onBackPress();
    }

    public void setFabMenu(View rootView) {
        actionsMenu = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);

        actionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fabListen.setVisibility(View.VISIBLE);
                fabListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionsMenu.collapse();
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                fabListen.setVisibility(View.INVISIBLE);
                fabListen.setOnClickListener(null);
            }
        });


    }

    private void sharingEvent() {
        String androidLink = "https://play.google.com/store/apps/details?id=" + getBaseActivity().getPackageName();
        String sharedText = getString(R.string.app_name) + ".\nAndroid: " + androidLink;
        CommonUtil.sharePlainText(getContext(), sharedText);
    }

    @Override
    public void onClick(View v) {
        MyAnimation.animZoomWhenOnClick(v, HomeFragment.this, 1, 1.1f, 1, 1.1f);
        int id = v.getId();
        if (id == R.id.btn_Upgrade || id == R.id.btn_favorite || id == R.id.btn_share || id == R.id.btn_feedback || id == R.id.btn_more_app || id == R.id.btn_link_app) {
            switch (id) {
                case R.id.btn_Upgrade:
                    MyDialog.getInstance(getContext()).show();
                    break;
                case R.id.btn_favorite:
                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    replaceFragment(favoriteFragment, favoriteFragment.toString());
                    MainActivity activity = (MainActivity) getActivity();
                    activity.animHideLogo();
                    break;
                case R.id.btn_share:
                    sharingEvent();
                    break;
                case R.id.btn_feedback:
                    ShareUtil.shareToGMail(getContext(), new String[]{ShareUtil.MAIL_ADDRESS_DEFAULT}, getString(R.string.subject_mail_feedback), "");
                    break;
                case R.id.btn_more_app:
                    AppCommon.getInstance().openMoreAppDialog(getContext());
                    break;
                case R.id.btn_link_app:
                    //
                    String packageNameAccent = "com.tatteam.android.englishaccenttraining";
                    if (CommonUtil.isPackageInstalled(packageNameAccent, getContext())) {
                        Intent intent = getBaseActivity().getPackageManager().getLaunchIntentForPackage(packageNameAccent);
                        startActivity(intent);
                    } else {
                        CommonUtil.openApplicationOnGooglePlay(getContext(), packageNameAccent);
                    }
                    break;
            }
            if (actionsMenu.isExpanded()) actionsMenu.collapse();
        } else {
            int idCat = 0;
            switch (id) {
                case R.id.btn_lv1:
                    idCat = 1;
                    break;
                case R.id.btn_lv2:
                    idCat = 2;
                    break;
                case R.id.btn_lv3:
                    idCat = 3;
                    break;
            }
            v.setOnClickListener(null);
            ConverStationsFragment converStationsFragment = new ConverStationsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("idCat", idCat);
            converStationsFragment.setArguments(bundle);
            replaceFragment(converStationsFragment, converStationsFragment.toString());
            MainActivity activity = (MainActivity) getActivity();
            activity.animHideLogo();
        }
    }

}
