package com.nth.settings.utils;

import java.io.DataOutputStream;

import com.nth.settings.R;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;

public class SettingsUtils
{
	public static void confirmAndExec(Context ct, final String str, String s2)
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(ct);
		ad.setTitle(s2);
		ad.setNegativeButton(ct.getString(R.string.cancel), null);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int p)
			{
				SettingsUtils.execCommand(str);
			}
		});
		ad.show();
	}

	public static String execCommand(String s)
	{
		String str = "";
		try
		{
			Process localProcess = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(
					localProcess.getOutputStream());
			dos.writeBytes(s + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			dos.close();
			localProcess.waitFor();
		}
		catch (Exception ex)
		{
			str = ex.getMessage();
		}

		return str;
	}

	public static int getPreferenceInt(ContentResolver cr, String s, int i)
	{
		return Settings.System.getInt(cr, s, i);
	}

	public static String getPreferenceString(ContentResolver cr, String s)
	{
		return Settings.System.getString(cr, s);
	}

	public static void setPreferenceInt(ContentResolver cr, String s, int i)
	{
		Settings.System.putInt(cr, s, i);
	}

	public static void setPreferenceInt(ContentResolver cr, String s, String s2)
	{
		Settings.System.putInt(cr, s, Integer.parseInt(s2));
	}

	public static void setPreferenceString(ContentResolver cr, String s,
			String s2)
	{
		Settings.System.putString(cr, s, s2);
	}
}
