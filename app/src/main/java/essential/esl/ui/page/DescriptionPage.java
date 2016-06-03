package essential.esl.ui.page;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.ui.activity.MainActivity;
import essential.esl.ui.fragment.QuizzeFragment;

/**
 * Created by admin on 5/26/2016.
 */
public class DescriptionPage extends BasePage {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;
    private TextView tvDescription, tvHeader;

    public DescriptionPage(MyBaseFragment fragment, MyBaseActivity activity, String stringContent, String header) {
        super(fragment, activity);
        tvDescription = (TextView) getContent().findViewById(R.id.tv_description);
        tvHeader = (TextView) getContent().findViewById(R.id.tv_header);
        tvHeader.setText(header);
        tvDescription.setText(Html.fromHtml(stringContent.trim()));


    }


    @Override
    protected int getContentId() {
        return R.layout.page_description;
    }
}
