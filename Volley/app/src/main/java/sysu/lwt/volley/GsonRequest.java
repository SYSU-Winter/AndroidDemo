package sysu.lwt.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by 12136 on 2017/5/20.
 */

public class GsonRequest<T> extends Request<T> {

    private Response.Listener<T> listener;
    private Gson gson;
    private Class<T> clazz;

    public GsonRequest(int methord,
                       String url,
                       Class clazz,
                       Response.Listener listener,
                       Response.ErrorListener errorListener) {
        super(methord, url, errorListener);
        this.listener = listener;
        this.clazz = clazz;
        gson = new Gson();
    }

    public GsonRequest(String url,
                       Class clazz,
                       Response.Listener listener,
                       Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //Log.d("当前的class: ", clazz.toString());
            Log.d("jsonString", jsonString);
            return Response.success(gson.fromJson(jsonString, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
