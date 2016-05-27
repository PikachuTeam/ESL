package essential.esl.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThanhNH on 9/11/2015.
 */
public abstract class BasePage {
    public MyBaseFragment fragment;
    public MyBaseActivity activity;
    protected View content;

    public BasePage(MyBaseFragment fragment, MyBaseActivity activity) {
        this(activity, fragment, null);
    }


    public BasePage(MyBaseActivity activity, MyBaseFragment fragment, ViewGroup parent) {
        this.fragment = fragment;

        this.activity = activity;
        if (parent != null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            content = inflater.inflate(getContentId(), parent, false);
        } else {
            content = View.inflate(activity, getContentId(), null);
        }

    }

    protected abstract int getContentId();

    public View getContent() {
        return content;
    }

    public void destroy() {
    }

}
