package share.quanquan.quanquanshare.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import share.quanquan.quanquanshare.R;
import share.quanquan.quanquanshare.html5.HTML5WebViewCustomAD;
import share.quanquan.quanquanshare.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button taobaoBtn = (Button) findViewById(R.id.taobaounion);
        taobaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HTML5WebViewCustomAD.class);
                intent.putExtra("url","http://h5.m.taobao.com/taokeapp/extend.html");
                intent.putExtra("title",taobaoBtn.getText());
                intent.putExtra("type",1);
                openActivityByIntent(intent);
            }
        });

        final Button jingxiangjieBtn = (Button) findViewById(R.id.jingxiangjie);
        jingxiangjieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HTML5WebViewCustomAD.class);
                intent.putExtra("url","https://qwd.jd.com");
                intent.putExtra("title",jingxiangjieBtn.getText());
                openActivityByIntent(intent);
                intent.putExtra("type",2);
            }
        });

        final Button baiduBtn = (Button) findViewById(R.id.baidu);
        baiduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HTML5WebViewCustomAD.class);
                intent.putExtra("url","https://www.baidu.com");
                intent.putExtra("title",baiduBtn.getText());
                openActivityByIntent(intent);
            }
        });

        final Button browerBtn = (Button) findViewById(R.id.brower);
        browerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OpenBrowerActivity.class);
                openActivityByIntent(intent);
            }
        });



    }
}
