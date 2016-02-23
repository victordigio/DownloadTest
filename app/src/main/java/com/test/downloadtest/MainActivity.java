package com.test.downloadtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_DOCUMENT_TREE = 1;
    public static final String URL_FILE = "http://www.publishers.org.uk/_resources/assets/attachment/full/0/2091.pdf";

    private Button explorer, download;
    private TextView path;
    private String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        explorer = (Button) findViewById(R.id.explorer);
        download = (Button) findViewById(R.id.download);
        explorer.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);

        path = (TextView) findViewById(R.id.path);

        //Default path
        currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        path.setText(currentPath);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            switch (view.getId()) {
                case R.id.explorer:
                    try {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(intent, REQUEST_CODE_DOCUMENT_TREE);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.download:
                    downloadExampleFile();
                    break;
            }
        }
    };


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DOCUMENT_TREE && resultCode == Activity.RESULT_OK && data != null) {
            // Get Uri from Storage Access Framework.
            Uri treeUri = data.getData();

            // Persist access permissions.
            getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            currentPath = FileUtil.getFullPathFromTreeUri(this, treeUri);
            path.setText(currentPath);
        }
    }

    public void downloadExampleFile(){
        FileDownloader.getImpl().create(URL_FILE).setAutoRetryTimes(2)
                .setPath(currentPath + "/file.pdf")
                .setListener(new FileDownloadListener() {

                    @Override protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("progress","bytes: " + soFarBytes);
                    }

                    @Override protected void blockComplete(BaseDownloadTask task) {

                    }

                    @Override protected void completed(BaseDownloadTask task) {
                        Toast.makeText(MainActivity.this, "Download successful", Toast.LENGTH_LONG).show();
                    }

                    @Override protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override protected void error(BaseDownloadTask task, Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override protected void warn(BaseDownloadTask task) {

                    }
                })
                .start();
    }


}
