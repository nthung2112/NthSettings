package com.nth.settings;

import com.nth.settings.utils.Common;
import com.nth.settings.utils.SettingsUtils;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

@SuppressWarnings("deprecation")
public class StatusBarExpanded extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener,
		SharedPreferences.OnSharedPreferenceChangeListener
{
	private CheckBoxPreference mBrightnessSliderHidden;
	private CheckBoxPreference mMiniToggleQSlide;
	private CheckBoxPreference mMiniToggleVolumeSlider;
	private CheckBoxPreference mShowQslideApp;
	private Common com;

	private void init()
	{
		this.mMiniToggleQSlide = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("mini_toggle_qslide"));
		this.mMiniToggleVolumeSlider = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("mini_toggle_volume_slider"));
		this.mBrightnessSliderHidden = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("brightness_slider_hidden"));
		this.mShowQslideApp = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("enable_qslide"));
	}

	private void initListener()
	{
		this.mMiniToggleQSlide.setOnPreferenceChangeListener(this);
		this.mMiniToggleVolumeSlider.setOnPreferenceChangeListener(this);
		this.mBrightnessSliderHidden.setOnPreferenceChangeListener(this);
		this.mShowQslideApp.setOnPreferenceChangeListener(this);
	}

	private void initPrefs()
	{
		com.initCheckbox(this.mMiniToggleQSlide);
		com.initCheckbox(this.mMiniToggleVolumeSlider);
		com.initCheckbox(this.mBrightnessSliderHidden);
		com.initCheckbox(this.mShowQslideApp);
	}

	private void removeUnsupported()
	{
		Resources localResources = getResources();
		PreferenceScreen ps = getPreferenceScreen();
		if (!localResources.getBoolean(R.bool.mini_toggle_qslide))
			ps.removePreference(this.mMiniToggleQSlide);
		if (!localResources.getBoolean(R.bool.mini_toggle_volume_slider))
			ps.removePreference(this.mMiniToggleVolumeSlider);
		if (!localResources.getBoolean(R.bool.brightness_slider_hidden))
			ps.removePreference(this.mBrightnessSliderHidden);
		if (!localResources.getBoolean(R.bool.show_qslide_apps))
			ps.removePreference(this.mShowQslideApp);
	}

	@Override
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.status_bar_expanded);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		com = new Common(getContentResolver());

		init();
		initListener();
		removeUnsupported();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if (preference == this.mMiniToggleQSlide
				|| preference == this.mMiniToggleVolumeSlider
				|| preference == this.mBrightnessSliderHidden
				|| preference == this.mShowQslideApp)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}

		return true;
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
	protected void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(
			SharedPreferences paramSharedPreferences, String paramString)
	{
	}
}
