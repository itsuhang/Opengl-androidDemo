package com.suhang.opengldemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.render.OpenGlRenderFive;
import com.suhang.opengldemo.render.OpenGlRenderSix;
import com.suhang.opengldemo.widget.ConstantButton;
import com.suhang.opengldemo.widget.MyGLSurfaceView;

/**
 * Created by 苏杭 on 2017/3/6 14:11.
 */

public class GLFragmentSix extends Fragment {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gl, container, false);
		MyGLSurfaceView glsv = (MyGLSurfaceView) view.findViewById(R.id.glsv);
		glsv.setRenderer(new OpenGlRenderSix(getContext()));
		ConstantButton left = (ConstantButton) view.findViewById(R.id.left);
		ConstantButton left_rotate = (ConstantButton) view.findViewById(R.id.up);
        ConstantButton right = (ConstantButton) view.findViewById(R.id.right);
		ConstantButton right_rotate = (ConstantButton) view.findViewById(R.id.down);
		left.setVisibility(View.VISIBLE);
		right.setVisibility(View.VISIBLE);
		left_rotate.setVisibility(View.VISIBLE);
		right_rotate.setVisibility(View.VISIBLE);
		left.setOnConstantClickListener(glsv);
		right.setOnConstantClickListener(glsv);
		left_rotate.setOnConstantClickListener(glsv);
		right_rotate.setOnConstantClickListener(glsv);
		return view;
	}
}
