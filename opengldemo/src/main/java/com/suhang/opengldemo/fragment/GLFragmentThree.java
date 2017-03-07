package com.suhang.opengldemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.render.OpenGlRenderOne;
import com.suhang.opengldemo.render.OpenGlRenderThree;
import com.suhang.opengldemo.widget.MyGLSurfaceView;

/**
 * Created by 苏杭 on 2017/3/6 14:11.
 */

public class GLFragmentThree extends Fragment {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gl, container, false);
		MyGLSurfaceView glsv = (MyGLSurfaceView) view.findViewById(R.id.glsv);
		glsv.setRenderer(new OpenGlRenderThree(getContext()));
		return view;
	}
}
