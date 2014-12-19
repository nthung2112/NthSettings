package com.nth.settings.widget;

import com.nth.settings.R;

import android.content.Context;
import android.preference.Preference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements
		SeekBar.OnSeekBarChangeListener
{
	private TextView labelPercent;
	private SeekBar seekbar;

	public SeekBarPreference(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
	}

	private void UpdateSetting()
	{
		int i = Math.round(5 * (seekbar.getProgress() / 5));
		float f = (float) i / 100F;
		Settings.System.putFloat(getContext().getContentResolver(),
				"status_bar_transparency", f);
		labelPercent.setText(String.valueOf(i) + "%");
	}

	private void UpdateView()
	{
		float f = Settings.System.getFloat(getContext().getContentResolver(),
				"status_bar_transparency", 0.6F);
		seekbar.setProgress(Math.round(100F * f));
		int i = Math.round(5 * (seekbar.getProgress() / 5));
		labelPercent.setText(String.valueOf(i) + "%");
	}

	@Override
	protected View onCreateView(ViewGroup viewgroup)
	{
		View view = View.inflate(getContext(), R.layout.seek_bar_preference,
				null);
		seekbar = (SeekBar) view.findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(this);
		labelPercent = (TextView) view.findViewById(R.id.summary);
		UpdateView();
		return view;
	}

	public void onProgressChanged(SeekBar seekbar1, int i, boolean flag)
	{
		UpdateSetting();
	}

	public void onStartTrackingTouch(SeekBar seekbar1)
	{
	}

	public void onStopTrackingTouch(SeekBar seekbar1)
	{
	}
}