package info.androidhive.recyclerviewswipe;

/**
 * Created by ravi on 27/09/17.
 */

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import info.androidhive.recyclerviewswipe.service.FTPService;
import info.androidhive.recyclerviewswipe.service.UserServiceXML;
import info.androidhive.recyclerviewswipe.timber.DebugTree;
import info.androidhive.recyclerviewswipe.timber.FileTree;
import info.androidhive.recyclerviewswipe.timber.ReleaseTree;
import timber.log.Timber;

public class MyApplication extends Application {

    public static String appPath;
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    private static UserServiceXML userServiceXML;


    public static UserServiceXML getUserServiceXML() {
        if (userServiceXML == null) {
            userServiceXML = new UserServiceXML();
        }
         return userServiceXML;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appPath = getApplicationContext().getFilesDir().getAbsolutePath();
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
            Timber.plant(new FileTree());
        } else {
            Timber.plant(new ReleaseTree());
            Timber.plant(new FileTree());
        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
