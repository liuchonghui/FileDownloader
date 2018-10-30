package tools.android.filedownloader;

import android.content.Context;

public abstract class DownloadAdatper implements DownloadListener {

    @Override
    public String getReleaseCode() {
        return String.valueOf(hashCode());
    }

    @Override
    public boolean checkInitialized(Context ctx, String url) {
        return false;
    }

    @Override
    public void onDownloadStart(String url) {

    }

    @Override
    public void onDownloadCancel(String url) {

    }

    @Override
    public void onDownloadProgress(String url, int progress) {

    }

    @Override
    public void onDownloadSuccess(String url, String path) {

    }

    @Override
    public void onDownloadFailure(String url, String message) {

    }

    @Override
    public void onDownloadClear(boolean success, String url, String path) {

    }
}
