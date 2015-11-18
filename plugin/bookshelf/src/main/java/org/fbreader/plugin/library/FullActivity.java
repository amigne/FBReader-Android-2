package org.fbreader.plugin.library;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import org.fbreader.common.android.FBActivity;

public abstract class FullActivity extends FBActivity {
	private volatile int myThemeId;
	private volatile boolean myThemeIsDark;

	@Override
	protected void onPreCreate() {
		super.onPreCreate();
		myThemeId = ActivityUtil.setup(this, false);
		myThemeIsDark =
			getTheme().obtainStyledAttributes(new int[] {R.attr.isThemeDark}).getBoolean(0, false);
	}

	public final boolean applyTheme() {
		if (myThemeId != ActivityUtil.currentThemeId(this, false)) {
			finish();
			startActivity(new Intent(this, getClass()));
			return true;
		}
		return false;
	}

	// fix for NPE in button menu call on LG devices
	@Override
	public boolean onKeyDown(int code, KeyEvent event) {
		if (code == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
			return true;
		} else {
			return super.onKeyDown(code, event);
		}
	}

	@Override
	public boolean onKeyUp(int code, KeyEvent event) {
		if (code == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
			openOptionsMenu();
			return true;
		} else {
			return super.onKeyUp(code, event);
		}
	}
}