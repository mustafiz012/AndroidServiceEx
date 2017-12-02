package com.android.musta.androidservicesex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.musta.androidservicesex.interfaces.OnCustomBroadcastTriggeredAction;
import com.android.musta.androidservicesex.receivers.DownloadResultReceiver;
import com.android.musta.androidservicesex.receivers.MyPhoneReceiver;
import com.android.musta.androidservicesex.receivers.ResponsesReceiver;
import com.android.musta.androidservicesex.services.DownloadIntentService;

public class MainActivity extends AppCompatActivity implements ResponsesReceiver, OnCustomBroadcastTriggeredAction {

    final String url = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";
    private ListView listView = null;
    private ArrayAdapter<String> arrayAdapter = null;
    private DownloadResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private MyPhoneReceiver myPhoneReceiver;
    private IntentFilter powerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //allow activity to show indeterminate progressbar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        //register broadcast receiver
        myPhoneReceiver = new MyPhoneReceiver(this);
        powerFilter = new IntentFilter();
        powerFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        powerFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(myPhoneReceiver, powerFilter);
        progressDialog = new ProgressDialog(this);
        listView = findViewById(R.id.list_items);
        mReceiver = new DownloadResultReceiver(new Handler(), this);
        Intent intentService = new Intent(Intent.ACTION_SYNC, null, this, DownloadIntentService.class);
        //send optional extras to DownloadIntentService
        intentService.putExtra("url", url);
        intentService.putExtra("receiver", mReceiver);
        intentService.putExtra("requestId", 101);
        startService(intentService);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DownloadIntentService.STATUS_RUNNING: {
                setProgressBarIndeterminateVisibility(true);
                setProgressDialog("Getting data...");
                break;
            }
            case DownloadIntentService.STATUS_FINISHED: {
                dismissProgressDialog();
                setProgressBarIndeterminateVisibility(false);
                String[] results = resultData.getStringArray("result");
                arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, results);
                listView.setAdapter(arrayAdapter);
                break;
            }
            case DownloadIntentService.STATUS_ERROR: {
                dismissProgressDialog();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void setProgressDialog(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            if (!MainActivity.this.isFinishing())
                progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPowerConnected() {
        Toast.makeText(this, "Charger connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPowerDisconnected() {
        Toast.makeText(this, "Charger disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadCompleted(Bundle bundle) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(myPhoneReceiver);
        } catch (IllegalArgumentException e) {
        }
    }
}
