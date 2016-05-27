package essential.esl.ui.page;

import android.text.Html;
import android.widget.TextView;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;

/**
 * Created by admin on 5/26/2016.
 */
public class DescriptionPage extends BasePage {
    private TextView tvDescription;

    public DescriptionPage(MyBaseFragment fragment, MyBaseActivity activity, String stringContent) {
        super(fragment, activity);
        tvDescription = (TextView) getContent().findViewById(R.id.tv_description);
        tvDescription.setText(Html.fromHtml(stringContent));
    }

    @Override
    protected int getContentId() {
        return R.layout.page_description;
    }
}
