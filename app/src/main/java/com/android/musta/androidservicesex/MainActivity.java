package com.android.musta.androidservicesex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.musta.androidservicesex.receivers.DownloadResultReceiver;
import com.android.musta.androidservicesex.receivers.Receiver;
import com.android.musta.androidservicesex.services.DownloadIntentService;

public class MainActivity extends AppCompatActivity implements Receiver {

    private ListView listView = null;
    private ArrayAdapter<String> arrayAdapter = null;
    private DownloadResultReceiver mReceiver;
    final String url = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //allow activity to show indeterminate progressbar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_items);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
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
                break;
            }
            case DownloadIntentService.STATUS_FINISHED: {
                setProgressBarIndeterminateVisibility(false);
                String[] results = resultData.getStringArray("result");
                arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, results);
                listView.setAdapter(arrayAdapter);
                break;
            }
            case DownloadIntentService.STATUS_ERROR: {
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
