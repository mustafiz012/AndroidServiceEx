package com.android.musta.androidservicesex.interfaces;

import android.os.Bundle;

/**
 * Created by musta on 12/2/17.
 */

public interface OnCustomBroadcastTriggeredAction {
    void onPowerConnected();

    void onPowerDisconnected();

    void onDownloadCompleted(Bundle bundle);
}
