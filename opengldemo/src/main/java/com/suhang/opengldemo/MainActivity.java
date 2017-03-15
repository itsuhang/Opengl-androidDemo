package com.suhang.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.suhang.opengldemo.adapter.MainFragmentAdapter;
import com.suhang.opengldemo.fragment.GLFragmentFive;
import com.suhang.opengldemo.fragment.GLFragmentFour;
import com.suhang.opengldemo.fragment.GLFragmentOne;
import com.suhang.opengldemo.fragment.GLFragmentSeven;
import com.suhang.opengldemo.fragment.GLFragmentSix;
import com.suhang.opengldemo.fragment.GLFragmentThree;
import com.suhang.opengldemo.fragment.GLFragmentTwo;
import com.suhang.opengldemo.utils.LogUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private ViewPager mVp;
	private TabLayout mTab;
	private ArrayList<Fragment> fragments = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTab = (TabLayout) findViewById(R.id.tab);
		mVp = (ViewPager) findViewById(R.id.vp);
		initData();
		init();
		ActivityManager am =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		LogUtil.i("啊啊啊"+info.getGlEsVersion());

	}

	private void initData() {
		fragments.add(new GLFragmentOne());
		fragments.add(new GLFragmentTwo());
		fragments.add(new GLFragmentThree());
		fragments.add(new GLFragmentFour());
		fragments.add(new GLFragmentFive());
		fragments.add(new GLFragmentSix());
		fragments.add(new GLFragmentSeven());
	}

	private void init() {
		MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments);
		mVp.setAdapter(adapter);
		mTab.setupWithViewPager(mVp);
	}
}
