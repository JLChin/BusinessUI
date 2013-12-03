package com.jameschin.android.firstglobalcapital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class OffersFragment extends SherlockFragment {
	String mTag;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_offers, container, false);
	
		return view;
	}
	
	public void setProviderId(String tag) {
		this.mTag = tag;
	}
}