package sysu.lwt.webtechnique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12136 on 2017/3/30.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button sendWithHttpURLConnection = (Button) findViewById(R.id.sendWithHttpURLConnection);
        Button sendWithOkHttp = (Button)findViewById(R.id.sendWithOkHttp);
        text = (TextView) findViewById(R.id.text);
        sendWithHttpURLConnection.setOnClickListener(this);
        sendWithOkHttp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendWithHttpURLConnection:
                sendRequestWithHttpURLConnection();
                break;
            case R.id.sendWithOkHttp:
                sendRequestWithOkHttp();
                break;
        }
    }

    public void sendRequestWithHttpURLConnection() {
        // 开启线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();
                    // 对获取的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    showResponse(builder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 首先创建一个OkHttp的实例
                    OkHttpClient client = new OkHttpClient();
                    // 创建一个Request对象，这是发起一条HTTP请求的前提
                    Request request = new Request.Builder()
                            .url("https://baidu.com")
                            .build();
                    // 调用OkHttpClient的newCall()方法来创建一个Call对象，并调用它的execute方法
                    // 来发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    showResponse(responseDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        // 将线程切回主线程，更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(response);
            }
        });
    }
}
