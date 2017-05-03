package com.dysen.mylibrary.utils.zbarscan;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zbar.build.CameraManager;
import com.dtr.zbar.build.CameraPreview;
import com.dtr.zbar.build.ZBarDecoder;
import com.dysen.mylibrary.R;
import com.dysen.mylibrary.utils.MyActivityTools;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author dysen 扫码 工具类
 * @code 99
 */
public class ScanCodeUtils extends MyActivityTools {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;

	private TextView scanResult, tvHintTitle;
	private FrameLayout scanPreview;
	private Button scanRestart;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	public static int code = 99;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scan_code);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//透明状态栏
//		StatusBarUtil.setTransparent(this);
		((TextView)bindView(R.id.tv_hint)).setText("扫码");
		bindView(R.id.ll_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				myResult("");
			}
		});

		findViewById();
		addEvents();
		initViews();
	}

	private void findViewById() {
		scanPreview = bindView(R.id.capture_preview);
		scanResult = bindView(R.id.capture_scan_result);
		tvHintTitle = bindView(R.id.tv_hint_title);
		scanRestart = bindView(R.id.capture_restart_scan);
		scanContainer = bindView(R.id.capture_container);
		scanCropView = bindView(R.id.capture_crop_view);
		scanLine = bindView(R.id.capture_scan_line);

		bindView(R.id.ll_top).setAlpha((float) 0.4);
	}

	/**
	 * dysen 2015-8-22 上午10:18:21 info: 返回到上个Activity
	 */
//	public void myBack(View v) {
//		myResult("");
//	}

	private void addEvents() {
		scanRestart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanResult.setText("Scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
	}

	private void initViews() {
		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		scanPreview.addView(mPreview);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.85f);
		animation.setDuration(3000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		@Override
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x
							+ y * size.width];
			}

			// 宽高也要调整
			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();
			ZBarDecoder zBarDecoder = new ZBarDecoder();
			String result = zBarDecoder.decodeCrop(rotatedData, size.width,
					size.height, mCropRect.left, mCropRect.top,
					mCropRect.width(), mCropRect.height());

			if (!TextUtils.isEmpty(result)) {

				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				// 添加震动效果，提示用户扫描完成
				Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				mVibrator.vibrate(200);
				myResult(result);

				scanResult.setText("barcode result " + result);
				barcodeScanned = true;
			}
		}
	};

	void myResult(String result) {

		try {// 过滤 含字符的扫码结果

			// 数据是使用Intent返回
			Intent intent = new Intent();
			// 把返回数据存入Intent
			intent.putExtra("result", result);
			// 设置返回数据
			ScanCodeUtils.this.setResult(code, intent);
			// 关闭Activity
			ScanCodeUtils.this.finish();
		} catch (Exception e) {
			// return false;// 如果抛出异常，返回False
			tvHintTitle.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			myResult("");
		}
		return false;
	}

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
