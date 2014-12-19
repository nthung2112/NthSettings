package com.nth.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.nth.settings.colorpicker.ColorPickerPreference;
import com.nth.settings.utils.Common;
import com.nth.settings.utils.SettingsUtils;

@SuppressWarnings("deprecation")
public class StatusBarIcon extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceChangeListener
{
	private ListPreference mBatteryIcon;
	private CheckBoxPreference mBatteryBar;
	private CheckBoxPreference mBatteryBarChargingAnimation;
	private ColorPickerPreference mBatteryBarColor;
	private ListPreference mBatteryBarStyle;
	private ListPreference mBatteryBarThickness;
	private ListPreference mClockAmPmstyle;
	private ColorPickerPreference mClockColor;
	private ListPreference mClockStyle;
	private ListPreference mClockWeekday;
	private ListPreference mSingleIcon;
	private ListPreference mSingleStyle;
	private ListPreference mWifiIcon;
	private Common com;

	private void init()
	{
		mBatteryIcon = (ListPreference) findPreference("statusbar_battery_icon");
		mSingleIcon = (ListPreference) findPreference("signal_icon");
		mWifiIcon = (ListPreference) findPreference("wifi_icon");
		mSingleStyle = (ListPreference) findPreference("signal_cluster_type");
		mClockStyle = (ListPreference) findPreference("statusbar_clock_style");
		mClockAmPmstyle = (ListPreference) findPreference("statusbar_clock_am_pm_style");
		mClockWeekday = (ListPreference) findPreference("statusbar_clock_weekday");
		mClockColor = (ColorPickerPreference) findPreference("statusbar_clock_color");

		mBatteryBar = (CheckBoxPreference) findPreference("statusbar_battery_bar");
		mBatteryBarChargingAnimation = (CheckBoxPreference) findPreference("statusbar_battery_bar_animate");
		mBatteryBarStyle = (ListPreference) findPreference("statusbar_battery_bar_style");
		mBatteryBarThickness = (ListPreference) findPreference("statusbar_battery_bar_thickness");
		mBatteryBarColor = (ColorPickerPreference) findPreference("statusbar_battery_bar_color");
	}

	private void initListener()
	{
		mBatteryIcon.setOnPreferenceChangeListener(this);
		mSingleIcon.setOnPreferenceChangeListener(this);
		mWifiIcon.setOnPreferenceChangeListener(this);
		mSingleStyle.setOnPreferenceChangeListener(this);
		mClockStyle.setOnPreferenceChangeListener(this);
		mClockAmPmstyle.setOnPreferenceChangeListener(this);
		mClockWeekday.setOnPreferenceChangeListener(this);
		mClockColor.setOnPreferenceChangeListener(this);

		mBatteryBar.setOnPreferenceChangeListener(this);
		mBatteryBarChargingAnimation.setOnPreferenceChangeListener(this);
		mBatteryBarStyle.setOnPreferenceChangeListener(this);
		mBatteryBarThickness.setOnPreferenceChangeListener(this);
		mBatteryBarColor.setOnPreferenceChangeListener(this);
	}

	private void removeUnsupported()
	{

	}

	private void initList(ListPreference lp)
	{
		// lp.setValueIndex(SettingsUtils.getPreferenceInt(getContentResolver(),
		// lp.getKey(), 0));
		lp.setSummary(lp.getEntry());
	}

	private void initPrefs()
	{
		initList(this.mBatteryIcon);
		initList(this.mSingleIcon);
		initList(this.mWifiIcon);
		initList(this.mSingleStyle);
		initList(this.mClockStyle);
		initList(this.mClockAmPmstyle);
		initList(this.mClockWeekday);
		initList(this.mBatteryBarStyle);
		initList(this.mBatteryBarThickness);

		com.initCheckbox(this.mBatteryBar);
		com.initCheckbox(this.mBatteryBarChargingAnimation);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.status_bar_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		com = new Common(getContentResolver());

		init();
		initListener();
		removeUnsupported();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if (preference == this.mBatteryIcon || preference == this.mSingleIcon
				|| preference == this.mWifiIcon
				|| preference == this.mSingleStyle
				|| preference == this.mClockStyle
				|| preference == this.mClockAmPmstyle
				|| preference == this.mClockWeekday
				|| preference == this.mBatteryBarStyle
				|| preference == this.mBatteryBarThickness)
		{
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), newValue.toString());
		}

		if (preference == this.mBatteryBar
				|| preference == this.mBatteryBarChargingAnimation)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}

		if (preference == mClockColor || preference == mBatteryBarColor)
		{
			String s = ColorPickerPreference.convertToARGB(Integer
					.valueOf(String.valueOf(newValue)));
			preference.setSummary(s);
			int l = ColorPickerPreference.convertToColorInt(s);
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), l);
		}

		return true;
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		com.setSummary(key, this.mBatteryIcon);
		com.setSummary(key, this.mSingleIcon);
		com.setSummary(key, this.mWifiIcon);
		com.setSummary(key, this.mSingleStyle);
		com.setSummary(key, this.mClockStyle);
		com.setSummary(key, this.mClockAmPmstyle);
		com.setSummary(key, this.mClockWeekday);
		com.setSummary(key, this.mBatteryBarStyle);
		com.setSummary(key, this.mBatteryBarThickness);
	}
}
