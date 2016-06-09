package essential.esl.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;
import essential.esl.ui.page.LevelPage;
import essential.esl.ui.activity.MainActivity;

/**
 * Created by admin on 5/18/2016.
 */
public class ConverStationsFragment extends MyBaseFragment implements View.OnClickListener {
    private LinearLayout btnBack, btnShare;
    private TextView tvTitle;
    private int idCat;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_conversations;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCat = getArguments().getInt("idCat");
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {

        if (adapter == null) {
            adapter = new MyViewPagerAdapter((MainActivity) getActivity(), this);
        } else
            adapter.updateData();
        init(rootView);
    }

    public void init(View rootView) {
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_Back);
        btnShare = (LinearLayout) rootView.findViewById(R.id.btn_Share);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        btnBack.setOnClickListener(this);
        btnShare.setVisibility(View.INVISIBLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setTitle();
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
    public void onAppUpgraded() {
        super.onAppUpgraded();
    }

    @Override
    public void onClick(View v) {
        MyAnimation.animZoomWhenOnClick(v, this, 1, 1.3f, 1, 1.3f);
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


    public class MyViewPagerAdapter extends PagerAdapter {
        private MyBaseFragment fragment;
        private MainActivity activity;
        private ArrayList<LevelPage> pages;

        public MyViewPagerAdapter(MainActivity activity, MyBaseFragment fragment) {
            this.activity = activity;
            this.fragment = fragment;
            if (pages == null) {
                pages = new ArrayList<>();
                switch (idCat) {
                    case 1:
                        for (int i = 1; i <= 3; i++) {
                            LevelPage page = new LevelPage(fragment, activity, idCat, i);
                            pages.add(page);
                        }
                        break;
                    case 2:
                        for (int i = 1; i <= 2; i++) {
                            LevelPage page = new LevelPage(fragment, activity, idCat, i);
                            pages.add(page);
                        }
                        break;
                    case 3:
                        for (int i = 2; i <= 4; i++) {
                            LevelPage page = new LevelPage(fragment, activity, idCat, i);
                            pages.add(page);
                        }
                        break;
                }
            }
        }

        public void updateData() {
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).updateData();
            }

        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = null;
            layout = pages.get(position).getContent();
            container.addView(layout);
            return layout;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages.get(position).getTitle();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            pages.get(position).destroy();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
