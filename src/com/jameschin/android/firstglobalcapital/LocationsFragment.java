package com.jameschin.android.firstglobalcapital;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationsFragment extends SherlockFragment {
	String mTag;
	
	private GoogleMap mMap;
	private SupportMapFragment mMapFragment;
	private Location currentLocation;
	private double currentLatitude;
	private double currentLongitude;
    private String saddr = "Menlo Park, CA";
    private String daddr = "3114 Crestfield Blvd, San Ramon, CA 94582";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_locations, container, false);
		
		checkGooglePlayServices();
		
		//new AsyncTaskGetCurrentLocation().execute(); //still too slow
		setUpMapIfNeeded();
		
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		//delete all nested fragments, to prevent crashing upon returning after swapping fragments
		try {
			FragmentTransaction ft = getSherlockActivity().getSupportFragmentManager().beginTransaction();
			ft.remove(mMapFragment);
			ft.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setProviderId(String tag) {
		this.mTag = tag;
	}
	
	public void checkGooglePlayServices() {
		//check if GooglePlayServices is available on the device, toast status
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getSherlockActivity());
				switch(resultCode) {
				case 0: Toast.makeText(getSherlockActivity(), R.string.GooglePlayServices0, Toast.LENGTH_SHORT).show(); break;
				case 1: Toast.makeText(getSherlockActivity(), R.string.GooglePlayServices1, Toast.LENGTH_SHORT).show(); break;
				case 2: Toast.makeText(getSherlockActivity(), R.string.GooglePlayServices2, Toast.LENGTH_SHORT).show(); break;
				case 3: Toast.makeText(getSherlockActivity(), R.string.GooglePlayServices3, Toast.LENGTH_SHORT).show(); break;
				case 9: Toast.makeText(getSherlockActivity(), R.string.GooglePlayServices9, Toast.LENGTH_SHORT).show(); break;
				}
				
		if(resultCode != ConnectionResult.SUCCESS)
		{
		        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getSherlockActivity(), 69);
		        dialog.show();
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map. 
		// NOTE: Since we have a nested SupportMapFragment within SherlockFragment AND we are trying to preserve compatibility with older Android, we have to delete the SupportMapFragment to avoid crashing when leaving and returning to this SherlockFragment.
		//if (mMap == null) {
			mMapFragment = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map));
			mMap = mMapFragment.getMap();
			//mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			
			setUpMarkers();
			
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.

			}
		//} 
	}
	
	private void setUpMarkers() {
		double latitude = 0; 
		double longitude = 0;
		
		Geocoder geocoder = new Geocoder(getSherlockActivity(), Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocationName("4588 Dublin Blvd, Dublin, CA 94568", 1);
			if (addresses.size() > 0) {
				latitude = addresses.get(0).getLatitude();
				longitude = addresses.get(0).getLongitude();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(getString(R.string.app_name) + " 1").snippet(getString(R.string.get_directions)));
		//move and zoom camera to initial location, without animation
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8.0f));
		
		//set Get Directions
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {
				//by Source-Address/Destination-Address
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + saddr + "&daddr=" + daddr));
				startActivityForResult(intent, 0);
			}
		});
	}

	void updateLocation(Location location) {
		currentLocation = location;
		currentLatitude = currentLocation.getLatitude();
		currentLongitude = currentLocation.getLongitude();
	}
	
	public class AsyncTaskGetCurrentLocation extends AsyncTask<Void, String, Boolean> {
		public LocationManager locationManager;
		public LocationListener locationListener;

		protected void onPreExecute() {
			locationManager = (LocationManager) getSherlockActivity().getSystemService(Context.LOCATION_SERVICE);

			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {updateLocation(location);}
				public void onStatusChanged(String provider, int status, Bundle extras) { }
				public void onProviderEnabled(String provider) { }
				public void onProviderDisabled(String provider) { }
			};

			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

			Geocoder geocoder = new Geocoder(getSherlockActivity(), Locale.getDefault());
			try {
				List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
				if (addresses.size() > 0) {
					StringBuilder result = new StringBuilder();
					for (int i = 0; i < addresses.size(); i++) {
						Address address = addresses.get(i);
						int maxIndex = address.getMaxAddressLineIndex();
						for (int x = 0; x <= maxIndex; x++) {
							result.append(address.getAddressLine(x));
							result.append(",");
						}
						result.append(address.getLocality());
						result.append(",");
						result.append(address.getPostalCode());
					}
					saddr = result.toString();
					//stop getting updates, to save power
					locationManager.removeUpdates(locationListener);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}

	    protected Boolean doInBackground(Void... params) {
	        while (currentLocation == null) { }
	        return true;
	    }

	    // This is called each time you call publishProgress()
	    protected void onProgressUpdate(String... progress) {
	    }

	    // This is called when doInBackground() is finished
	    protected void onPostExecute(Boolean result) {
	    	Toast.makeText(getSherlockActivity(), saddr, Toast.LENGTH_LONG).show();
	    }
	}

}