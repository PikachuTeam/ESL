package essential.esl.ui.parentfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import essential.esl.R;
import essential.esl.app.BaseParentFragment;
import essential.esl.ui.childfragment.HomeFragment;

/**
 * Created by admin on 5/18/2016.
 */
public class MainFragment extends BaseParentFragment {
    @Override
    protected int getIdChildFragmentContent() {
        return R.id.child_fragment_content;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        transaction.add(getIdChildFragmentContent(), fragment, fragment.getClass().getName());
        transaction.commit();
    }


}
