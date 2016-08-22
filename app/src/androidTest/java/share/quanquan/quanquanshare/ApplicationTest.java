package share.quanquan.quanquanshare;

import android.app.Application;
import android.test.ApplicationTestCase;

import share.quanquan.quanquanshare.utils.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        Log.i("velocity",getContext().getApplicationContext().getFilesDir().getAbsolutePath());
    }
}