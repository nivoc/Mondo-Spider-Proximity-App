package com.mondospider.android.radar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SpiderOverlay extends Overlay {
	private final Bitmap bmp;
	private final GeoPoint gpoint;
	public SpiderOverlay(Resources res, GeoPoint gpoint) {
//		this.drawable = drawable;
		this.bmp = BitmapFactory.decodeResource(res, R.anim.spider_point);
		this.gpoint = gpoint;
	}
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection pro = mapView.getProjection();//Mapと画面の位置を計算するオブジェクト
		Point p = pro.toPixels(gpoint, null);    //ロケーションから、表示する位置を計算する
		canvas.drawBitmap(this.bmp, p.x, p.y, null);  //表示する場所へ画像を配置する。
	}
}
