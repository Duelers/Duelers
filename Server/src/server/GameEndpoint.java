package server;

import server.clientPortal.ClientPortal;
import server.clientPortal.models.message.Message;
import server.dataCenter.DataCenter;
import server.exceptions.LogicException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/game")
public class GameEndpoint {

    private static final Logger LOGGER =
            Logger.getLogger(GameEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        String id = session.getId();
        ClientPortal.getInstance().addClient(session);
        Message message = Message.makeClientIDMessage(id, id);
        ClientPortal.getInstance().sendMessage(id, message.toJson());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        ClientPortal.getInstance().addMessage(session.getId(), message);
    }

    @OnClose
    public void onClose(Session session) {
        try {
            DataCenter.getInstance().logout(session);
        } catch (LogicException e) {
            //TODO: log this once we have it set up
        }
        ClientPortal.getInstance().removeClient(session);
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}