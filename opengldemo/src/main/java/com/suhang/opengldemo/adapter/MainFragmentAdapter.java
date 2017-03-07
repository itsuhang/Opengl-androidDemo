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

	public MainFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "PAGE_"+position;
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
