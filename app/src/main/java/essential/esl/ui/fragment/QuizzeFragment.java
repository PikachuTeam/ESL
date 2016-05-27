package essential.esl.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;
import essential.esl.ui.activity.MainActivity;
import essential.esl.ui.page.DescriptionPage;
import essential.esl.ui.page.LevelPage;
import essential.esl.ui.page.QuizzesPage;
import essential.esl.widget.DotDotDot;
import essential.esl.widget.HelpfulTip;
import tatteam.com.app_common.ui.activity.BaseActivity;

/**
 * Created by admin on 5/25/2016.
 */
public class QuizzeFragment extends MyBaseFragment implements View.OnClickListener {
    private LinearLayout btnBack, btnShare;
    private TextView tvTitle;
    private ViewPager viewPager;
    public HelpfulTip helpfulTip;
    public Conversation conversation;
    public MyViewPagerAdapter adapter;
    private ArrayList<ImageView> dots;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_quizze;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        this.conversation = (Conversation) getArguments().getSerializable("converstation");
        helpfulTip = new HelpfulTip(this, rootView, conversation);
        init(rootView);
        setupViewPager();
        selectPage(0);

    }

    private void setupViewPager() {
        adapter = new MyViewPagerAdapter((MyBaseActivity) getActivity(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void selectPage(int position) {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setColorFilter(getResources().getColor(R.color.secondaryTextColor));
        }
        dots.get(position).setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void init(View rootView) {
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_Back);
        btnShare = (LinearLayout) rootView.findViewById(R.id.btn_Share);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        dots = new ArrayList<>();
        dots.add((ImageView) rootView.findViewById(R.id.dot1));
        dots.add((ImageView) rootView.findViewById(R.id.dot2));
        dots.add((ImageView) rootView.findViewById(R.id.dot3));
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(conversation.title);
    }

    @Override
    public void onBackPress() {
        if (helpfulTip.isTipsShowing()) helpfulTip.hideTips();
        else
            super.onBackPress();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MyAnimation.animZoomWhenOnClick(v, this, 1, 1.3f, 1, 1.3f);
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
        private ArrayList<BasePage> pages;

        public MyViewPagerAdapter(MyBaseActivity activity, MyBaseFragment fragment) {
            pages = new ArrayList<>();
            QuizzesPage quizzesPage = new QuizzesPage(fragment, activity, conversation.id);
            DescriptionPage scriptPage = new DescriptionPage(fragment, activity, conversation.script);
            DescriptionPage keyVocabularyPage = new DescriptionPage(fragment, activity, conversation.keyVocabulary);
            pages.add(quizzesPage);
            pages.add(scriptPage);
            pages.add(keyVocabularyPage);

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
