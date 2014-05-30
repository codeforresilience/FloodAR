/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
