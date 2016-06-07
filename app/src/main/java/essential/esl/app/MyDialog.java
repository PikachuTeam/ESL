package essential.esl.app;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import essential.esl.R;

/**
 * Created by admin on 6/8/2016.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private TextView btnNo, btnYes;
    private static MyDialog instance;

    private MyDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);
        init();
    }

    public static MyDialog getInstance(Context context) {
        if (instance == null) {
            instance = new MyDialog(context);
        }
        return instance;
    }

    private void init() {
        btnNo = (TextView) findViewById(R.id.btn_no);
        btnYes = (TextView) findViewById(R.id.btn_yes);
        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);

    }

    private void doSomething() {

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
                doSomething();
                break;
        }
    }
}
