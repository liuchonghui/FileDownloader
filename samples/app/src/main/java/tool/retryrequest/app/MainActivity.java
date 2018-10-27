package tool.retryrequest.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tools.android.retryrequest.GsonUtil;
import tools.android.retryrequest.Result;
import tools.android.retryrequest.RetryRequest;

public class MainActivity extends Activity {

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn1 = (Button) findViewById(R.id.btn1);
        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);
        final Button btn2 = (Button) findViewById(R.id.btn2);
        final TextView btn2ret = (TextView) findViewById(R.id.btn2_ret);

        if (mHandler == null) {
            HandlerThread ht = new HandlerThread("retryrequest-single-thread") {
                {
                    start();
                }
            };
            mHandler = new Handler(ht.getLooper());
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String url = "http://api.kuai.mvideo.xiaomi.com/api/cp/1/fstoken";
                url = "https://api.kuai.mvideo.xiaomi.com/api/video/2?noop=1&vid=j-ylCRwAVtcKLX1jUcNjUiQhkMwu0u8=";
                RetryRequest.get()
                        .setEnableLogcat(true)
                        .setLogtag("PPP")
                        .setDelayMillis(333L)
                        .request(url, new Result<FsToken>() {
                            @Override
                            public void onSuccess(FsToken fsToken) {
                                Log.d("PPP", "RetryRequest|onSuccess|" + GsonUtil.toJson(fsToken));
                            }

                            @Override
                            public void onFailure(int code) {
                                Log.d("PPP", "RetryRequest|onFailure|" + code);
                            }
                        }, FsToken.class);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}