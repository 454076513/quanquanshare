package share.quanquan.quanquanshare.ui;



import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import share.quanquan.quanquanshare.R;
import share.quanquan.quanquanshare.ui.base.BaseActivity;
import share.quanquan.quanquanshare.utils.FileHelper;
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
        boolean existUC = false, existOpera = false, existQQ = false, existDolphin = false, existSkyfire = false, existSteel = false, existGoogle = false;
        String ucPath = "", operaPath = "", qqPath = "", dolphinPath = "", skyfirePath = "", steelPath = "", googlePath = "";
        PackageManager packageMgr = getPackageManager();
        List<PackageInfo> list = packageMgr.getInstalledPackages(0);
        for (int i = 0; i < list.size(); i++) {
            PackageInfo info = list.get(i);
            String temp = info.packageName;
            if (temp.equals("com.uc.browser")) {
                // 存在UC
                ucPath = temp;
                existUC = true;
            } else if (temp.equals("com.tencent.mtt")) {
                // 存在QQ
                qqPath = temp;
                existQQ = true;
            } else if (temp.equals("com.opera.mini.android")) {
                // 存在Opera
                operaPath = temp;
                existOpera = true;
            } else if (temp.equals("mobi.mgeek.TunnyBrowser")) {
                dolphinPath = temp;
                existDolphin = true;
            } else if (temp.equals("com.skyfire.browser")) {
                skyfirePath = temp;
                existSkyfire = true;
            } else if (temp.equals("com.kolbysoft.steel")) {
                steelPath = temp;
                existSteel = true;
            } else if (temp.equals("com.android.browser")) {
                // 存在GoogleBroser
                googlePath = temp;
                existGoogle = true;
            }
        }
        if (existUC) {
            gotoUrl(ucPath, url, packageMgr);
        } else if (existOpera) {
            gotoUrl(operaPath, url, packageMgr);
        } else if (existQQ) {
            gotoUrl(qqPath, url, packageMgr);
        } else if (existDolphin) {
            gotoUrl(dolphinPath, url, packageMgr);
        } else if (existSkyfire) {
            gotoUrl(skyfirePath, url, packageMgr);
        } else if (existSteel) {
            gotoUrl(steelPath, url, packageMgr);
        } else if (existGoogle) {
            gotoUrl(googlePath, url, packageMgr);
        } else {
            doDefault(url);
        }
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

    private void doDefault(String visitUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
        startActivity(intent);
    }



}
