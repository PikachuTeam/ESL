package essential.esl.app;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import essential.esl.R;
import essential.esl.ui.activity.MainActivity;

/**
 * Created by admin on 6/8/2016.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private TextView btnNo, btnYes;
    private static MyDialog instance;
    private MainActivity activity;

    private MyDialog(MainActivity activity) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        setContentView(R.layout.my_dialog);
        init();
    }

    public static MyDialog getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new MyDialog(activity);
        }
        return instance;
    }

    private void init() {
        btnNo = (TextView) findViewById(R.id.btn_no);
        btnYes = (TextView) findViewById(R.id.btn_yes);
        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);

    }

    private void requestUpgradeToProVersion() {
        activity.requestUpgradeToProVersion();
    }

    @Override
    public void onClick(View v) {
        MyAnimation.animZoomWhenOnClick(v, MyDialog.this, 1, 1.3f, 1, 1.3f);
        int id = v.getId();
        switch (id) {
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.btn_yes:
                requestUpgradeToProVersion();
                break;
        }
    }
}
