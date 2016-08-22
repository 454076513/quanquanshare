package share.quanquan.quanquanshare.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

import share.quanquan.quanquanshare.ui.MainActivity;

/**
 * http://blog.csdn.net/fenghai22/article/details/44061307
 */
public class StringRequest extends Request<String> {
    private final Response.Listener<String> mListener; //请求成功的监听...
    //根据指定的请求方式和url创建一个StringRquest对象...
    public StringRequest(int method, String url, Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(method, url, errorListener); //设置请求方式，url，以及错误监听..
        mListener = listener; //设置成功监听...
    }
    //根据指定的url来创建一个StringRequest对象，请求方式默认为GET..
    public StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }
    //这里涉及到发送响应的过程了...表示整个请求的响应已经返回...
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
    //对响应的解析过程...
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers)); //对响应数据封装，解析字符集...
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));//返回请求成功...
    }

}