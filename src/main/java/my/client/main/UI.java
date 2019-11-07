package my.client.main;

import my.client.browser.MyBrowser;
import my.client.handler.DownloadHandler;
import my.client.handler.MenuHandler;
import my.client.handler.MessageRouterHandler;
import my.client.interfaces.MyService;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefMessageRouter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class UI implements InitializingBean {
    private MyService myService;

    public UI(MyService myService) {
        this.myService = myService;
    }

    private void init() {
        EventQueue.invokeLater(() -> {
            JFrame jFrame = new JFrame("MyBrowser");
            jFrame.setMinimumSize(new Dimension(1366, 738));    // 设置最小窗口大小
            jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);    // 默认窗口全屏
            jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(jFrame.getClass().getResource("/images/icon.png")));

            if (!CefApp.startup()) {    // 初始化失败
                JLabel error = new JLabel("<html><body>&nbsp;&nbsp;&nbsp;&nbsp;在启动这个应用程序时，发生了一些错误，请关闭并重启这个应用程序。<br>There is something wrong when this APP start up, please close and restart it.</body></html>");
                error.setFont(new Font("宋体/Arial", Font.PLAIN, 28));
                error.setIcon(new ImageIcon(jFrame.getClass().getResource("/images/error.png")));
                error.setForeground(Color.red);
                error.setHorizontalAlignment(SwingConstants.CENTER);

                jFrame.getContentPane().setBackground(Color.white);
                jFrame.getContentPane().add(error, BorderLayout.CENTER);
                jFrame.setVisible(true);
                return;
            }


            MyBrowser myBrowser = new MyBrowser("https://www.baidu.com", false, false);

            CefClient client = myBrowser.getClient();
            // 绑定 MessageRouter 使前端可以执行 js 到 java 中
            CefMessageRouter cmr = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("cef", "cefCancel"));
            cmr.addHandler(new MessageRouterHandler(myService), true);
            client.addMessageRouter(cmr);
            // 绑定 ContextMenuHandler 实现右键菜单
            client.addContextMenuHandler(new MenuHandler(jFrame));
            // 绑定 DownloadHandler 实现下载功能
            client.addDownloadHandler(new DownloadHandler());

            jFrame.getContentPane().add(myBrowser.getBrowserUI(), BorderLayout.CENTER);
            jFrame.setVisible(true);

            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int i;
                    String language = "en-us";
                    if (language.equals("en-us"))
                        i = JOptionPane.showOptionDialog(null, "Do you really want to quit this software?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, "Yes");
                    else if (language.equals("zh-cn"))
                        i = JOptionPane.showOptionDialog(null, "你真的想退出这个软件吗？", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"是的", "不"}, "是的");
                    else
                        i = JOptionPane.showOptionDialog(null, "你真的想退出这个软件吗？\nDo you really want to quit this software?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"是的(Yes)", "不(No)"}, "是的(Yes)");
                    if (i == JOptionPane.YES_OPTION) {
                        myBrowser.getCefApp().dispose();
                        jFrame.dispose();
                        System.exit(0);
                    }
                }
            });
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
