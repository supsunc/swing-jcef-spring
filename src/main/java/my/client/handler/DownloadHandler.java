package my.client.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.handler.CefDownloadHandlerAdapter;

public class DownloadHandler extends CefDownloadHandlerAdapter {

    @Override
    public void onBeforeDownload(CefBrowser browser, CefDownloadItem item, String fileName, CefBeforeDownloadCallback callback) {
        callback.Continue(fileName, true);
    }

    @Override
    public void onDownloadUpdated(CefBrowser browser, CefDownloadItem item, CefDownloadItemCallback callback) {
        if (item.isInProgress() && !item.isCanceled() && !item.isComplete()) {
            int percent = item.getPercentComplete() == -1 ? 0 : item.getPercentComplete();
            StringBuilder sb = new StringBuilder();
            if (browser.getURL().contains("en-us"))
                sb.append("It is downloading, ").append(percent).append("% completed.");
            else
                sb.append("正在下载，完成度：").append(percent).append("%。");
            browser.executeJavaScript("$alertDownload.show(); pAlertDownload.innerText='" + sb + "';", item.getURL(), 1);
        } else {
            browser.executeJavaScript("setTimeout(() => $alertDownload.fadeOut('fast'), 1000);", item.getURL(), 2);
        }
    }
}