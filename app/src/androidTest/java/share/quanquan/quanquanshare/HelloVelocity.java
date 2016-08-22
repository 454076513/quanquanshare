package share.quanquan.quanquanshare;


import android.app.Application;
import android.test.ApplicationTestCase;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import share.quanquan.quanquanshare.utils.Log;

public class HelloVelocity extends ApplicationTestCase<Application> {

    void test(){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        ve.init();

        Template t = ve.getTemplate("hellovelocity.vm");
        VelocityContext ctx = new VelocityContext();

        ctx.put("name", "velocity");
        ctx.put("date", (new Date()).toString());

        List temp = new ArrayList();
        temp.add("1");
        temp.add("2");
        ctx.put("list", temp);

        StringWriter sw = new StringWriter();

        t.merge(ctx, sw);

        System.out.println(sw.toString());

    }

    public HelloVelocity() {

        super(Application.class);
        Log.i("velocity",getContext().getApplicationContext().getFilesDir().getAbsolutePath());
    }

    public HelloVelocity(Class<Application> applicationClass) {
        super(applicationClass);
        Log.i("velocity",getContext().getApplicationContext().getFilesDir().getAbsolutePath());

    }


}