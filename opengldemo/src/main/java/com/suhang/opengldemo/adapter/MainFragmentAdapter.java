package com.suhang.opengldemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by 苏杭 on 2017/3/6 14:26.
 */

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> mFragments;
	private String[] titles = {"矩形","着色","纹理","变换","摄像机","光照","材质","光照贴图","定向光","点光源"};

	public MainFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}
}
