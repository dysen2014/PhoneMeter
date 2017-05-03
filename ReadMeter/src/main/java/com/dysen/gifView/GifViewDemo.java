package com.dysen.gifView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import com.dysen.gifView.util.GifAction;
import com.dysen.gifView.util.GifView;
import com.dysen.gifView.util.GifView.GifImageType;
import com.dysen.qj.wMeter.R;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.load.UserDemo;

/**
 * 
 * @author dysen
 * @version 2014-12-23 上午9:16:34
 */
public class GifViewDemo extends MyActivityTools {

	private ImageView imageView = null;
	private Animation alphaAnimation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gif_view);
		imageView = (ImageView) findViewById(R.id.welcome_image_view);
		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true); // 启动Fill保持
		alphaAnimation.setFillAfter(true); // 设置动画的最后一帧是保持在View上面
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener((AnimationListener)this); // 为动画设置监听


		myStartGif();
	}

	/**
	 *	dysen
	 *	2015-7-11 上午11:45:15
	 *	info: 启动 动画 gif 播放
	 */
	public  void myStartGif() {
		
		// 把该 Activity 添加到 activity列表里 方便退出时一下子退出所有activity
				// QuitAllActivity.getInstance().addActivity(this);

				// 从xml中得到GifView的句柄
				GifView gf1 = (GifView) findViewById(R.id.gif1);
				// 设置Gif图片源
				gf1.setGifImage(R.drawable.loading);
				// 添加监听器
				gf1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
						startActivity(new Intent(GifViewDemo.this, UserDemo.class));
					}
				});
				// 设置显示的大小，拉伸或者压缩
				gf1.setShowDimension(myScreenWidth(), myScreenHeight());

				long l1 = System.currentTimeMillis();
				// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
				gf1.setGifImageType(GifImageType.SYNC_DECODER);

				new GifAction() {

					@Override
					public void parseOk(boolean arg0, int arg1) {

						if (arg1 == -1 || arg0 == true) {
							startActivity(new Intent(GifViewDemo.this, UserDemo.class));
						}
					}
				};
	}

	/**
	 * 获取屏幕的宽 sen dy 2015-1-28 下午12:34:17
	 */
	public int myScreenWidth() {

		// 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中
		DisplayMetrics DM = new DisplayMetrics();
		// 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
		getWindowManager().getDefaultDisplay().getMetrics(DM);

		float scale, pxValue;

		pxValue = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		scale = this.getResources().getDisplayMetrics().density;
		int wdip = (int) (pxValue / scale + 0.5f);

		return (int) pxValue;
	}

	/**
	 * 获取屏幕的高 sen dy 2015-1-28 下午12:34:17
	 */
	public int myScreenHeight() {

		float scale, pxValue;

		pxValue = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		scale = this.getResources().getDisplayMetrics().density;
		int hdip = (int) (pxValue / scale + 0.5f);

		return (int) pxValue;
	}
	
	/**
	 *	dysen
	 *	2015-8-22 上午10:18:21
	 *	info:	返回到上个Activity
	 */
	public void myBack(View v){
		finish();
	}
}
