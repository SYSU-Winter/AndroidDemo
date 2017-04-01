package sysu.lwt.webtechnique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                    URL url = new URL("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=广州&theUserID=");
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
                    //showResponse(builder.toString());
                    parseXMLWithPull(builder.toString());
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
                            .url("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=广州&theUserID=")
                            .build();
                    // 调用OkHttpClient的newCall()方法来创建一个Call对象，并调用它的execute方法
                    // 来发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    //showResponse(responseDate);
                    parseXMLWithPull(responseDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final List<String> list) {
        // 将线程切回主线程，更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                for (int i = 0; i < list.size(); i++) {
                    result += list.get(i);
                    result += "\n";
                }
                text.setText(result);
            }
        });
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            // 首先是创建一个XmlPullParseFactory的实例
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            // 借助这个实例得到XmlPullParser对象
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            // 调用XmlPullParser的setInput()方法将服务器返回的XML数据设置进去，然后就可以解析了
            xmlPullParser.setInput(new StringReader(xmlData));

            // 通过getEventType()得到当前的解析事件，然后在一个while循环中不断的进行解析
            // 如果当前的解析事件不等于XmlPullParser。END_DOCUMENT, 说明解析工作还没有完成
            // 调用next()方法获取下一个解析事件
            int eventType = xmlPullParser.getEventType();
            List<String> list = new ArrayList<>();
            // 在while循环中，通过getName()来获取当前节点的名字，如果发现节点等于string
            // 就调用nextText()来获取节点的具体内容
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 开始解析某个节点
                    case XmlPullParser.START_TAG:
                        String name = xmlPullParser.getName();
                        if ("string".equals(name)) {
                            String str = xmlPullParser.nextText();
                            list.add(str);
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            showResponse(list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
