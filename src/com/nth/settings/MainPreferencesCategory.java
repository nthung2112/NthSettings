package com.nth.settings;

import com.nth.settings.utils.SettingsUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

@SuppressWarnings("deprecation")
public class MainPreferencesCategory extends PreferenceActivity
{
	private Preference mNavigationBarPref;
	private Preference mOtherSettingsHeader;
	private Preference mRebootPrefHeader;
	private Preference mRebootRecoveryPrefHeader;
	private Preference mReloadSystemUIPrefHeader;
	private Preference mSettingsInfoPrefHeader;
	private Preference mSoftRebootPrefHeader;
	private Preference mStatusBarExpandedPref;
	private Preference mStatusBarPref;

	private void removeUnsupported()
	{
		Resources resources = getResources();
		PreferenceScreen ps = getPreferenceScreen();
		PreferenceCategory mTool = (PreferenceCategory) findPreference("tools_category_pref");
		
		if (!resources.getBoolean(R.bool.button_header))
			ps.removePreference(mNavigationBarPref);
		if (!resources.getBoolean(R.bool.status_bar_header))
			ps.removePreference(mStatusBarPref);
		if (!resources.getBoolean(R.bool.status_bar_expanded_header))
			ps.removePreference(mStatusBarExpandedPref);
		if (!resources.getBoolean(R.bool.other_settings_header))
			ps.removePreference(mOtherSettingsHeader);
		if (!resources.getBoolean(R.bool.soft_reboot_pref_header))
			mTool.removePreference(mSoftRebootPrefHeader);
		if (!resources.getBoolean(R.bool.reload_systemui_pref_header))
			mTool.removePreference(mReloadSystemUIPrefHeader);
		if (!resources.getBoolean(R.bool.reboot_pref_header))
			mTool.removePreference(mRebootPrefHeader);
		if (!resources.getBoolean(R.bool.reboot_recovery_pref_header))
			mTool.removePreference(mRebootRecoveryPrefHeader);
	}

	private void init()
	{
		this.mNavigationBarPref = findPreference("nav_bar_header");
		this.mStatusBarPref = findPreference("status_bar_header");
		this.mStatusBarExpandedPref = findPreference("status_bar_expanded_header");
		this.mOtherSettingsHeader = findPreference("other_settings_header");
		this.mSoftRebootPrefHeader = findPreference("soft_reboot_pref_header");
		this.mReloadSystemUIPrefHeader = findPreference("reload_systemui_pref_header");
		this.mRebootPrefHeader = findPreference("reboot_pref_header");
		this.mRebootRecoveryPrefHeader = findPreference("reboot_recovery_pref_header");
		this.mSettingsInfoPrefHeader = findPreference("settings_info_pref");

		this.mSettingsInfoPrefHeader.setSummary("nthung2112 - lgviet.com");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_headers);
		init();
		removeUnsupported();
	}
	
	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference)
	{
		if (preference == this.mNavigationBarPref)
			startActivity(new Intent(this, HardKeys.class));

		if (preference == this.mStatusBarPref)

			startActivity(new Intent(this, StatusBar.class));

		if (preference == this.mStatusBarExpandedPref)
			startActivity(new Intent(this, StatusBarExpanded.class));

		if (preference == this.mOtherSettingsHeader)
			startActivity(new Intent(this, OtherSettings.class));

		if (preference == this.mSoftRebootPrefHeader)
			SettingsUtils.confirmAndExec(this, "softrb.sh",
					getString(R.string.soft_reboot_header) + "?");

		if (preference == this.mReloadSystemUIPrefHeader)
			SettingsUtils.confirmAndExec(this, "pkill com.android.systemui",
					getString(R.string.reload_systemui_header) + "?");
		if (preference == this.mRebootPrefHeader)
			SettingsUtils.confirmAndExec(this, "reboot",
					getString(R.string.reboot_header) + "?");

		if (preference == this.mRebootRecoveryPrefHeader)
		{
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			ad.setTitle(getString(R.string.reboot_recovery_header));
			ad.setMessage(getString(R.string.reboot_recovery_confirm_msg));
			ad.setNegativeButton(getString(R.string.no), null);
			ad.setNeutralButton(getString(R.string.init_rec),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int p)
						{
							SettingsUtils
									.execCommand("echo 1 > /data/local/tmp/.recovery_mode\nreboot");
						}
					});
			ad.setPositiveButton(getString(R.string.rec_par),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int p)
						{
							SettingsUtils.execCommand("reboot recovery");
						}
					});
			ad.show();
		}

		if (preference == this.mSettingsInfoPrefHeader)
			startActivity(new Intent("android.intent.action.VIEW",
					Uri.parse("http://www.lgviet.com")));

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
