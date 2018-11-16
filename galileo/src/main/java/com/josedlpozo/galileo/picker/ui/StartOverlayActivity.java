package com.josedlpozo.galileo.picker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.josedlpozo.galileo.picker.overlays.GridOverlay;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;

public class StartOverlayActivity extends Activity {
    private static final int REQUEST_OVERLAY_PERMSSISION = 42;

    public static final String EXTRA_OVERLAY_TYPE = "overlayType";
    public static final int GRID_OVERLAY = 0;
    public static final int MOCK_OVERLAY = 1;
    public static final int COLOR_PICKER_OVERLAY = 2;

    private int mOverlayType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_OVERLAY_TYPE)) {
            mOverlayType = intent.getIntExtra(EXTRA_OVERLAY_TYPE, -1);
            if (Settings.canDrawOverlays(this)) {
                startOverlayService(mOverlayType);
                finish();
            } else {
                Intent closeDialogsIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(closeDialogsIntent);
                Intent newIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                              Uri.parse("package:" + getPackageName()));
                startActivityForResult(newIntent, REQUEST_OVERLAY_PERMSSISION);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OVERLAY_PERMSSISION) {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService(mOverlayType);
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startOverlayService(int overlayType) {
        switch (overlayType) {
            case GRID_OVERLAY:
                Intent newIntent = new Intent(this, GridOverlay.class);
                this.startService(newIntent);
                PreferenceUtils.GridPreferences.setGridOverlayActive(this, true);
                PreferenceUtils.GridPreferences.setGridQsTileEnabled(this, true);
                break;
            case COLOR_PICKER_OVERLAY:
                LaunchUtils.startColorPickerOrRequestPermission(this);
                break;
        }
    }
}
