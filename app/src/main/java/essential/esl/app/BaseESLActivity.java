package essential.esl.app;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import essential.esl.base_v4.BaseFragment_v4;
import essential.esl.base_v4.BaseSplashActivity_v4;

/**
 * Created by admin on 5/12/2016.
 */
public abstract class BaseESLActivity extends BaseSplashActivity_v4 {
    protected abstract int getParentFragmentContainerId();

    protected abstract BaseFragment_v4 getFragmentContent();


    public void addFragmentContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BaseFragment_v4 fragment = getFragmentContent();
            transaction.add(getParentFragmentContainerId(), fragment, fragment.getClass().getName());
            transaction.commit();
        } else {
            popToFirstFragment();
        }
    }

    public void makeMessage(View view, int idString) {
        Snackbar.make(view, idString, Snackbar.LENGTH_LONG).show();
    }

    public BaseParentFragment getCurrentParentFragment() {
        if (getStatusLoading()) return null;
        else
            return (BaseParentFragment) getSupportFragmentManager().findFragmentById(getParentFragmentContainerId());
    }

    public void popToFirstFragment() {
        getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
