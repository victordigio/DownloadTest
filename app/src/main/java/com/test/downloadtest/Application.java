package com.test.downloadtest;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by Victor on 23/02/2016.
 */
public class Application extends android.app.Application {

    @Override public void onCreate() {
        super.onCreate();

        FileDownloader.init(this);

    }
}
