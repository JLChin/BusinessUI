package com.jameschin.android.firstglobalcapital;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
	// store the active tab here
	public static String ACTIVE_TAB = "activeTab";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// add tabs
		Tab tab1 = actionBar
				.newTab()
				.setText(R.string.tab_home)
				.setTabListener(new TabListener<HomeFragment>(this, "tab1", HomeFragment.class));
		actionBar.addTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText(R.string.tab_offers)
				.setTabListener(new TabListener<OffersFragment>(this, "tab2", OffersFragment.class));
		actionBar.addTab(tab2);

		Tab tab3 = actionBar
				.newTab()
				.setText(R.string.tab_locations)
				.setTabListener(new TabListener<LocationsFragment>(this, "tab3", LocationsFragment.class));
		actionBar.addTab(tab3);

		// check if there is a saved state to select active tab
		if (savedInstanceState != null) {
			getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(ACTIVE_TAB));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// save active tab
		savedInstanceState.putInt(ACTIVE_TAB, getSupportActionBar().getSelectedNavigationIndex());
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
