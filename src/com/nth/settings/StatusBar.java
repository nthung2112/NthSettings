package com.nth.settings;

import com.nth.settings.utils.Common;
import com.nth.settings.utils.SettingsUtils;
import com.nth.settings.widget.SeekBarPreference;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

@SuppressWarnings("deprecation")
public class StatusBar extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener,
		SharedPreferences.OnSharedPreferenceChangeListener
{
	private ListPreference mClockTapPressPref;
	private ListPreference mClockLongPressPref;
	private SeekBarPreference mStatusBarTrans;
	private CheckBoxPreference mNetworkSpeedPref;
	private ListPreference mNetworkSpeedInterval;
	private CheckBoxPreference mShowCarrierLogo;
	private EditTextPreference mCarrierLogoText;
	private CheckBoxPreference mStatusBarSwipeBrightness;
	private ListPreference mNotiTapPressPref;
	private ListPreference mNotiLongPressPref;
	private Common com;

	private void init()
	{
		this.mClockTapPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("clock_click"));
		this.mClockLongPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("clock_long_press"));

		this.mNotiTapPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("noti_zone_click"));
		this.mNotiLongPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("noti_zone_long_press"));

		this.mNetworkSpeedPref = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("network_speed"));
		this.mNetworkSpeedInterval = ((ListPreference) getPreferenceScreen()
				.findPreference("network_speed_refresh"));

		this.mShowCarrierLogo = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("nth_show_carrier_label"));
		this.mCarrierLogoText = ((EditTextPreference) getPreferenceScreen()
				.findPreference("nth_custom_carrier_label"));

		this.mStatusBarSwipeBrightness = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("status_bar_swipe_brightness"));
	}

	private void initListener()
	{
		this.mClockTapPressPref.setOnPreferenceChangeListener(this);
		this.mClockLongPressPref.setOnPreferenceChangeListener(this);
		this.mNotiTapPressPref.setOnPreferenceChangeListener(this);
		this.mNotiLongPressPref.setOnPreferenceChangeListener(this);
		this.mNetworkSpeedPref.setOnPreferenceChangeListener(this);
		this.mNetworkSpeedInterval.setOnPreferenceChangeListener(this);
		this.mShowCarrierLogo.setOnPreferenceChangeListener(this);
		this.mCarrierLogoText.setOnPreferenceChangeListener(this);
		this.mStatusBarSwipeBrightness.setOnPreferenceChangeListener(this);
	}

	private void initPrefs()
	{
		com.initCheckbox(mNetworkSpeedPref);
		com.initCheckbox(mShowCarrierLogo);
		com.initCheckbox(mStatusBarSwipeBrightness);

		this.mClockTapPressPref.setValueIndex(SettingsUtils.getPreferenceInt(
				getContentResolver(), this.mClockTapPressPref.getKey(), 0));
		this.mClockTapPressPref.setSummary(this.mClockTapPressPref.getEntry());

		this.mClockLongPressPref.setValueIndex(SettingsUtils.getPreferenceInt(
				getContentResolver(), this.mClockLongPressPref.getKey(), 0));
		this.mClockLongPressPref
				.setSummary(this.mClockLongPressPref.getEntry());

		this.mNotiTapPressPref.setValueIndex(SettingsUtils.getPreferenceInt(
				getContentResolver(), this.mNotiTapPressPref.getKey(), 0));
		this.mNotiTapPressPref.setSummary(this.mNotiTapPressPref.getEntry());

		this.mNotiLongPressPref.setValueIndex(SettingsUtils.getPreferenceInt(
				getContentResolver(), this.mNotiLongPressPref.getKey(), 0));
		this.mNotiLongPressPref.setSummary(this.mNotiLongPressPref.getEntry());

		String s = SettingsUtils.getPreferenceString(getContentResolver(),
				mCarrierLogoText.getKey());
		if (s == null || TextUtils.isEmpty(s))
		{
			s = "";
		}
		mCarrierLogoText.setText(s);
		mCarrierLogoText.setSummary(s);
	}

	private void removeUnsupported()
	{
		Resources localResources = getResources();
		PreferenceScreen ps = getPreferenceScreen();
		if (!localResources.getBoolean(R.bool.clock_tap_action))
			ps.removePreference(this.mClockTapPressPref);
		if (!localResources.getBoolean(R.bool.clock_long_press_action))
			ps.removePreference(this.mClockLongPressPref);
		if (!localResources.getBoolean(R.bool.status_bar_transparence))
			ps.removePreference(this.mStatusBarTrans);
		if (!localResources.getBoolean(R.bool.show_network_speed))
			ps.removePreference(this.mNetworkSpeedPref);
		if (!localResources.getBoolean(R.bool.network_speed_update_interval))
			ps.removePreference(this.mNetworkSpeedInterval);
		if (!localResources.getBoolean(R.bool.custom_carrier_logo))
		{
			ps.removePreference(this.mShowCarrierLogo);
			ps.removePreference(this.mCarrierLogoText);
		}
		if (!localResources.getBoolean(R.bool.status_bar_swipe_brightness))
			ps.removePreference(this.mStatusBarSwipeBrightness);
	}

	@Override
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.status_bar);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		com = new Common(getContentResolver());
		init();
		initListener();
		removeUnsupported();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if (preference == this.mClockTapPressPref
				|| preference == this.mClockLongPressPref
				|| preference == this.mNetworkSpeedInterval
				|| preference == this.mNotiTapPressPref
				|| preference == this.mNotiLongPressPref)
		{
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), newValue.toString());
		}

		if (preference == this.mNetworkSpeedPref
				|| preference == this.mShowCarrierLogo
				|| preference == this.mStatusBarSwipeBrightness)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}

		if (preference == this.mStatusBarSwipeBrightness)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}

		if (preference == this.mCarrierLogoText)
		{
			String str1 = newValue.toString();
			if ((str1 == null) || (TextUtils.isEmpty(str1)))
			{
				str1 = "";
				this.mCarrierLogoText.setText(str1);
			}

			this.mCarrierLogoText.setSummary(str1);
			SettingsUtils.setPreferenceString(getContentResolver(),
					preference.getKey(), str1);
		}

		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String p)
	{
		com.setSummary(p, this.mNetworkSpeedInterval);
		com.setSummary(p, this.mClockTapPressPref);
		com.setSummary(p, this.mClockLongPressPref);
		com.setSummary(p, this.mNotiTapPressPref);
		com.setSummary(p, this.mNotiLongPressPref);
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
