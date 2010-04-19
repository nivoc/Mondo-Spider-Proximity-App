package com.mondospider.android.overlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class SpiderOverlayItem extends OverlayItem {

    public SpiderOverlayItem(GeoPoint point){
        super(point, "", "");
    }
}