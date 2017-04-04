package sysu.lwt.webtechnique;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12136 on 2017/4/4.
 */

public class HttpUtil {
    public static void sendRequestWithHttpURLConnection(final String address, final HttpCallBackListener listener) {
        // 开启线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于使用的xml网址是http而不是https，所以使用HttpURLConnection而不是HttpsURLConnection
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    int x = connection.getResponseCode(); //状态码为200表示连接成功
                    Log.d("state", "The response is: " + x);

                    InputStream in = connection.getInputStream();
                    // 对获取的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    //showResponse(builder.toString());
                    //Log.v("builder.toString()", builder.toString());
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendRequestWithOkHttp(final String address, final okhttp3.Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 首先创建一个OkHttp的实例
                OkHttpClient client = new OkHttpClient();
                // 创建一个Request对象，这是发起一条HTTP请求的前提
                Request request = new Request.Builder()
                        .url(address)
                        .build();
                client.newCall(request).enqueue(callback);
                //showResponse(responseDate);
                //parseXMLWithSAX(responseDate);
                //parseJSONWithJSONObject(responseDate);
            }
        }).start();
    }
}
