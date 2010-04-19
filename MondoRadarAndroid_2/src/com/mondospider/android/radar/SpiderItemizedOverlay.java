package com.mondospider.android.radar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

public class SpiderItemizedOverlay extends ItemizedOverlay<SpiderOverlayItem> {

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    public SpiderItemizedOverlay(Drawable defaultMarker) {
        super( boundCenterBottom(defaultMarker) );
    }

    @Override
    protected SpiderOverlayItem createItem(int i) {
        GeoPoint point = points.get(i);
        return new SpiderOverlayItem(point);
    }

    @Override
    public int size() {
        return points.size();
    }

    public void addPoint(GeoPoint point) {
        this.points.add(point);
        populate();
    }
	
    public void clearPoint() {
        this.points.clear();
        populate();
    }
}