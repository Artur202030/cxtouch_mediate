package com.cxplan.projection.mediate.message.handler.ime;

import com.cxplan.common.util.LogUtil;
import com.cxplan.projection.mediate.CXApplication;
import com.cxplan.projection.mediate.Constant;
import com.cxplan.projection.mediate.io.ClientConnection;
import com.cxplan.projection.mediate.io.DefaultIoHandlerAdapter;
import com.cxplan.projection.mediate.message.Message;
import com.cxplan.projection.mediate.message.MessageException;
import com.cxplan.projection.mediate.message.MessageUtil;
import com.cxplan.projection.mediate.message.handler.AbstractCommandHandler;

import org.apache.mina.core.session.IoSession;
/**
 * Created on 2018/5/19.
 *
 * @author kenny
 */
public class InitIMESessionCommandHandler extends AbstractCommandHandler {
    private static final String TAG = Constant.TAG_PREFIX + "initIME";

    public InitIMESessionCommandHandler() {
        super(MessageUtil.CMD_DEVICE_INIT_SESSION);
    }

    @Override
    public void process(IoSession session, Message message) throws MessageException {
        //init connection
        ClientConnection connection = (ClientConnection)session.getAttribute(DefaultIoHandlerAdapter.CLIENT_SESSION);
        if (connection == null) {
            connection = CXApplication.getInstance().getInputerConnection();
            session.setAttribute(DefaultIoHandlerAdapter.CLIENT_SESSION, connection);
        }
        connection.setSession(session);

        boolean bind = message.getParameter("bind");
        LogUtil.i(TAG, "Current state of touch IME: " + bind);
        CXApplication.isIMERunning = bind;

        Message msg = Message.createResultMessage(message);
        MessageUtil.sendMessage(session, msg);
    }
}
