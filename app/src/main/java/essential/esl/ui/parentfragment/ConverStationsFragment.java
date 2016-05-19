package essential.esl.ui.parentfragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import essential.esl.R;
import essential.esl.app.MyBaseFragment;
import essential.esl.ui.MainActivity;

/**
 * Created by admin on 5/18/2016.
 */
public class ConverStationsFragment extends MyBaseFragment implements View.OnClickListener {
    private LinearLayout btnBack, btnShare;
    private TextView tvTitle;
    private int idCat;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        idCat = getArguments().getInt("idCat");
        init(rootView);
        setTitle();
    }

    public void init(View rootView) {
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_Back);
        btnShare = (LinearLayout) rootView.findViewById(R.id.btn_Share);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    public void setTitle() {
        switch (idCat) {
            case 1:
                tvTitle.setText(getResources().getString(R.string.basic));
                break;
            case 2:
                tvTitle.setText(getResources().getString(R.string.general));
                break;
            case 3:
                tvTitle.setText(getResources().getString(R.string.academy));
                break;

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_Back:
                MainActivity activity = (MainActivity) getActivity();
                activity.onBackPressed();
                break;
            case R.id.btn_Share:
                break;
        }
    }


}
