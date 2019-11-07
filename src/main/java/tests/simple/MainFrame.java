// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.simple;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;

/**
 * This is a simple example application using JCEF.
 * It displays a JFrame with a JTextField at its top and a CefBrowser in its
 * center. The JTextField is used to enter and assign an URL to the browser UI.
 * No additional handlers or callbacks are used in this example.
 * <p>
 * The number of used JCEF classes is reduced (nearly) to its minimum and should
 * assist you to get familiar with JCEF.
 * <p>
 * For a more feature complete example have also a look onto the example code
 * within the package "tests.detailed".
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = -5570653778104813836L;
    private final JTextField address_;
    private final CefApp cefApp_;
    private final CefClient client_;
    private final CefBrowser browser_;
    private final Component browerUI_;
    private boolean browserFocus_ = true;

    /**
     * To display a simple browser window, it suffices completely to create an
     * instance of the class CefBrowser and to assign its UI component to your
     * application (e.g. to your content pane).
     * 要显示一个简单的浏览器窗口，只需创建一个类 CefBrowser 的实例并将其ui组件分配给应用程序（例如，分配给内容窗格）就足够了。
     * <p>
     * But to be more verbose, this CTOR keeps an instance of each object on the
     * way to the browser UI.
     * 但是，为了更详细，这个 CTOR 将每个对象的一个实例保存在通往浏览器 UI 的路上。
     */
    private MainFrame(String startURL, boolean useOSR, boolean isTransparent) {
        // (1) The entry point to JCEF is always the class CefApp. There is only one
        //     instance per application and therefore you have to call the method
        //     "getInstance()" instead of a CTOR.
        //     JCEF 的入口点总是类 CefApp。每个应用程序只有一个实例，因此必须调用方法"getInstance()"而不是一个 CTOR。

        //     CefApp is responsible for the global CEF context. It loads all
        //     required native libraries, initializes CEF accordingly, starts a
        //     background task to handle CEF's message loop and takes care of
        //     shutting down CEF after disposing it.
        //     CefApp 负责全局 CEF 上下文。它加载所有必需的本地库，相应地初始化 CEF，启动后台任务来处理 CEF 的消息循环，并在处理完后关闭 CEF。
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(CefAppState state) {
                // Shutdown the app if the native CEF part is terminated
                // 如果本机 CEF 部分终止，则关闭应用程序
                if (state == CefAppState.TERMINATED) System.exit(0);
            }
        });
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        cefApp_ = CefApp.getInstance(settings);

        // (2) JCEF can handle one to many browser instances simultaneous. These
        //     browser instances are logically grouped together by an instance of
        //     the class CefClient. In your application you can create one to many
        //     instances of CefClient with one to many CefBrowser instances per
        //     client. To get an instance of CefClient you have to use the method
        //     "createClient()" of your CefApp instance. Calling an CTOR of
        //     CefClient is not supported.
        //     JCEF 可以同时处理一到多个浏览器实例。这些浏览器实例按类 CefClient 的实例在逻辑上分组在一起。
        //     在您的应用程序中，您可以创建一到多个 CefClient 实例，每个客户端有一到多个 CefBrowser 实例。
        //     要获取 CefClient 的实例，必须使用 CefApp 实例的方法"createClient()"。不支持调用 CefClient 的 CTOR。
        //
        //     CefClient is a connector to all possible events which come from the
        //     CefBrowser instances. Those events could be simple things like the
        //     change of the browser title or more complex ones like context menu
        //     events. By assigning handlers to CefClient you can control the
        //     behavior of the browser. See tests.detailed.MainFrame for an example
        //     of how to use these handlers.
        //     CefClient 是连接来自 CefBrowser 实例的所有可能事件的连接器。
        //     这些事件可以是诸如更改浏览器标题之类的简单事件，也可以是诸如上下文菜单事件之类的更复杂事件。
        //     通过将处理程序分配给 CefClient，您可以控制浏览器的行为。有关如何使用这些处理程序的示例，请参见 tests.detailed.MainFrame。
        client_ = cefApp_.createClient();

        // (3) One CefBrowser instance is responsible to control what you'll see on
        //     the UI component of the instance. It can be displayed off-screen
        //     rendered or windowed rendered. To get an instance of CefBrowser you
        //     have to call the method "createBrowser()" of your CefClient
        //     instances.
        //     一个 CefBrowser 实例负责控制在该实例的 UI 组件上看到的内容。
        //     它可以显示屏幕外渲染或窗口渲染。要获取 CefBrowser 实例，必须调用 CefClient 实例的方法"createBrowser()"。
        //
        //     CefBrowser has methods like "goBack()", "goForward()", "loadURL()",
        //     and many more which are used to control the behavior of the displayed
        //     content. The UI is held within a UI-Compontent which can be accessed
        //     by calling the method "getUIComponent()" on the instance of CefBrowser.
        //     The UI component is inherited from a java.awt.Component and therefore
        //     it can be embedded into any AWT UI.
        //     CefBrowser 有"goBack()"、"goForward()"、"loadURL()"等方法，这些方法用于控制显示内容的行为。
        //     该 UI 保存在 UI 组件中，可以通过调用 CefBrowser 实例上的方法"getUIComponent()"来访问该 UI 组件。
        //     UI 组件继承自java.awt.Component，因此可以嵌入到任何awt ui中。
        browser_ = client_.createBrowser(startURL, useOSR, isTransparent);
        browerUI_ = browser_.getUIComponent();

        // (4) For this minimal browser, we need only a text field to enter an URL
        //     we want to navigate to and a CefBrowser window to display the content
        //     of the URL. To respond to the input of the user, we're registering an
        //     anonymous ActionListener. This listener is performed each time the
        //     user presses the "ENTER" key within the address field.
        //     If this happens, the entered value is passed to the CefBrowser
        //     instance to be loaded as URL.
        //     对于这个最小的浏览器，我们只需要一个文本字段来输入我们要导航到的 url，以及一个 CefBrowser 窗口来显示 url 的内容。
        //     为了响应用户的输入，我们注册了一个匿名 ActionListener。每当用户在地址字段中按“回车”键时，就会执行此侦听器。
        //     如果发生这种情况，则将输入的值传递给要作为 url 加载的 CefBrowser 实例。
        address_ = new JTextField(startURL, 100);
        address_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser_.loadURL(address_.getText());
            }
        });

        // Update the address field when the browser URL changes.
        // 当浏览器 URL 更改时更新地址字段。
        client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
            @Override
            public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
                address_.setText(url);
            }
        });

        // Clear focus from the browser when the address field gains focus.
        // 当地址字段获得焦点时，从浏览器中清除焦点。
        address_.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!browserFocus_) return;
                browserFocus_ = false;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                address_.requestFocus();
            }
        });

        // Clear focus from the address field when the browser gains focus.
        // 当浏览器获得焦点时，从地址字段中清除焦点。
        client_.addFocusHandler(new CefFocusHandlerAdapter() {
            @Override
            public void onGotFocus(CefBrowser browser) {
                if (browserFocus_) return;
                browserFocus_ = true;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                browser.setFocus(true);
            }

            @Override
            public void onTakeFocus(CefBrowser browser, boolean next) {
                browserFocus_ = false;
            }
        });

        // (5) All UI components are assigned to the default content pane of this
        //     JFrame and afterwards the frame is made visible to the user.
        //     所有 UI 组件都被分配给这个 JFrame 的默认内容窗格，然后这个框架对用户可见。
        getContentPane().add(address_, BorderLayout.NORTH);
        getContentPane().add(browerUI_, BorderLayout.CENTER);
        pack();
        setSize(800, 600);
        setVisible(true);

        // (6) To take care of shutting down CEF accordingly, it's important to call
        //     the method "dispose()" of the CefApp instance if the Java
        //     application will be closed. Otherwise you'll get asserts from CEF.
        //     要相应地关闭 CEF，如果 Java 应用程序将被关闭，那么调用 CefApp 实例的方法"dispose()"非常重要。否则你会得到 CEF 的指控。
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        // Perform startup initialization on platforms that require it.
        // 在需要的平台上执行启动初始化。
        if (!CefApp.startup()) {
            System.out.println("Startup initialization failed!");

            return;
        }

        // The simple example application is created as anonymous class and points
        // to Google as the very first loaded page. Windowed rendering mode is used by
        // default. If you want to test OSR mode set |useOsr| to true and recompile.
        // 这个简单的示例应用程序被创建为匿名类，并指向 Google 作为第一个加载的页面。默认情况下使用窗口渲染模式。如果要测试OSR模式，请将 |useOsr| 设置为 true 并重新编译。
        boolean useOsr = false;
        new MainFrame("http://www.google.com", useOsr, false);
    }
}
