package com.jameschin.android.firstglobalcapital;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabListener<T extends Fragment> implements com.actionbarsherlock.app.ActionBar.TabListener {
	private final SherlockFragmentActivity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	private Fragment mFragment;

	public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment preInitializedFragment = (Fragment) mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		if (preInitializedFragment == null) {
			mFragment = (Fragment) Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			ft.attach(preInitializedFragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Fragment preInitializedFragment = (Fragment) mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		if (preInitializedFragment != null) {
			ft.detach(preInitializedFragment);
		} else if (mFragment != null) {
			ft.detach(mFragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}