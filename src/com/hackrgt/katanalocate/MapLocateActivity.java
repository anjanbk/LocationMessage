package com.hackrgt.katanalocate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapLocateActivity extends MapActivity {
    
	private EditText mapSearchBox;
	private MapView mapView;
	private MyLocationOverlay myLocOverlay;
	private Location selectedLocation;
	
	private ArrayList<OverlayItem> overlays;
	private MapItemizedOverlay itemizedOverlay;
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private Location getLocation(GeoPoint point) {
		
		return null;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.map_view);
	     MapView mapView = (MapView) findViewById(R.id.mapview);
	     mapView.setBuiltInZoomControls(true);
	     Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
	     mapView.getOverlays().add(new MapItemizedOverlay(drawable));
	     myLocOverlay=new MyLocationOverlay(this, mapView);
	     mapView.getOverlays().add(myLocOverlay);
	     
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_view);
//        
//        mapView = (MapView)findViewById(R.id.mapview);
//        mapView.setBuiltInZoomControls(true);
//        
//        // Center location on current position
//        List<Overlay> overlays = mapView.getOverlays();
//        myLocOverlay = new MyLocationOverlay(this, mapView);
//        
//        /**Changes*******************/
//        itemizedOverlay = new MapItemizedOverlay(drawable, this);
//        
//        GeoPoint myLoc = myLocOverlay.getMyLocation();
//        myLocOverlay.enableMyLocation();
//        //myLocOverlay.onTouchEvent(this, mapView);
//        mapView.getOverlays().add(myLocOverlay);
//        
//        if (myLoc != null)
//        	System.out.println(myLoc.getLatitudeE6()+", "+myLoc.getLongitudeE6());
//        else
//        	System.out.println("My Loc is null!");
//        
//        /**End of Changes*******************/
//        
//        overlays.add(myLocOverlay);
//        
//        myLocOverlay.runOnFirstFix(new Runnable() {
//        	public void run() {
//        		mapView.getController().animateTo(myLocOverlay.getMyLocation());
//        		mapView.getController().setZoom(16);
//        	}
//        });
//        
//        mapSearchBox  = (EditText)findViewById(R.id.search_box);
//        mapSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//	                    actionId == EditorInfo.IME_ACTION_DONE ||
//	                    actionId == EditorInfo.IME_ACTION_GO ||
//	                    event.getAction() == KeyEvent.ACTION_DOWN &&
//	                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//	                imm.hideSoftInputFromWindow(mapSearchBox.getWindowToken(), 0);
//
//	                new SearchClicked(mapSearchBox.getText().toString()).execute();
//	                mapSearchBox.setText("", TextView.BufferType.EDITABLE);
//	                return true;
//				}
//				return false;
//			}
//		});
    }
	
	private class SearchClicked extends AsyncTask<Void, Void, Boolean> {
        private String toSearch;
        private Address address;

        public SearchClicked(String toSearch) {
            this.toSearch = toSearch;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
                List<Address> results = geocoder.getFromLocationName(toSearch, 1);

                if (results.size() == 0) {
                    return false;
                }

                address = results.get(0);
               

                //Now do something with this GeoPoint:
                GeoPoint p = new GeoPoint((int) (address.getLatitude() * 1E6), (int) (address.getLongitude() * 1E6));
                OverlayItem overlay = new OverlayItem(p, toSearch, "");
                itemizedOverlay.addOverlay(overlay);
                mapView.getOverlays().add(itemizedOverlay);

            } catch (Exception e) {
                Log.e("", "Something went wrong: ", e);
                return false;
            }
            return true;
        }
        
        }
}