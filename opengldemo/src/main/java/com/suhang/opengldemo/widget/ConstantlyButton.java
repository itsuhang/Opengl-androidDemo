package com.suhang.opengldemo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by 苏杭 on 2017/3/10 15:48.
 */

public class ConstantlyButton extends AppCompatButton{

    private Disposable mSubscribe;

    public ConstantlyButton(Context context) {
        super(context);
    }

    public ConstantlyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static final int DELAY = 50;
    private void startClickTask() {
        mSubscribe = Flowable.interval(0, DELAY, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                if(mListener!=null)
                mListener.onConstantlyClick(ConstantlyButton.this);
            }
        });
    }

    private void stopClickTask() {
        if (mSubscribe != null) {
            mSubscribe.dispose();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTask();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopClickTask();
                break;
        }
        return true;
    }

    private OnConstantlyClickListener mListener;

    public void setOnConstantlyClickListener(OnConstantlyClickListener listener) {
        mListener = listener;
    }

    public interface OnConstantlyClickListener {
        void onConstantlyClick(View view);
    }

}
