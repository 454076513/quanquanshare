package share.quanquan.quanquanshare.html5.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import share.quanquan.quanquanshare.R;
import share.quanquan.quanquanshare.application.FDApplication;
import share.quanquan.quanquanshare.html5.HTML5CustomWebView;
import share.quanquan.quanquanshare.utils.FileHelper;
import share.quanquan.quanquanshare.utils.TemplateUtil;

/**
 * Created by mindfreak on 16/8/21.
 */
public class TaobaoUnionWebView extends HTML5CustomWebView.AbstractWebViewClient {

    public static StringBuilder TAOBAO_CLICK = new StringBuilder();

    private HTML5CustomWebView wv;

    public TaobaoUnionWebView(HTML5CustomWebView wv) {
        wv.super();
        this.wv = wv;
    }

    /**
     * 页面加载过程中，加载资源回调的方法
     *
     * @param view
     * @param url
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        Log.i("taobaounion", "-------->onLoadResource url:" + url);
//        super.onLoadResource(view, url);
        Uri mUri = Uri.parse(url);
        try {
            if (mUri != null && mUri.getHost() != null && mUri.getHost().contains("api.m.taobao.com") && mUri.getPath() != null && mUri.getPath().contains("mtop.alimama.moon.adzone.publisherUrl")) {
//                Toast.makeText(wv.getmActivity(), url, Toast.LENGTH_SHORT).show();
                String data = mUri.getQueryParameter("data");
                String id = "";
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    id = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TAOBAO_CLICK.setLength(0);
                TAOBAO_CLICK.append("&id=").append(id);
            }
        } catch (Exception e){

            Toast.makeText(wv.getContext(),e.toString()+":"+url,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected boolean overideUrl(WebView view, String url) {
        Log.i("taobaounion", "-------->overideUrl url:" + url);
        //这边因为考虑到之前项目的问题，这边拦截的url过滤掉了zttmall://开头的地址
        //在其他项目中 大家可以根据实际情况选择不拦截任何地址，或者有选择性拦截
        try {
            if (!url.startsWith("moon://")) {
                Uri mUri = Uri.parse(url);
                List<String> browerList = new ArrayList<String>();
                browerList.add("http");
                browerList.add("https");
                browerList.add("about");
                browerList.add("javascript");
                if (browerList.contains(mUri.getScheme())) {
                    if (mUri.getHost().contains("api.m.taobao.com") && mUri.getPath().contains("mtop.alimama.moon.adzone.publisherUrl")) {
                        Toast.makeText(wv.getmActivity(), url, Toast.LENGTH_SHORT).show();
                        String id = mUri.getQueryParameter("id");
                        TAOBAO_CLICK.setLength(0);
                        TAOBAO_CLICK.append("&id=").append(id);


                    }

                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    //如果另外的应用程序WebView，我们可以进行重用
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID,
                            FDApplication.getInstance()
                                    .getApplicationContext().getPackageName());
                    try {
                        FDApplication.getInstance().startActivity(intent);
                        return true;
                    } catch (ActivityNotFoundException ex) {
                    }
                }
                return false;
            } else {//过滤内部跳转
                Uri mUri = Uri.parse(url);
                if (mUri.getScheme().contains("moon") && mUri.getHost().contains("share")) {
                    TAOBAO_CLICK.insert(0, url);
                    startBrower(TAOBAO_CLICK.toString());
//                Log.i("zttjiangqq", "-------->TAOBAO_CLICK url:" + TAOBAO_CLICK);
//                Toast.makeText(wv.getmActivity(), TAOBAO_CLICK,Toast.LENGTH_SHORT).show();

                }

                return true;
            }
        }catch (Exception e){
            Toast.makeText(wv.getContext(),e.toString()+":"+url,Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void startBrower(String content) {
        FileHelper helper = new FileHelper(wv.getContext());
        helper.verifyStoragePermissions(wv.getmActivity());
        final String fileName = "index.html";
        try {
            helper.createSDFile(fileName).getAbsolutePath();
            String template = helper.readInputStream(wv.getResources().openRawResource(R.raw.index));

            Map<String,String> data = new HashMap<String,String>();
            data.put("content",content);
            String writeData = TemplateUtil.composeMessage(template,data);

            helper.writeSDFile(writeData, fileName);
            String address = "file://" + helper.getSDPATH() + "/" + fileName;
            choiceBrowserToVisitUrl(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void choiceBrowserToVisitUrl(String url) {
        boolean existUC = false, existOpera = false, existQQ = false, existDolphin = false, existSkyfire = false, existSteel = false, existGoogle = false;
        String ucPath = "", operaPath = "", qqPath = "", dolphinPath = "", skyfirePath = "", steelPath = "", googlePath = "";
        PackageManager packageMgr = wv.getmActivity().getPackageManager();
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
            wv.getmActivity().startActivity(intent);
        } catch (Exception e) {
            // 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
            e.printStackTrace();
        }
    }

    private void doDefault(String visitUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
        wv.getmActivity().startActivity(intent);
    }
}
