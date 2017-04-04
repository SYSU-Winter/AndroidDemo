package sysu.lwt.webtechnique;

/**
 * Created by 12136 on 2017/4/4.
 */

public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
