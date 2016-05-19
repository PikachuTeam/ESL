package essential.esl.ui.parentfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import essential.esl.R;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseFragment;
import essential.esl.ui.MainActivity;

/**
 * Created by admin on 5/18/2016.
 */
public class HomeFragment extends MyBaseFragment implements View.OnClickListener {
    private LinearLayout btn1, btn2, btn3;

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
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        MyAnimation.animZoomWhenOnClick(v, HomeFragment.this, 1, 1.1f, 1, 1.1f);
        int id = v.getId();
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
