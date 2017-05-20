package sysu.lwt.volley;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 12136 on 2017/5/20.
 */

public class CustomActivity extends Activity{

    private Button bt_gson;

    private TextView gson_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        initId();
        initView();
    }

    private void initView() {
        bt_gson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(CustomActivity.this);
                // 创建一个Gson请求
                String url = "http://www.kuaidi100.com/query?type=tiantian&postid=550577458139";
                GsonRequest<Expressage> request = new GsonRequest<>(url, Expressage.class,
                        new Response.Listener<Expressage>() {
                            @Override
                            public void onResponse(Expressage response) {
                                //List<Expressage.ExpressageData> list = response.getExpressageData();
                                gson_result.setText(getGson(response));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        gson_result.setText(error.getMessage());
                    }
                });

                // 将请求添加到请求队列中
                requestQueue.add(request);
            }
        });
    }

    private String getGson(Expressage response) {
        String result = "";
        result += "message: ";
        result += response.getMessage();
        result += "\n";
        result += "nu: ";
        result += response.getNu();
        result += "\n";
        result += "isCheck: ";
        result += response.getIsCheck();
        result += "\n";
        result += "condition: ";
        result += response.getCondition();
        result += "\n";
        result += "com: ";
        result += response.getCom();
        result += "\n";
        result += "status: ";
        result += response.getStatus();
        result += "\n";
        result += "state: ";
        result += response.getState();
        result += "\n";

        result += "data: {\n";

        List<Expressage.data> list = response.getData();
        Log.d("list的长度：", ""+list.size());
        for (Expressage.data ex : list) {
            result += "time: ";
            result += ex.getTime();
            result += "\n";
            result += "ftime: ";
            result += ex.getFtime();
            result += "\n";
            result += "content: ";
            result += ex.getContext();
            result += "\n";
            result += "location: ";
            result += ex.getLocation();
            result += "\n\n";
        }
        result += "}";
        return result;
    }

    private void initId() {
        bt_gson = (Button) findViewById(R.id.bt_Gson);
        gson_result = (TextView) findViewById(R.id.gson_result);
    }
}
