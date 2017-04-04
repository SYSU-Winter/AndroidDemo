package sysu.lwt.webtechnique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12136 on 2017/3/30.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView text;
    private String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=广州&theUserID=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button Clear = (Button) findViewById(R.id.clear);
        Button sendWithHttpURLConnection = (Button) findViewById(R.id.sendWithHttpURLConnection);
        Button sendWithOkHttp = (Button)findViewById(R.id.sendWithOkHttp);
        text = (TextView) findViewById(R.id.text);
        sendWithHttpURLConnection.setOnClickListener(this);
        sendWithOkHttp.setOnClickListener(this);
        Clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendWithHttpURLConnection:
                HttpUtil.sendRequestWithHttpURLConnection(url, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        parseXMLWithPull(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.sendWithOkHttp:
                HttpUtil.sendRequestWithOkHttp(url, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException{
                        String responseData = response.body().string();
                        parseXMLWithPull(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.clear:
                text.setText("Clear!");
                break;
        }
    }



    public void showResponse(final List<String> list) {
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

    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            ContentHandler contentHandler = new ConTentHandler();
            reader.setContentHandler(contentHandler);
            reader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONWithJSONObject(String jsonData) {

    }
}
