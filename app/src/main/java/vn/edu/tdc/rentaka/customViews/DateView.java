package vn.edu.tdc.rentaka.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.edu.tdc.rentaka.R;

@SuppressLint("AppCompatCustomView")
public class DateView extends TextView {
    private String delimiter;
    private boolean fancyText;

    public DateView(Context context) {
        super(context);
        setDate();
    }
    public DateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateView);
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DateView_delimiter) {
                delimiter = a.getString(attr);
                setDate();
            }
            else if (attr == R.styleable.DateView_fancyText) {
                fancyText = a.getBoolean(attr, false);
                fancyText();
            }
        }
        a.recycle();
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDate();
    }
    // Private functions
    private void setDate() {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd");
        String today = dateFormat.format(Calendar.getInstance().getTime());
        setText(today);  // self = DateView = subclass of TextView
    }
    // Function set text style
    private void fancyText() {
    }
}
