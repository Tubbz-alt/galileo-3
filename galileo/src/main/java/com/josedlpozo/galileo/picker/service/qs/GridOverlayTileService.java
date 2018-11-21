/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.picker.service.qs;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.ui.DesignerTools;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;

public class GridOverlayTileService extends TileService {

    public GridOverlayTileService() {
        super();
    }

    @Override public void onTileAdded() {
        super.onTileAdded();
    }

    @Override public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override public void onStartListening() {
        super.onStartListening();
        boolean isOn = DesignerTools.INSTANCE.gridOverlayOn(this);
        updateTile(isOn);
    }

    @Override public void onStopListening() {
        super.onStopListening();
    }

    @Override public void onClick() {
        super.onClick();
        boolean isOn = DesignerTools.INSTANCE.gridOverlayOn(this);
        if (isOn) {
            LaunchUtils.cancelGridOverlay(this);
        } else {
            LaunchUtils.launchGridOverlay(this);
        }
        updateTile(!isOn);
    }

    private void updateTile(boolean isOn) {
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(this, isOn ? R.drawable.ic_qs_grid_on : R.drawable.ic_qs_grid_off));
        tile.updateTile();
    }
}
