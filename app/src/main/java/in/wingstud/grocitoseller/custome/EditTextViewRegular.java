package in.wingstud.grocitoseller.custome;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import in.wingstud.grocitoseller.AppInitialization;
import in.wingstud.grocitoseller.R;


/**
 * Created by Android1 on 1/9/2016.
 */

public class EditTextViewRegular extends AppCompatEditText {


    public EditTextViewRegular(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.setTypeface(AppInitialization.getFontRegular());
//        this.setBackground(context.getResources().getDrawable(R.drawable.et_bg));
//        this.setPadding((int)context.getResources().getDimension(R.dimen.dp_10),0,(int)context.getResources().getDimension(R.dimen.dp_10),0);
        this.setTextColor(getResources().getColor(R.color.colorPrimary));
        this.setHintTextColor(getResources().getColor(R.color.gray));
    }


    public EditTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}