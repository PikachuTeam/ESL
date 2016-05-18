package essential.esl.ui.childfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import essential.esl.R;
import essential.esl.app.BaseChildFragment;
import essential.esl.app.MyAnimation;

/**
 * Created by admin on 5/18/2016.
 */
public class HomeFragment extends BaseChildFragment implements View.OnClickListener {
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
        switch (id) {
            case R.id.btn_lv1:
                break;
            case R.id.btn_lv2:
                break;
            case R.id.btn_lv3:
                break;
        }
    }
}
