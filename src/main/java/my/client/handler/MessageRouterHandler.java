package my.client.handler;

import my.client.interfaces.MyService;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

public class MessageRouterHandler extends CefMessageRouterHandlerAdapter {
    private MyService myService;

    public MessageRouterHandler(MyService myService) {
        this.myService = myService;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request, boolean persistent, CefQueryCallback callback) {
        if (request.indexOf("doSomething") == 0) {
            callback.success(myService.doSomething());
            return true;
        }
        if (request.indexOf("click:") == 0) {
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(6).trim();
            callback.success(msg + " create new message(cnm)");
            return true;
        }
        if (request.indexOf("custom:") == 0) {
            String[] method = request.substring(7).trim().split("[,:\\-]");
            switch (method[0].trim()) {
                case "search":
                    callback.success("This is the result of search.");
                    break;
                case "connect":
                    System.out.println(method[1].trim());
                    callback.success("This is the result of connect.");
                    break;
                default:
                    callback.failure(404, "This is the result of failure.");
                    break;
            }
            return true;
        }
        // Not handled.
        return false;
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long query_id) {
    }
}
