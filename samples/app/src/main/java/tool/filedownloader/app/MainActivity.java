package tool.filedownloader.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tools.android.filedownloader.DownloadAdatper;
import tools.android.filedownloader.FileDownloadManager;

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
                FileDownloadManager.get().downloadFile(view.getContext(),
                        "cp.pptv.plugin", "apk", "db9357c67b82f2da0afc1e540549296c",
                        "https://gist.github.com/liuchonghui/b9757b65748eb42548213ec7b9572116/raw/c52b33e0768e6b2f3e0732ec0f4ac9759e4e366d/1.6_25.pptv.db9357c67b82f2da0afc1e540549296c.zip",
                        new DownloadAdatper() {
                            @Override
                            public void onDownloadStart(String url) {
                                Log.d("PPP", "onDownloadStart|" + url);
                            }

                            @Override
                            public void onDownloadSuccess(String url, String path) {
                                Log.d("PPP", "onDownloadSuccess|" + url + "|" + path);
                            }

                            @Override
                            public void onDownloadFailure(String url, String message) {
                                Log.d("PPP", "onDownloadFailure|" + message);
                            }

                            @Override
                            public void onDownloadClear(boolean success, String url, String path) {
                                Log.d("PPP", "onDownloadClear|" + success);
                            }
                        });
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
