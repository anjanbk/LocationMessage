package com.hackrgt.katanalocate;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private Location selectedLocation;
	
	public MapItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
	  super(boundCenterBottom(defaultMarker));
	  mContext = context;
	}
	
	 @Override
	 public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		 final int action = event.getAction();
		 boolean result = false;
		 if (action == MotionEvent.ACTION_DOWN) {
			 GeoPoint p = mapView.getProjection().fromPixels(
                     (int) event.getX(),
                     (int) event.getY());
			 mOverlays.add(new OverlayItem(p, "", ""));
			 populate();
			 result = true;
		 }
		 return(result || super.onTouchEvent(event, mapView));
	 }
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
//	  Toast.makeText(mContext, item.getTitle()+" selected...", Toast.LENGTH_SHORT).show();
//	  
//	  /*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//	  dialog.setTitle(item.getTitle());
//	  dialog.setMessage(item.getSnippet());
//	  dialog.show();*/
//	  
//	  //Store as selected location
//	  GeoPoint p = item.getPoint();
//	  Location loc = new Location(LocationManager.GPS_PROVIDER);
//	  if (loc != null) {
//		  loc.setLatitude(p.getLatitudeE6()/1000000F);
//		  loc.setLongitude(p.getLongitudeE6()/1000000F);
//		  selectedLocation = loc;
//	  }
	  return true;
	}
	
	

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	public Location getSelectedLocation() {
		return selectedLocation;
	}
	

}
