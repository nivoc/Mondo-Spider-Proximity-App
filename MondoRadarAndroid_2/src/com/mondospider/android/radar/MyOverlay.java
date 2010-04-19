package com.mondospider.android.radar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyOverlay extends Overlay {
	  private final Bitmap bmp;
	  private final GeoPoint gpoint;
	 
	  public MyOverlay(Bitmap bmp, GeoPoint gp) {
	    this.bmp = bmp;
	    this.gpoint = gp;
	  }
	 
	  public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	    Projection pro = mapView.getProjection();//Map�Ɖ�ʂ̈ʒu���v�Z����I�u�W�F�N�g
	    Point p = pro.toPixels(gpoint, null);    //���P�[�V��������A�\������ʒu���v�Z����
	    canvas.drawBitmap(bmp, p.x, p.y, null);  //�\������ꏊ�։摜��z�u����B
	  }
	}