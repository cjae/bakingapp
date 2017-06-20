package com.dreammesh.app.bakingapp.app;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.dreammesh.app.bakingapp.util.CommonUtil;

import butterknife.internal.Utils;

/**
 * Created by Jedidiah on 19/06/2017.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(CommonUtil.getVideoCacheDir(this))
                .build();
    }
}
