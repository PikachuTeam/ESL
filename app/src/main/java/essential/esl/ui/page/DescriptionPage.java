package essential.esl.ui.page;

import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.CustomScrollView;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyDialog;
import essential.esl.ui.activity.MainActivity;

/**
 * Created by admin on 5/26/2016.
 */
public class DescriptionPage extends BasePage {
    private TextView tvDescription, tvHeader, tvHeaderFake, tvUpgrade;
    private boolean isTranscription = false;
    private ImageView imageViewBlur;
    private RelativeLayout tvTrick;

    public DescriptionPage(final MyBaseFragment fragment, MyBaseActivity activity, String stringContent, String header, boolean isTranscription) {
        super(fragment, activity);
        tvDescription = (TextView) getContent().findViewById(R.id.tv_description);
        tvHeader = (TextView) getContent().findViewById(R.id.tv_header);
        tvHeaderFake = (TextView) getContent().findViewById(R.id.trick);
        tvTrick = (RelativeLayout) getContent().findViewById(R.id.tv_trick);
        imageViewBlur = (ImageView) getContent().findViewById(R.id.iv);
        tvUpgrade = (TextView) getContent().findViewById(R.id.tv_upgrade);
        tvHeader.setText(header);
        tvDescription.setText(Html.fromHtml(stringContent.trim()));
        this.isTranscription = isTranscription;
        if (MainActivity.isProVersion() == false) {
            if (isTranscription == true) {
                tvTrick.setVisibility(View.VISIBLE);
                imageViewBlur.setVisibility(View.VISIBLE);
                tvHeaderFake.setText(header);
                tvDescription.setVisibility(View.GONE);
                tvUpgrade.setVisibility(View.VISIBLE);
                tvUpgrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.getInstance(fragment.getContext()).show();
                    }
                });

            } else {
                imageViewBlur.setVisibility(View.GONE);
                tvTrick.setVisibility(View.GONE);
                tvUpgrade.setVisibility(View.GONE);


            }
        } else {
            imageViewBlur.setVisibility(View.GONE);
            tvTrick.setVisibility(View.GONE);
        }
    }


    @Override
    protected int getContentId() {
        return R.layout.page_description;
    }
}
