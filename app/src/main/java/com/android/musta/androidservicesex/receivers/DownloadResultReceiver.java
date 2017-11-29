package com.android.musta.androidservicesex.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by musta on 11/29/17.
 */

public class DownloadResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    private ResponsesReceiver mResponsesReceiver;

    public DownloadResultReceiver(Handler handler, ResponsesReceiver responsesReceiver) {
        super(handler);
        this.mResponsesReceiver = responsesReceiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        //super.onReceiveResult(resultCode, resultData);
        if (mResponsesReceiver != null) {
            mResponsesReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
