package share.quanquan.quanquanshare.ui;



import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import share.quanquan.quanquanshare.R;
import share.quanquan.quanquanshare.ui.base.BaseActivity;
import share.quanquan.quanquanshare.utils.FileHelper;
import share.quanquan.quanquanshare.utils.Log;
import share.quanquan.quanquanshare.utils.TemplateUtil;

public class OpenBrowerActivity extends BaseActivity {




    private FileHelper helper;

    private TextView hasSDTextView;
    private TextView SDPathTextView;
    private TextView FILESpathTextView;
    private TextView createFileTextView;
    private TextView readFileTextView;
    private TextView deleteFileTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_brower_activity_layout);
        FileHelper.verifyStoragePermissions(this);

        hasSDTextView = (TextView) findViewById(R.id.hasSDTextView);
        SDPathTextView = (TextView) findViewById(R.id.SDPathTextView);
        FILESpathTextView = (TextView) findViewById(R.id.FILESpathTextView);
        createFileTextView = (TextView) findViewById(R.id.createFileTextView);
        readFileTextView = (TextView) findViewById(R.id.readFileTextView);
        deleteFileTextView = (TextView) findViewById(R.id.deleteFileTextView);

        helper = new FileHelper(getApplicationContext());
        hasSDTextView.setText("SD卡是否存在:" + helper.hasSD());
        SDPathTextView.setText("SD卡路径:" + helper.getSDPATH());
        FILESpathTextView.setText("包路径:" + helper.getFILESPATH());


        final String fileName = "index.html";
        try {
            createFileTextView.setText("创建文件："
                    + helper.createSDFile(fileName).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        String template = helper.readInputStream(getResources().openRawResource(R.raw.index));
        Map<String,String> data = new HashMap<String,String>();
        data.put("content","safsafd");
        String writeData = TemplateUtil.composeMessage(template,data);

        helper.writeSDFile(writeData, fileName);

        Button browerBtn = (Button) findViewById(R.id.brower);
        browerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = "file://" + helper.getSDPATH()+ "/"+ fileName;

                Uri uri = Uri.parse(address.trim());
                Intent urlintent = new Intent(Intent.ACTION_VIEW, uri);
                if (urlintent.resolveActivity(getPackageManager()) != null) {
                    startActivity(urlintent);
                }
                choiceBrowserToVisitUrl(address);

            }
        });

        Button openJDBtn = (Button) findViewById(R.id.openJd);
        openJDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = "openapp.jdmobile://virtual?params={\"category\":\"jump\",\"des\":\"getCoupon\",\"action\":\"to\",\"url\":\"http://union.click.jd.com/jdc?d=P0VZvy\"}";

                Uri uri = Uri.parse(address.trim());
                Intent urlintent = new Intent(Intent.ACTION_VIEW, uri);
                if (urlintent.resolveActivity(getPackageManager()) != null) {
                    startActivity(urlintent);
                }
                choiceBrowserToVisitUrl(address);

            }
        });


    }

    private void choiceBrowserToVisitUrl(String url) {
        PackageManager packageMgr = getPackageManager();
        List<PackageInfo> list = packageMgr.getInstalledPackages(PackageManager.GET_ACTIVITIES);
//        Log.i("quanquanshare", list.toString());
        List<String> browserList = new ArrayList<String>();
        browserList.add("com.tencent.mtt");
        browserList.add("com.uc.browser");
//        browserList.add("com.tencent.mm");
        browserList.add("com.tencent.mtt.x86");
        browserList.add("com.opera.mini.android");
        browserList.add("mobi.mgeek.TunnyBrowser");
        browserList.add("com.skyfire.browser");
        browserList.add("com.kolbysoft.steel");

        //构建浏览器优先顺序，key:包路径，value:优先级
        Map<String,Integer> browserListLevel = new HashMap<String,Integer>();
        for (int i = 0; i < browserList.size();i++ ){
            browserListLevel.put(browserList.get(i),i);
        }

        //把支持的浏览器按照优先级存到数组中
        String[] supportBroswers = new String[browserList.size()];
        for (int i = 0; i < list.size(); i++) {
            String packageName = list.get(i).packageName;
            Integer level = browserListLevel.get(packageName);
            if(level != null){
                supportBroswers[level] = packageName;
            }
        }

        String browserPath =  "com.android.browser";
        for (String supportBroswer: supportBroswers){
            if(supportBroswer != null){
                browserPath = supportBroswer;
                break;
            }
        }
        gotoUrl(browserPath, url, packageMgr);
    }

    private void gotoUrl(String packageName, String url,
                         PackageManager packageMgr) {
        try {
            Intent intent;
            intent = packageMgr.getLaunchIntentForPackage(packageName);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            // 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
            e.printStackTrace();
        }
    }



}
