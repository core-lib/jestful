package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Entity;
import org.qfox.jestful.client.Header;
import org.qfox.jestful.client.Message;
import org.qfox.jestful.client.exception.UnsupportedTypeException;
import org.qfox.jestful.core.*;

/**
 * HEAD 处理器
 *
 * @author Payne 646742615@qq.com
 * 2019/9/30 10:21
 */
public class HeadHandler extends AbstractHandler {

    @Override
    public void receive(Client client, Action action) throws Exception {
        Response response = action.getResponse();
        Body body = action.getResult().getBody();
        if (body.getType() == Void.TYPE) {
            boolean success = response.getResponseStatus().isSuccess();
            if (!success) super.receive(client, action);
        } else if (body.getType() == Message.class) {
            Message message = new Message(response);
            body.setValue(message);
        } else if (body.getType() == Entity.class) {
            Entity entity = new Entity(response);
            body.setValue(entity);
        } else if (body.getType() == Header.class) {
            Header header = new Header(response);
            body.setValue(header);
        } else if (body.getType() == Status.class) {
            Status status = response.getResponseStatus();
            body.setValue(status);
        } else if (body.getType() == Boolean.class || body.getType() == boolean.class) {
            Status status = response.getResponseStatus();
            body.setValue(status.isSuccess());
        } else if (body.getType() == Integer.class || body.getType() == int.class) {
            Status status = response.getResponseStatus();
            body.setValue(status.getCode());
        } else if (body.getType() == String.class) {
            Status status = response.getResponseStatus();
            body.setValue(status.getReason());
        } else {
            Restful restful = action.getRestful();
            throw new UnsupportedTypeException(action, restful.getMethod(), body.getType());
        }
    }
}
