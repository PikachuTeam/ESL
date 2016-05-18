package essential.esl.app;


import essential.esl.base_v4.BaseFragment_v4;

/**
 * Created by admin on 5/13/2016.
 */
public abstract class BaseChildFragment extends BaseFragment_v4 {

    public void replaceChildFragment(BaseChildFragment fragment, String fragmentTag, String transactionTag) {
        BaseParentFragment parentFragment = (BaseParentFragment) getParentFragment();
        parentFragment.replaceChildFragment(fragment, fragmentTag, transactionTag);
    }
}
