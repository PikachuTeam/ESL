package essential.esl.widget;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.MyBaseFragment;

/**
 * Created by admin on 5/25/2016.
 */
public class DotDotDot {
    private ArrayList<ImageView> dots;
    private ImageView dot1, dot2, dot3;
    private MyBaseFragment fragment;
    private View parentView;

    public DotDotDot(MyBaseFragment fragment, View parentView) {
        this.fragment = fragment;
        this.parentView = parentView;
        init();
        selectPage(0);
    }

    private void init() {
        dot1 = (ImageView) parentView.findViewById(R.id.dot1);
        dot2 = (ImageView) parentView.findViewById(R.id.dot2);
        dot3 = (ImageView) parentView.findViewById(R.id.dot3);
        dots = new ArrayList<>();
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);


    }

    public void setUpWithViewPager(ViewPager viewPager) {
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
            dots.get(i).setColorFilter(fragment.getResources().getColor(R.color.secondaryTextColor));
        }
        dots.get(position).setColorFilter(fragment.getResources().getColor(R.color.colorPrimaryDark));
    }
}
