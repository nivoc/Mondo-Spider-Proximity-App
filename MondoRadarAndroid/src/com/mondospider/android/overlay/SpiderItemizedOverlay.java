/*
 * Copyright (C) 2010 The Mondospider Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mondospider.android.overlay;

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
    	if(point != null){
    		this.points.add(point);
        	populate();
    	}
    }
	
    public void clearPoint() {
        this.points.clear();
        populate();
    }
}