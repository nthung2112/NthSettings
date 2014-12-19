package com.nth.settings.utils;

import android.content.ContentResolver;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

public class Common
{
	public ContentResolver cr;

	public Common(ContentResolver content)
	{
		cr = content;
	}

	public void initList(ListPreference lp)
	{
		lp.setValueIndex(SettingsUtils.getPreferenceInt(cr, lp.getKey(), 0));
		lp.setSummary(lp.getEntry());
	}

	public void initCheckbox(CheckBoxPreference cb)
	{
		int i = SettingsUtils.getPreferenceInt(cr, cb.getKey(), 0);
		boolean b = (i == 1) ? true : false;
		cb.setChecked(b);
	}

	public void initCheckbox(CheckBoxPreference cb, int k)
	{
		int i = SettingsUtils.getPreferenceInt(cr, cb.getKey(), k);
		boolean b = (i == 1) ? true : false;
		cb.setChecked(b);
	}

	public void setSummary(String key, ListPreference lp)
	{
		if (key.equals(lp.getKey()))
		{
			lp.setSummary(lp.getEntry());
		}
	}
}
