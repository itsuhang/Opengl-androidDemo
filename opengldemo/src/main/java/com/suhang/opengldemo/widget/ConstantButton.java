package com.suhang.opengldemo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import io.reactivex.disposables.Disposable;

/**
 * Created by 苏杭 on 2017/3/10 15:48.
 */

public class ConstantButton extends AppCompatButton{

    private Disposable mSubscribe;

    public ConstantButton(Context context) {
        super(context);
    }

    public ConstantButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mListener!=null)
                    mListener.onConstantClick(true,ConstantButton.this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mListener!=null)
                    mListener.onConstantClick(false,ConstantButton.this);
                break;
        }
        return true;
    }

    private OnConstantClickListener mListener;

    public void setOnConstantClickListener(OnConstantClickListener listener) {
        mListener = listener;
    }

    public interface OnConstantClickListener {
        void onConstantClick(boolean isPress,View v);
    }

}
