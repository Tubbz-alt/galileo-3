package com.josedlpozo.galileo.picker.qs;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;
import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

public class GridQuickSettingsTile {

    private static final String TAG = GridQuickSettingsTile.class.getSimpleName();
    public static final String ACTION_TOGGLE_STATE = "org.cyanogenmod.designertools.action.TOGGLE_GRID_STATE";
    public static final String ACTION_UNPUBLISH = "org.cyanogenmod.designertools.action.UNPUBLISH_GRID_TILE";
    public static final int TILE_ID = 1000;

    public static void publishGridTile(Context context) {
        publishGridTile(context, OnOffTileState.STATE_OFF);
    }

    public static void publishGridTile(Context context, int state) {
        Intent intent = new Intent(ACTION_TOGGLE_STATE);
        intent.putExtra(OnOffTileState.EXTRA_STATE, state);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int iconResId = state == OnOffTileState.STATE_OFF ? R.drawable.ic_qs_grid_off : R.drawable.ic_qs_grid_on;
        CustomTile tile =
            new CustomTile.Builder(context).setOnClickIntent(pi).setLabel(context.getString(R.string.grid_qs_tile_label)).setIcon(iconResId).build();
        CMStatusBarManager.getInstance(context).publishTile(TAG, TILE_ID, tile);
        PreferenceUtils.GridPreferences.setGridQsTileEnabled(context, true);
    }

    public static void unpublishGridTile(Context context) {
        CMStatusBarManager.getInstance(context).removeTile(TAG, TILE_ID);
        PreferenceUtils.GridPreferences.setGridQsTileEnabled(context, false);
        Intent intent = new Intent(GridQuickSettingsTile.ACTION_UNPUBLISH);
        context.sendBroadcast(intent);
    }

    public static class ClickBroadcastReceiver extends BroadcastReceiver {

        @Override public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(OnOffTileState.EXTRA_STATE, OnOffTileState.STATE_OFF);
            if (state == OnOffTileState.STATE_OFF) {
                LaunchUtils.publishGridOverlayTile(context, OnOffTileState.STATE_ON);
                LaunchUtils.launchGridOverlay(context);
            } else {
                LaunchUtils.publishGridOverlayTile(context, OnOffTileState.STATE_OFF);
                LaunchUtils.cancelGridOverlay(context);
            }
        }
    }
}
