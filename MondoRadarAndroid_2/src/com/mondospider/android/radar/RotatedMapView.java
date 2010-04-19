package com.mondospider.android.radar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import com.google.android.maps.MapView;

/** 
 * Container that allows MapView to be rotated 
 * @author Paul Robello probello@gmail.com 
 * 
 */ 
public class RotatedMapView extends FrameLayout { 
   private static final String TAG = "RotatedMapView"; 
    
   private int mScaleDim = 0; 
    private float mLastRot = 0f; 
    private boolean mRotateEnabled = false; 
   private MapView mMapview; 
   private long mLastRefresh = 0l; 
   private float mCompassBearing = 0f; 
   private float mGpsSpeed = 0f; 
   private float mGpsBearing = 0f; 
   private float mMagneticDeclination = 0f; 
    
    
//    @Override 
    protected LayoutParams generateDefaultLayoutParams2() { 
       return getChildLayoutParams(); 
    } 
    
    /** 
     * used to provide size to child views to ensure that if rotated they will still fill the entire screen 
     * @return 
     */ 
    public LayoutParams getChildLayoutParams() { 
       if (mRotateEnabled){ 
          Display display =  ((Activity)getContext()).getWindowManager().getDefaultDisplay(); 
          int w = display.getWidth(); 
          int h = display.getHeight(); 
          mScaleDim = (int)Math.sqrt((w*w)+(h*h)); 
        
          return new LayoutParams(mScaleDim, mScaleDim); 
       }else{ 
          return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); 
       } 
    } 

   public RotatedMapView(Context context,String apiKey) { 
        super(context); 
        mMapview = new MapView(context,apiKey); 
        mMapview.setLayoutParams(getChildLayoutParams()); 
        this.addView(mMapview); 
    } 
    
   /** 
    * Handles rotation of map view (if enabled) 
    */ 
   @Override 
   protected void dispatchDraw(Canvas canvas) { 
       try{ 
          mLastRefresh=System.currentTimeMillis(); 
          if (!mRotateEnabled) return; 
          if (mCompassBearing>=0 || (mGpsSpeed>=1 && mGpsBearing>=0) ){ 
             // dims of screen canvas 
             int cw = canvas.getWidth(); 
             int ch = canvas.getHeight(); 
             // dims of scaled canvas 
             int w = mScaleDim; 
             int h = mScaleDim; 

             // center of scaled canvas 
             int cx = w / 2; 
             int cy = h / 2; 

             // center of screen canvas 
             int ccx = cw / 2; 
             int ccy = ch / 2; 
           
             // move the center of the scaled area to to the center of the screen 
             canvas.translate(-(cx-ccx), -(cy-ccy)); 

             if (mGpsBearing>=0 && mGpsSpeed>1){ 
                mLastRot=-(mGpsBearing); 
               }else if (mCompassBearing>=0){ 
                  // compensate for landscape orientation 90 degree rotation 
                  float rot_fix = 0; 
                 Configuration c = getResources().getConfiguration(); 
                 if(c.orientation == Configuration.ORIENTATION_LANDSCAPE ){ 
                    rot_fix = 90; 
                 }else{ 
                    rot_fix = 0; 
                 } 

                  mLastRot=-((mCompassBearing+mMagneticDeclination+rot_fix)); 
               } 
             // rotate around center of screen 
             canvas.rotate(mLastRot,cx,cy); 
          } 
       }catch(Exception ex){ 
          Log.e(TAG ,ex.getMessage()); 
       }finally{ 
          super.dispatchDraw(canvas); 
       } 
    } 

    /** 
     * returns if view has north locked in up position 
     * @return true if map is using bearing values for rotation 
     */ 
   public synchronized boolean isRotateEnabled() { 
      return mRotateEnabled; 
   } 

   /** 
    * set whether or not to rotate map based on bearing values 
    * @param mRotateEnabled 
    */ 
   public synchronized void setRotateEnabled(boolean mRotateEnabled) { 
      this.mRotateEnabled = mRotateEnabled; 
      mMapview.setLayoutParams(getChildLayoutParams()); 
      mMapview.invalidate(); 
   } 

   /** 
    * get current compass bearing set by setCompassBearing 
    * @return 
    */ 
   public synchronized float getCompassBearing() { 
      return mCompassBearing; 
   } 

   /** 
    * set current compass bearing only used if gps speed < 1  
    * @param mCompassBearing 
    */ 
   public synchronized void setCompassBearing(float mCompassBearing) { 
      this.mCompassBearing = mCompassBearing; 
   } 

   /** 
    * get currently set gps speed 
    * @return gps speed set by setGpsSpeed 
    */ 
   public synchronized float getGpsSpeed() { 
      return mGpsSpeed; 
   } 

   /** 
    * sets gps speed which is used to determin if compass or gps bearing should be used 
    * @param mGpsSpeed 
    */ 
   public synchronized void setGpsSpeed(float mGpsSpeed) { 
      this.mGpsSpeed = mGpsSpeed; 
   } 

   /** 
    * get current gps bearing 
    * @return 
    */ 
   public synchronized float getGpsBearing() { 
      return mGpsBearing; 
   } 

   /** 
    * set gps bearing. only used if gps speed>=1 
    * @param mGpsBearing 
    */ 
   public synchronized void setGpsBearing(float mGpsBearing) { 
      this.mGpsBearing = mGpsBearing; 
   } 

   /** 
    * return difference between magnetic and true north for your location 
    * @return 
    */ 
   public synchronized float getMagneticDeclination() { 
      return mMagneticDeclination; 
   } 

   /** 
    * set magnetic declination used by compass rotate 
    * @param magneticDeclination 
    */ 
   public synchronized void setMagneticDeclination(float magneticDeclination) { 
      this.mMagneticDeclination=magneticDeclination; 
   } 

   /** 
    * get difference between true north and magnetic north for current location. 
    * required valid compass bearing, gps fix and gps speed>=1 
    * value set = gpsBearing - compassBearing 
    */ 
   public synchronized void setMagneticDeclinationFromGps() { 
      if (mGpsSpeed>=1 && mGpsBearing>=0 && mCompassBearing>=0){ 
         mMagneticDeclination=mGpsBearing-mCompassBearing; 
      } 
   } 
    
   /** 
    * get MapView child for this ViewGroup 
    * @return 
    */ 
   public synchronized MapView getMapview() { 
      return mMapview; 
   } 

   /** 
    * get time of last refresh as returned by System.currentTimeMillis() 
    * @return 
    */ 
   public synchronized long getLastRefresh() { 
      return mLastRefresh; 
   } 

   public synchronized float getLastRot() { 
      return mLastRot; 
   } 
} 