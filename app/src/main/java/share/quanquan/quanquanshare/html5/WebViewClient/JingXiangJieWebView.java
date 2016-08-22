package share.quanquan.quanquanshare.html5.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import share.quanquan.quanquanshare.application.FDApplication;
import share.quanquan.quanquanshare.html5.HTML5CustomWebView;

/**
 * Created by mindfreak on 16/8/21.
 */
public class JingXiangJieWebView extends HTML5CustomWebView.AbstractWebViewClient {

    private HTML5CustomWebView wv;
    public JingXiangJieWebView(HTML5CustomWebView wv){
        wv.super();
        this.wv = wv;
    }


    @Override
    protected boolean overideUrl(WebView view, String url) {
        Log.i("zttjiangqq", "-------->shouldOverrideUrlLoading url:" + url);
        //这边因为考虑到之前项目的问题，这边拦截的url过滤掉了zttmall://开头的地址
        //在其他项目中 大家可以根据实际情况选择不拦截任何地址，或者有选择性拦截
        if (!url.startsWith("moon://")) {
            Uri mUri = Uri.parse(url);
            List<String> browerList = new ArrayList<String>();
            browerList.add("http");
            browerList.add("https");
            browerList.add("about");
            browerList.add("javascript");
            if (browerList.contains(mUri.getScheme())) {
                if(mUri.getHost().contains("api.m.taobao.com") && mUri.getPath().contains("mtop.alimama.moon.adzone.publisherUrl")){
                    Toast.makeText(wv.getmActivity(), url,Toast.LENGTH_SHORT).show();

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
        } else {
            Uri mUri = Uri.parse(url);
            if(mUri.getHost().contains("share")){
                Toast.makeText(wv.getmActivity(), url,Toast.LENGTH_SHORT).show();

            }
            return true;
        }
    }
}
