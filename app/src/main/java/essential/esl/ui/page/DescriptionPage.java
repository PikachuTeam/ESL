package essential.esl.ui.page;

import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.CustomScrollView;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyDialog;
import essential.esl.data.Conversation;
import essential.esl.ui.activity.MainActivity;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;

/**
 * Created by admin on 5/26/2016.
 */
public class DescriptionPage extends BasePage {
    private TextView tvDescription, tvHeader, tvHeaderFake, tvUpgrade;
    private boolean isTranscription = false;
    private ImageView imageViewBlur;
    private RelativeLayout tvTrick;
    private Conversation conversation;
    private AdsSmallBannerHandler adsHandler;

    public DescriptionPage(final MyBaseFragment fragment, final MainActivity activity, Conversation conversation, String stringContent, String header, boolean isTranscription) {
        super(fragment, activity);
        tvDescription = (TextView) getContent().findViewById(R.id.tv_description);
        tvHeader = (TextView) getContent().findViewById(R.id.tv_header);
        tvHeaderFake = (TextView) getContent().findViewById(R.id.trick);
        tvTrick = (RelativeLayout) getContent().findViewById(R.id.tv_trick);
        imageViewBlur = (ImageView) getContent().findViewById(R.id.iv);
        tvUpgrade = (TextView) getContent().findViewById(R.id.tv_upgrade);
        tvHeader.setText(header);
        this.conversation = conversation;
        tvDescription.setText(Html.fromHtml(stringContent.trim()));
        this.isTranscription = isTranscription;
        if (activity.isProVersion() == false) {

            if (isTranscription == true) {
                if (conversation.isFree > 0) {
                    imageViewBlur.setVisibility(View.GONE);
                    tvTrick.setVisibility(View.GONE);
                } else {
                    tvTrick.setVisibility(View.VISIBLE);
                    imageViewBlur.setVisibility(View.VISIBLE);
                    tvHeaderFake.setText(header);
                    tvDescription.setVisibility(View.GONE);
                    tvUpgrade.setVisibility(View.VISIBLE);
                    tvUpgrade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            (activity).showUpgradeProVersionDialog();
                        }
                    });
                }
            } else {
                imageViewBlur.setVisibility(View.GONE);
                tvTrick.setVisibility(View.GONE);
                tvUpgrade.setVisibility(View.GONE);
            }
        } else {
            imageViewBlur.setVisibility(View.GONE);
            tvTrick.setVisibility(View.GONE);
        }

        if (!activity.isProVersion()) {
            adsHandler = new AdsSmallBannerHandler(activity, (ViewGroup) getContent().findViewById(R.id.ads_container), MainActivity.ADS_TYPE_SMALL, AdSize.MEDIUM_RECTANGLE);
            adsHandler.setup();
        }
    }


    @Override
    protected int getContentId() {
        return R.layout.page_description;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (adsHandler != null) {
            adsHandler.destroy();
        }
    }
}
