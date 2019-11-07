package my.client.handler;

import my.client.dialog.DevToolsDialog;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.callback.CefMenuModel.MenuId;
import org.cef.handler.CefContextMenuHandlerAdapter;

import java.awt.*;

public class MenuHandler extends CefContextMenuHandlerAdapter {

    private final Frame owner;

    public MenuHandler(Frame owner) {
        this.owner = owner;
    }

    private final static int MENU_ID_SHOW_DEV_TOOLS = 10000;
//        private final static int MENU_ID_SAVE_PICTURE = 10001;
//        private final static int MENU_ID_MORE = 10001;
    @Override
    public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params, CefMenuModel model) {
        //清除菜单项
        model.clear();

//        if (params.hasImageContents() && params.getSourceUrl() != null) {
//            model.addItem(MENU_ID_SAVE_PICTURE, "save picture as...");
//            model.addSeparator();
//        }
        //剪切、复制、粘贴
//        model.addItem(MenuId.MENU_ID_COPY, "copy");
//        model.addItem(MenuId.MENU_ID_CUT, "cut");
//        model.addItem(MenuId.MENU_ID_PASTE, "paste");
//        model.setEnabled(MenuId.MENU_ID_PASTE, false);
//        model.addSeparator();
//
//        CefMenuModel more = model.addSubMenu(MENU_ID_MORE, "more");
//        more.addItem(MenuId.MENU_ID_PRINT,"print");
//        more.addItem(MenuId.MENU_ID_VIEW_SOURCE,"view source");
//        model.addSeparator();

//        model.addItem(MenuId.MENU_ID_RELOAD, "reload");
        model.addItem(MENU_ID_SHOW_DEV_TOOLS, "开发者选项");
    }

    @Override
    public boolean onContextMenuCommand(CefBrowser browser, CefFrame frame, CefContextMenuParams params, int commandId, int eventFlags) {
        switch (commandId) {
//            case MenuId.MENU_ID_RELOAD:
//                browser.reload();
//                return true;
            case MENU_ID_SHOW_DEV_TOOLS:
                // 打开开发者选项
                DevToolsDialog devToolsDlg = new DevToolsDialog(owner, "开发者选项", browser);
                devToolsDlg.setVisible(true);
                return true;
//            case MENU_ID_SAVE_PICTURE:
//                browser.startDownload(params.getSourceUrl());
//                return true;
        }
        return false;
    }
}
