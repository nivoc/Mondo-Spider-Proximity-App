package com.mondospider.android.radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
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
		/*
		DrawFilter xxx = canvas.getDrawFilter();
		
		Paint p = new Paint();
		ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
		ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
		p.setColorFilter(cmcf);
		*/
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
