package jp.itnav.r4r.ardrill;

import android.content.res.AssetManager;

// Wrapper for native library

public class ARdrillJNILib {

     static {
         System.loadLibrary("ARdrill");
     }

    /**
     * @param width the current view width
     * @param height the current view height
     */
     public static native void setUp(String extPath, AssetManager assetManager, int avatarType);
     public static native void init(int width, int height);
     public static native void step();
     public static native void gainedFocus();
     public static native void resume();
     public static native void pause();
     public static native void setCameraAxis(float x, float y, float z, float w);
     public static native void setAnimationNumber(int number);
     public static native void setWaterLevel(float level);
     public static native void setAnimationSpeed(float speed);
}
