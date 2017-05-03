package com.dysen.myUtil;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by dysen on 2017/4/20.
 */

public class SoundPoolUtil {


	/**
     * SoundPool 相关配置(初始化)
	 * @param soundPool
     * @return
     * @throws Exception
	 */
	public static SoundPool initSP(SoundPool soundPool) {

		//当前系统的SDK版本大于等于21(Android 5.0)时
		if (Build.VERSION.SDK_INT >= 21) {
			SoundPool.Builder builder = new SoundPool.Builder();
			//传入音频数量
			builder.setMaxStreams(2);
			//AudioAttributes是一个封装音频各种属性的方法
			AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
			//设置音频流的合适的属性
			attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
			//加载一个AudioAttributes
			builder.setAudioAttributes(attrBuilder.build());
			soundPool = builder.build();
		}
		//当系统的SDK版本小于21时
		else {//设置最多可容纳2个音频流，音频的品质为5
			soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		}

		return soundPool;
	}
}
