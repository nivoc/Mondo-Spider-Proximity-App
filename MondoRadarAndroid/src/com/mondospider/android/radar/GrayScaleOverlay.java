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

package com.mondospider.android.radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class GrayScaleOverlay extends MyLocationOverlay {
	public boolean myLocationFlag = false;
	private MapView mapview;
	public GrayScaleOverlay(Context context, MapView mapView) {
		super(context, mapView);
		this.mapview = mapView;
	}
	@Override  
	public synchronized boolean draw(
			Canvas canvas,
			MapView mapView,  
            boolean shadow,
            long when) {
//		mapView.getBackground().
//		mapView.
//		DrawFilter xxx = canvas.getDrawFilter();
//		canvas.set

//		Drawable d = new MyStateDrawable(); 
//		mapView.setBackgroundDrawable(d);
		/*
		mapView.setDrawingCacheEnabled(true);
//		mapView.buildDrawingCache();
		Log.e("xxx",mapView.toString());
		Bitmap source = mapView.getDrawingCache();
		mapView.destroyDrawingCache();
		
		ColorMatrix cm = new ColorMatrix();
	 	cm.setSaturation(0);
	 	
	 	ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
	 	
	 	Paint p = new Paint();
        p.setColorFilter(cmcf);
        
        Bitmap grayscaleBitmap = Bitmap.createBitmap(
	            source.getWidth(), source.getHeight(),
	            Bitmap.Config.RGB_565);
        
        Canvas newCanvas = new Canvas(grayscaleBitmap);
        newCanvas.drawBitmap(source, 0, 0, p);
        */
		/*
		p.setColorFilter(f);
        int xo = (w-500)/2;
        int yo = (h-500)/2;
//		mapView.draw( canvas ); 
		canvas.setDrawFilter(new DrawFilter());
		
		*/
//		canvas.rotate(degrees);
        boolean ret = super.draw(canvas, mapView, shadow, when);
//        if(myLocationFlag){  
//            drawMyLocation(canvas, mapview, getLastFix(), getMyLocation(), 5000);  
//        }  
        return ret;  
    }  
}
class MyStateDrawable extends Drawable {

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		Paint p = new Paint();
		ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
		ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
		p.setColorFilter(cmcf);
	}
}

