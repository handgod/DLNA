package com.geniusgithub.mediarender;

import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.geniusgithub.mediarender.util.CommonLog;
import com.geniusgithub.mediarender.util.LogFactory;
import com.soniq.mediahelper.CrashHandler;
import com.tendcloud.tenddata.TCAgent;

public class RenderApplication extends Application implements ItatisticsEvent {

	private static final CommonLog log = LogFactory.createLog();

	private static RenderApplication mInstance;

	private DeviceInfo mDeviceInfo;

	private static Activity img;
	private static Activity music;
	private static Activity video;

	public static void setImg(Activity imgActivity) {
		img = imgActivity;
	}

	public static void setMusic(Activity musicActivity) {
		music = musicActivity;
	}

	public static void setVideo(Activity videoActivity) {
		video = videoActivity;
	}

	public static void closeImg() {
		if (img != null) {
			img.finish();
		}
	}

	public static void closeVideo() {
		if (video != null) {
			video.finish();
		}
	}

	public static void closeMusic() {
		if (music != null) {
			music.finish();
		}
	}

	public synchronized static RenderApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		log.e("RenderApplication onCreate");

		mInstance = this;
		mDeviceInfo = new DeviceInfo();

		TCAgent.init(this);
		CrashHandler catchHandler = CrashHandler.getInstance();
		catchHandler.init(this);
	}

	public void updateDevInfo(String name, String uuid) {
		mDeviceInfo.dev_name = name;
		mDeviceInfo.uuid = uuid;
	}

	public void setDevStatus(boolean flag) {
		mDeviceInfo.status = flag;
		DeviceUpdateBrocastFactory.sendDevUpdateBrocast(this);
	}

	public DeviceInfo getDevInfo() {
		return mDeviceInfo;
	}

	@Override
	public void onEvent(String eventID) {
		log.e("eventID = " + eventID);
		TCAgent.onEvent(this, eventID);
	}

	@Override
	public void onEvent(String eventID, HashMap<String, String> map) {
		log.e("eventID = " + eventID);
		TCAgent.onEvent(this, eventID, "", map);
	}

	public static void onPause(Activity context) {
		TCAgent.onPause(context);
	}

	public static void onResume(Activity context) {
		TCAgent.onResume(context);
	}

	public static void onCatchError(Context context) {
		TCAgent.setReportUncaughtExceptions(true);
	}
}
