package essential.esl.app;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * Created by admin on 5/12/2016.
 */
public abstract class MyBaseLActivity extends MyBaseSplashActivity {


    protected abstract MyBaseFragment getFragmentContent();


    public void addFragmentContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MyBaseFragment fragment = getFragmentContent();
            transaction.add(getParentFragmentContainerId(), fragment, fragment.getClass().getName());
            transaction.commit();
        } else {
            popToFirstFragment();
        }
    }

    public void makeMessage(View view, int idString) {
        Snackbar.make(view, idString, Snackbar.LENGTH_LONG).show();
    }

    public MyBaseFragment getCurrentParentFragment() {
        if (getStatusLoading()) return null;
        else
            return (MyBaseFragment) getSupportFragmentManager().findFragmentById(getParentFragmentContainerId());
    }
    public MyBaseFragment getCurrentFragment() {
        MyBaseFragment currentFragment = (MyBaseFragment) getSupportFragmentManager().findFragmentById(getParentFragmentContainerId());
        return currentFragment;
    }
    public void popToFirstFragment() {
        getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
