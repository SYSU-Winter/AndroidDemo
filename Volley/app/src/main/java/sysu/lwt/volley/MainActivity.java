package sysu.lwt.volley;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12136 on 2017/5/16.
 */

public class MainActivity extends Activity {
    private Button bt_get;
    private Button bt_post;
    private Button bt_getJson;
    private Button bt_imageRequest;
    private Button bt_imageLoader;
    private Button bt_networkImageView;

    private Button bt_customRequest;

    private TextView result;

    private ImageView image;
    private NetworkImageView networkImageView;

    @Override
    protected void onCreate(Bundle savedInstateState) {
        super.onCreate(savedInstateState);
        setContentView(R.layout.activity_main);
        initId();
        initView();
    }

    private void initView() {
        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                // 1 创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                // 2 创建一个请求
                String url = "http://api.map.baidu.com/telematics/v3/weather?location=广州&output=json&ak=CZEcEFa11qyVma6gp3kmASpArvis3VVm";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.setText(error.getMessage());
                    }
                });

                // 3 将请求加入到请求队列中
                requestQueue.add(stringRequest);
            }
        });

        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                // 1 创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                // 2 创建一个请求
                String url = "http://www.kuaidi100.com/query";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    response = new String(response.getBytes(), "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                result.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.setText(error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()  throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "tiantian");
                        map.put("postid", "550565591088");
                        return map;
                    }
                };

                // 3 将请求添加到请求队列中
                requestQueue.add(stringRequest);
            }
        });

        bt_getJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                // 创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                // 创建一个请求
                String url = "http://www.kuaidi100.com/query";

                final Map<String, String> map = new HashMap<>();
                map.put("type", "tiantian");
                map.put("postid", "550577458139");

                final String mRequestBody = appendParameter(url, map);
                Log.d("url", mRequestBody);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                result.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.setText(error.getMessage());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    mRequestBody, PROTOCOL_CHARSET);
                            return null;
                        }
                    }
                };

                // 将请求加入到请求队列中
                requestQueue.add(jsonObjectRequest);
            }
        });

        bt_imageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                // 创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                // 创建一个图片请求
                String url = "http://img0.imgtn.bdimg.com/it/u=2365380205,39687539&fm=23&gp=0.jpg";
                ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageResource(R.mipmap.ic_launcher);
                    }
                });

                // 将图片请求添加到请求队列中
                requestQueue.add(imageRequest);
            }
        });

        bt_imageLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                // 创建请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                // 创建一个imageLoader
//                ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return null;
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//
//                    }
//                });

                ImageLoader imageLoader = new ImageLoader(requestQueue, new MyImageCache());

                // 加载图片
                image.setVisibility(View.VISIBLE);
                String url = "http://img.pconline.com.cn/images/upload/upc/tx/pcdlc/1612/07/c318/31705865_1481111787386.jpg";
                ImageLoader.ImageListener imageListener = imageLoader.getImageListener(image, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round);
                imageLoader.get(url, imageListener);

            }
        });

        bt_networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

                networkImageView.setVisibility(View.VISIBLE);
                //创建一个请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                //创建一个imageLoader
                ImageLoader imageLoader = new ImageLoader(requestQueue, new MyImageCache());

                // 默认图片和发生错误时的图片设置
                networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                networkImageView.setErrorImageResId(R.mipmap.ic_launcher_round);

                // 加载图片
                String url = "http://img0.imgtn.bdimg.com/it/u=3930903039,3445302169&fm=214&gp=0.jpg";
                networkImageView.setImageUrl(url,imageLoader);
            }

        });

        bt_customRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(intent);
            }
        });
    }

    private String appendParameter(String url, Map<String,String> params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getQuery();
    }

    private void initId() {
        bt_get = (Button) findViewById(R.id.bt_get);
        bt_post = (Button) findViewById(R.id.bt_post);
        bt_getJson = (Button) findViewById(R.id.bt_getJson);
        bt_imageRequest = (Button) findViewById(R.id.bt_imageRequest);
        bt_imageLoader = (Button) findViewById(R.id.bt_imageLoader);
        bt_networkImageView = (Button) findViewById(R.id.bt_networkImageView);

        bt_customRequest = (Button) findViewById(R.id.bt_customRequest);

        result = (TextView) findViewById(R.id.showString);

        image = (ImageView) findViewById(R.id.image);
        networkImageView = (NetworkImageView) findViewById(R.id.network_image);
    }
}
