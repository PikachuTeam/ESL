package essential.esl.app;

import android.support.v4.app.FragmentTransaction;

import essential.esl.base_v4.BaseFragment_v4;

/**
 * Created by admin on 5/13/2016.
 */
public abstract class BaseParentFragment extends BaseFragment_v4 {
    protected abstract int getIdChildFragmentContent();

    public void replaceChildFragment(BaseChildFragment fragment, String fragmentTag, String transactionTag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(tatteam.com.app_common.R.anim.fragment_slide_right_enter, tatteam.com.app_common.R.anim.fragment_slide_left_exit,
                tatteam.com.app_common.R.anim.fragment_slide_left_enter, tatteam.com.app_common.R.anim.fragment_slide_right_exit);
        transaction.replace(getIdChildFragmentContent(), fragment, fragmentTag).addToBackStack(transactionTag);
        transaction.commit();
    }
}
