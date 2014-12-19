package com.nth.settings;

import com.nth.settings.utils.Common;
import com.nth.settings.utils.SettingsUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

@SuppressWarnings("deprecation")
public class OtherSettings extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener,
		SharedPreferences.OnSharedPreferenceChangeListener
{
	private CheckBoxPreference mDisableStatusBarPullDown;
	private CheckBoxPreference mDoubleTap;
	private CheckBoxPreference mGestureAnswering;
	private CheckBoxPreference mPreventMisoperationPref;
	private Preference mScanAllMedia;
	private CheckBoxPreference mScanMedia;
	private Common com;

	private void init()
	{
		this.mPreventMisoperationPref = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("unlock_sensor_disable"));
		this.mGestureAnswering = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("gesture_answering"));
		this.mDisableStatusBarPullDown = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("disable_pulldown"));
		this.mScanMedia = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("nth_disable_media_scan"));
		this.mDoubleTap = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("gesture_trun_screen_on"));
		this.mScanAllMedia = getPreferenceScreen().findPreference(
				"scan_all_media");
	}

	private void initListener()
	{
		this.mPreventMisoperationPref.setOnPreferenceChangeListener(this);
		this.mGestureAnswering.setOnPreferenceChangeListener(this);
		this.mDisableStatusBarPullDown.setOnPreferenceChangeListener(this);
		this.mScanMedia.setOnPreferenceChangeListener(this);
		this.mDoubleTap.setOnPreferenceChangeListener(this);

		this.mScanAllMedia
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference paramPreference)
					{
						scanMediaNow();
						return true;
					}
				});
	}

	private void initPrefs()
	{
		com.initCheckbox(this.mPreventMisoperationPref, 0);
		com.initCheckbox(this.mDisableStatusBarPullDown, 0);
		com.initCheckbox(this.mScanMedia, 0);
		com.initCheckbox(this.mGestureAnswering, 0);
		com.initCheckbox(this.mDoubleTap, 1);
	}

	private void removeUnsupported()
	{
		Resources r = getResources();
		PreferenceScreen ps = getPreferenceScreen();
		if (!r.getBoolean(R.bool.enable_screen_on_proximity_sensor))
			ps.removePreference(this.mPreventMisoperationPref);
		if (!r.getBoolean(R.bool.gesture_answering))
			ps.removePreference(this.mGestureAnswering);
		if (!r.getBoolean(R.bool.disable_pull_down_lock))
			ps.removePreference(this.mDisableStatusBarPullDown);
		if (!r.getBoolean(R.bool.scan_media_on_boot))
		{
			ps.removePreference(this.mScanMedia);
			ps.removePreference(this.mScanAllMedia);
		}
		if (!r.getBoolean(R.bool.home_double))
			ps.removePreference(this.mDoubleTap);
	}

	@Override
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.other_settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		com = new Common(getContentResolver());

		init();
		initListener();
		removeUnsupported();
	}

	private void scanMediaNow()
	{
		final int i = SettingsUtils.getPreferenceInt(getContentResolver(),
				"nth_disable_media_scan", 0);
		SettingsUtils.setPreferenceInt(getContentResolver(),
				"nth_disable_media_scan", 0);
		sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED",
				Uri.parse((new StringBuilder("file://")).append(
						Environment.getExternalStorageDirectory()).toString())));
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run()
			{
				SettingsUtils.setPreferenceInt(getContentResolver(),
						"nth_disable_media_scan", i);
			}
		}, 1000L);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if (preference == this.mPreventMisoperationPref
				|| preference == this.mGestureAnswering
				|| preference == this.mDisableStatusBarPullDown
				|| preference == this.mScanMedia
				|| preference == this.mDoubleTap)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(
			SharedPreferences paramSharedPreferences, String paramString)
	{
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		initPrefs();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}
}
