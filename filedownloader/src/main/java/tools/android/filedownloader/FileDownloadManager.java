package tools.android.filedownloader;

import android.compact.utils.FileCompactUtil;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FileDownloadManager implements FileDownloadImpl {

    private static String TAG = "FDM";
    private static FileDownloadManager instance;
    private Handler mHandler;
    private ExecutorService downloadExecutor = null;
    private ConcurrentHashMap<String, List<DownloadListener>> pair = new ConcurrentHashMap<String, List<DownloadListener>>();
    private String cachePath = null;

    public static FileDownloadManager get() {
        if (instance == null) {
            synchronized (FileDownloadManager.class) {
                if (instance == null) {
                    instance = new FileDownloadManager();
                }
            }
        }
        return instance;
    }

    private FileDownloadManager() {
        if (mHandler == null) {
            HandlerThread postThread = new HandlerThread("file-download-post-state-thread");
            postThread.start();
            mHandler = new Handler(postThread.getLooper());
        }
    }

    public synchronized void downloadFile(Context context, String identify, String suffix, String md5,
                                          String url, DownloadListener listener) {
        if (url == null || url.length() == 0) {
            return;
        }
        DownloadListener l = listener;
        if (l == null) {
            l = new DownloadAdatper() {
            };
        }
        if (l.checkInitialized(context, url)) {
            return;
        }
        List<DownloadListener> ls = pair.get(url);
        if (ls == null || ls.size() == 0) {
            if (ls == null) {
                ls = new ArrayList();
            }
            Log.d(TAG, "PluginDownloadManager add task:" + l.hashCode() + "|" + l.getReleaseCode() + "|" + identify);
            ls.add(l);
            pair.put(url, ls);
            DownloadWorker worker = createFileDownloadWorker(url,
                    getDownloadCacheDir(context), identify, suffix, md5);
            if (downloadExecutor == null) {
                downloadExecutor = Executors.newFixedThreadPool(2, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread();
                        thread.setPriority(Thread.MAX_PRIORITY - 1);
                        return thread;
                    }
                });
            }
            // 相同的url，跑一个任务
            downloadExecutor.submit(worker);

        } else {
            Log.d(TAG, "PluginDownloadManager add task+" + l.hashCode() + "|" + l.getReleaseCode() + "|" + identify);
            ls.add(l);
            pair.put(url, ls);
            Log.d(TAG, "ls size|" + ls.size());
        }
    }

    private String getDownloadCacheDir(Context context) {
        if (cachePath == null || cachePath.length() == 0) {
            cachePath = FileCompactUtil.getTempDirPath(context);
        }
        return cachePath;
    }

    private DownloadWorker createFileDownloadWorker(String url, String cachePath,
                                                    String fileName, String suffix, String md5) {
        return new URLConnectionWorker(url, cachePath, fileName, suffix, md5);
    }

    @Override
    public void notifyDownloadStart(final String url) {
        final List<DownloadListener> ls = pair.get(url);
        if (ls != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener l : ls) {
                        try {
                            l.onDownloadStart(url);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void notifyDownloadProgress(final String url, final int progress) {
        final List<DownloadListener> ls = pair.get(url);
        if (ls != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener l : ls) {
                        try {
                            l.onDownloadProgress(url, progress);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void notifyDownloadFailure(final String url, final String message) {
        final List<DownloadListener> ls = pair.get(url);
        if (ls != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener l : ls) {
                        try {
                            l.onDownloadFailure(url, message);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void notifyDownloadSuccess(final String url, final String path) {
        final List<DownloadListener> ls = pair.get(url);
        if (ls != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener l : ls) {
                        try {
                            Log.d(TAG, "download success ls size:" + ls.size());
                            l.onDownloadSuccess(url, path);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void notifyDownloadClear(final boolean success, final String url, final String path) {
        final List<DownloadListener> ls = pair.get(url);
        if (ls != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener l : ls) {
                        try {
                            l.onDownloadClear(success, url, path);
                            Log.d(TAG, "download complete ls size:" + ls.size());
                            l.onDownloadClear(success, url, path);
                            Log.d(TAG, "PluginDownloadManager runned task " + l.hashCode() + "|" + l.getReleaseCode());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
