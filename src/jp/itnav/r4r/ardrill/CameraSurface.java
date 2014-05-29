package jp.itnav.r4r.ardrill;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurface extends SurfaceView {


	private Camera c;

	public CameraSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CameraSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CameraSurface(Context context) {
		super(context);
		init();
	}
	

	public void init() {
		final SurfaceHolder holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder hoder) {
				c = Camera.open(0);
				try {
					c.setPreviewDisplay(holder);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

				c.stopPreview();

				Parameters params = c.getParameters();
				Size sz = params.getSupportedPreviewSizes().get(0);
				params.setPreviewSize(sz.width, sz.height);
				//c.setParameters(params);

				//c.setDisplayOrientation(90);
				c.startPreview();
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				c.release();
				c = null;

			}

		});

		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}
}