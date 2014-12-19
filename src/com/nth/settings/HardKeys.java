package com.nth.settings;

import com.nth.settings.utils.Common;
import com.nth.settings.utils.SettingsUtils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

@SuppressWarnings("deprecation")
public class HardKeys extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceChangeListener

{
	private ListPreference mBackLongPressPref;
	private ListPreference mMenuLongPressPref;
	private ListPreference mQButtonLongPressPref;
	private ListPreference mHomeDouble;
	private CheckBoxPreference mLongPressBackOnLockPref;
	private CheckBoxPreference mLongPressMenuOnLockPref;
	private CheckBoxPreference mLongPressHomeOnLockPref;
	private CheckBoxPreference mVolumeRockerWake;
	private ListPreference mHoldVolDown;
	private ListPreference mHoldVolUp;
	private Common com;

	private void init()
	{
		this.mBackLongPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("back_long_press"));
		this.mMenuLongPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("menu_long_press"));
		this.mQButtonLongPressPref = ((ListPreference) getPreferenceScreen()
				.findPreference("q_button"));
		this.mHomeDouble = ((ListPreference) getPreferenceScreen()
				.findPreference("home_double"));
		this.mVolumeRockerWake = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("volume_rocker_wake"));
		this.mLongPressMenuOnLockPref = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("long_press_menu_on_lock"));
		this.mLongPressBackOnLockPref = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("long_press_back_on_lock"));
		this.mLongPressHomeOnLockPref = ((CheckBoxPreference) getPreferenceScreen()
				.findPreference("long_press_home_on_lock"));
		this.mHoldVolUp = ((ListPreference) getPreferenceScreen()
				.findPreference("volup_long_press"));
		this.mHoldVolDown = ((ListPreference) getPreferenceScreen()
				.findPreference("voldown_long_press"));
	}
	
	private void initListener()
	{
		this.mBackLongPressPref.setOnPreferenceChangeListener(this);
		this.mMenuLongPressPref.setOnPreferenceChangeListener(this);
		this.mQButtonLongPressPref.setOnPreferenceChangeListener(this);
		this.mHomeDouble.setOnPreferenceChangeListener(this);
		this.mVolumeRockerWake.setOnPreferenceChangeListener(this);
		this.mLongPressMenuOnLockPref.setOnPreferenceChangeListener(this);
		this.mLongPressBackOnLockPref.setOnPreferenceChangeListener(this);
		this.mLongPressHomeOnLockPref.setOnPreferenceChangeListener(this);
		this.mHoldVolUp.setOnPreferenceChangeListener(this);
		this.mHoldVolDown.setOnPreferenceChangeListener(this);
	}
	
	private void initPrefs()
	{
		com.initList(this.mBackLongPressPref);
		com.initList(this.mMenuLongPressPref);
		com.initList(this.mHomeDouble);
		com.initList(this.mQButtonLongPressPref);
		// initList(this.mHoldVolDown);
		// initList(this.mHoldVolUp);

		com.initCheckbox(this.mVolumeRockerWake);
		com.initCheckbox(this.mLongPressMenuOnLockPref);
		com.initCheckbox(this.mLongPressBackOnLockPref);
		com.initCheckbox(this.mLongPressHomeOnLockPref);
	}

	private void removeUnsupported()
	{
		Resources localResources = getResources();
		PreferenceScreen ps = getPreferenceScreen();
		PreferenceCategory mCategory = (PreferenceCategory) findPreference("on_lock_screen");
		if (!localResources.getBoolean(R.bool.back_button_navi))
			ps.removePreference(this.mBackLongPressPref);
		if (!localResources.getBoolean(R.bool.menu_button_navi))
			ps.removePreference(this.mMenuLongPressPref);
		if (!localResources.getBoolean(R.bool.home_double))
			ps.removePreference(this.mHomeDouble);
		if (!localResources.getBoolean(R.bool.q_button))
			ps.removePreference(this.mQButtonLongPressPref);
		if (!localResources.getBoolean(R.bool.volume_rocker_wake))
			mCategory.removePreference(this.mVolumeRockerWake);
		if (!localResources.getBoolean(R.bool.long_press_menu_on_lock))
			mCategory.removePreference(this.mLongPressMenuOnLockPref);
		if (!localResources.getBoolean(R.bool.long_press_back_on_lock))
			mCategory.removePreference(this.mLongPressBackOnLockPref);
		if (!localResources.getBoolean(R.bool.long_press_home_on_lock))
			mCategory.removePreference(this.mLongPressHomeOnLockPref);
	}

	@Override
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.hard_keys);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		com = new Common(getContentResolver());
		init();
		initListener();
		removeUnsupported();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if (preference == this.mBackLongPressPref
				|| preference == this.mMenuLongPressPref
				|| preference == this.mHomeDouble
				|| preference == this.mQButtonLongPressPref
				|| preference == this.mHoldVolUp
				|| preference == this.mHoldVolDown)
		{
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), newValue.toString());
		}

		if (preference == this.mVolumeRockerWake
				|| preference == this.mLongPressMenuOnLockPref
				|| preference == this.mLongPressBackOnLockPref
				|| preference == this.mLongPressHomeOnLockPref)
		{
			boolean b = ((Boolean) newValue).booleanValue();
			int m = b ? 1 : 0;
			SettingsUtils.setPreferenceInt(getContentResolver(),
					preference.getKey(), m);
		}
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key)
	{
		com.setSummary(key, this.mBackLongPressPref);
		com.setSummary(key, this.mMenuLongPressPref);
		com.setSummary(key, this.mHomeDouble);
		com.setSummary(key, this.mQButtonLongPressPref);
		com.setSummary(key, this.mHoldVolUp);
		com.setSummary(key, this.mHoldVolDown);
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
