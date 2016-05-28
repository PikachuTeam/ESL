package essential.esl.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import essential.esl.R;

/**
 * Created by admin on 5/27/2016.
 */
public class MyItemLayout extends LinearLayout {
    private TextView textView;
    private ImageView imageView;


    public MyItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Options, 0, 0);
        a.recycle();
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_item_layout, this, true);
        imageView = (ImageView) getChildAt(0);
        textView = (TextView) getChildAt(1);
    }

    public void setText(String string) {
        textView.setText(string);
    }

    public void setImage(int id) {
        imageView.setImageResource(id);
    }

    public void setTextColor(int id) {
        textView.setTextColor(id);
    }

    public void setImageColorFilter(int id) {
        imageView.setColorFilter(id);
    }
}
