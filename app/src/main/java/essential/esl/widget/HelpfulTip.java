package essential.esl.widget;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import essential.esl.R;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;

/**
 * Created by admin on 5/25/2016.
 */
public class HelpfulTip implements View.OnClickListener {
    private ImageView btnTips, ivAvatar;
    private TextView tvLevel, tvTopic, tvType, tvSpeakers, tvTips;
    private RelativeLayout background;
    private CardView content;
    private MyBaseFragment fragment;
    private String SPACE = "                           ";
    private Conversation conversation;
    private String level;
    private View parentView;

    public HelpfulTip(MyBaseFragment fragment, View parentView, Conversation conversation) {
        this.fragment = fragment;
        this.parentView = parentView;
        this.conversation = conversation;
        init();
    }

    public boolean isTipsShowing() {
        if (background.getVisibility() == View.GONE) return false;
        else return true;
    }

    public void hideTips() {
        background.setVisibility(View.GONE);
    }

    private void init() {
        tvLevel = (TextView) parentView.findViewById(R.id.level);
        tvTopic = (TextView) parentView.findViewById(R.id.topic);
        tvType = (TextView) parentView.findViewById(R.id.type);
        tvSpeakers = (TextView) parentView.findViewById(R.id.speaker);
        tvTips = (TextView) parentView.findViewById(R.id.tips);
        ivAvatar = (ImageView) parentView.findViewById(R.id.imageAvatar);
        btnTips = (ImageView) parentView.findViewById(R.id.btn_Tips);
        background = (RelativeLayout) parentView.findViewById(R.id.background_tips);
        content = (CardView) parentView.findViewById(R.id.tips_content);

        background.setOnClickListener(this);
        content.setOnClickListener(this);
        btnTips.setOnClickListener(this);

        tvLevel.setText(getLevel());
        tvType.setText(conversation.type);
        tvTips.setText(SPACE + conversation.helpFulTip);
        tvTopic.setText(conversation.topic);
        tvSpeakers.setText(conversation.speakers);
        Glide.with(fragment)
                .load(conversation.avatarImageUrl).centerCrop().crossFade().error(R.drawable.err)
                .into(ivAvatar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_Tips) {
            MyAnimation.animZoomWhenOnClick(v, this, 1, 1.1f, 1, 1.1f);
            background.setVisibility(View.VISIBLE);
        } else if (id == R.id.background_tips) background.setVisibility(View.GONE);
    }

    public String getLevel() {
        String level = "";
        switch (conversation.level) {
            case 1:
                level = "easy";
                break;
            case 2:
                level = "medium";
                break;
            case 3:
                level = "difficult";
                break;
            case 4:
                level = "extreme difficult";
                break;
        }
        return level;
    }
}
