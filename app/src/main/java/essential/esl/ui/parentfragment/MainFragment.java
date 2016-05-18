package essential.esl.ui.parentfragment;

import android.os.Bundle;
import android.view.View;

import essential.esl.R;
import essential.esl.app.BaseParentFragment;

/**
 * Created by admin on 5/18/2016.
 */
public class MainFragment extends BaseParentFragment {
    @Override
    protected int getIdChildFragmentContent() {
        return 0;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {

    }
}
