package share.quanquan.quanquanshare.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import share.quanquan.quanquanshare.R;
import share.quanquan.quanquanshare.html5.HTML5WebViewCustomAD;
import share.quanquan.quanquanshare.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private EditText urlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button taobaoBtn = (Button) findViewById(R.id.taobaounion);
        taobaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HTML5WebViewCustomAD.class);
                intent.putExtra("url", "http://h5.m.taobao.com/taokeapp/extend.html");
                intent.putExtra("title", taobaoBtn.getText());
                intent.putExtra("type", 1);
                openActivityByIntent(intent);
            }
        });

        final Button jingxiangjieBtn = (Button) findViewById(R.id.jingxiangjie);
        jingxiangjieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HTML5WebViewCustomAD.class);
                intent.putExtra("url", "https://qwd.jd.com");
                intent.putExtra("title", jingxiangjieBtn.getText());
                openActivityByIntent(intent);
                intent.putExtra("type", 2);
            }
        });

        final Button baiduBtn = (Button) findViewById(R.id.baidu);
        baiduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HTML5WebViewCustomAD.class);
                intent.putExtra("url", "https://www.baidu.com");
                intent.putExtra("title", baiduBtn.getText());
                openActivityByIntent(intent);
            }
        });

        final Button browerBtn = (Button) findViewById(R.id.brower);
        browerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenBrowerActivity.class);
                openActivityByIntent(intent);
            }
        });

        urlText = (EditText) findViewById(R.id.txtUri);

        Button goButton = (Button) findViewById(R.id.btnGo);

        //setup event handler
        urlText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                   openBrowser();
                    return true;

                }
                return false;
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser();
            }
        });
    }

    public  void openBrowser(){
        Intent intent = new Intent(MainActivity.this, HTML5WebViewCustomAD.class);
        intent.putExtra("url", urlText.getText().toString());
        openActivityByIntent(intent);
    }

}
