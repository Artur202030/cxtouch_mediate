package com.cxplan.projection.mediate.message.handler;

import android.content.ComponentName;
import android.content.Intent;

import com.cxplan.common.util.LogUtil;
import com.cxplan.common.util.StringUtil;
import com.cxplan.projection.mediate.CXApplication;
import com.cxplan.projection.mediate.Constant;
import com.cxplan.projection.mediate.MonkeyManager;
import com.cxplan.projection.mediate.inputer.InputerReceiver;
import com.cxplan.projection.mediate.message.Message;
import com.cxplan.projection.mediate.message.MessageException;
import com.cxplan.projection.mediate.message.MessageUtil;
import com.cxplan.projection.mediate.process.Main;

import org.apache.mina.core.session.IoSession;
/**
 * Created on 2018/5/19.
 *
 * @author kenny
 */
public class MonkeyCommandHandler extends AbstractCommandHandler {
    private static final String TAG = Constant.TAG_PREFIX + "monkey";

    public MonkeyCommandHandler() {
        super(MessageUtil.CMD_DEVICE_MONKEY);
    }

    @Override
    public void process(IoSession session, Message message) throws MessageException {
        short type = message.getParameter("type");
//        LogUtil.e(TAG, "monkey: " + type);
        switch (type) {
            case Constant.MONKEY_TYPE:
                processType(message);
                break;
            case Constant.MONKEY_MOUSE_DOWN:
                mouseDown(message);
                break;
            case Constant.MONKEY_MOUSE_MOVE:
                mouseMove(message);
                break;
            case Constant.MONKEY_MOUSE_UP:
                mouseUp(message);
                break;
            case Constant.MONKEY_SWITCH_INPUTER:
                switchInputer(message);
                break;
            case Constant.MONKEY_PRESS:
                procesPress(message);
                break;
            case Constant.MONKEY_SCROLL:
                scroll(message);
                break;
            case Constant.MONKEY_WAKE:
                wake(message);
                break;
            case Constant.MONKEY_SLEEP:
                sleep(message);
                break;
            default:
                LogUtil.e(TAG, "The monkey operation is not supported: " + type);
        }
    }

    private void processType(Message message) {
        final String text = message.getParameter("s");
        if (StringUtil.isEmpty(text)) {
            return;
        }

        if (CXApplication.isIMERunning) {//Custom IME is running.
            Intent intent = new Intent();
            intent.putExtra("s", text);
            intent.setAction("COM.CXPLAN.TOUCH.INPUTER");
            intent.setComponent(new ComponentName(Constant.packageName, InputerReceiver.class.getName()));

            try {
                MonkeyManager.sendBroadcast(intent);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage(), e);
            }
        } else {
            MonkeyManager.injectCharKeyEvent(text.toCharArray());
        }
    }

    private void switchInputer(Message message) {
        boolean value = message.getParameter("isTouchIME");
        CXApplication.isIMERunning = value;
    }
    private void procesPress(Message message) {
        int keyCode = message.getParameter("kc");
        MonkeyManager.press(keyCode);
    }

    private void scroll(Message message) {
        final float x = message.getParameter("x");
        final float y = message.getParameter("y");
        final Float vScrollValue = message.getParameter("vs");
        final Float hScrollValue = message.getParameter("hs");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                MonkeyManager.scroll(x, y, hScrollValue == null ? 0f : hScrollValue,
                        vScrollValue == null ? 0f : vScrollValue);
            }
        };

        Main.invoke(task);

    }

    private void wake(Message message) {
        try {
            MonkeyManager.turnScreenOn();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }
    private void sleep(Message message) {
        try {
            MonkeyManager.turnScreenOff();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    private void mouseDown(Message message) {
        final float x = message.getParameter("x");
        final float y = message.getParameter("y");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                MonkeyManager.mouseDown(x, y);
            }
        };

        Main.invoke(task);

    }
    private void mouseMove(Message message) {
        final float x = message.getParameter("x");
        final float y = message.getParameter("y");
        Long tmpdelta = message.getParameter("delta");
        if (tmpdelta == null) {
            tmpdelta = -1L;
        }
        final Long delta = tmpdelta;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                MonkeyManager.mouseMove(x, y, delta);
            }
        };
        Main.invoke(task);
    }
    private void mouseUp(Message message) {
        final float x = message.getParameter("x");
        final float y = message.getParameter("y");
        Long tmpdelta = message.getParameter("delta");
        if (tmpdelta == null) {
            tmpdelta = -1L;
        }
        final Long delta = tmpdelta;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                MonkeyManager.mouseUp(x, y, delta);
            }
        };
        Main.invoke(task);
    }
}
