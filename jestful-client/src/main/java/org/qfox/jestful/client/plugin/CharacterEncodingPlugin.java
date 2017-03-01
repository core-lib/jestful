package org.qfox.jestful.client.plugin;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.exception.PluginConfigException;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/1.
 */
public class CharacterEncodingPlugin implements Plugin {

    public String characterEncoding = Charset.defaultCharset().name();

    @Override
    public void config(Map<String, String> arguments) throws PluginConfigException {
        Map<String, String> args = new CaseInsensitiveMap<String, String>(arguments);
        String charset = args.get("charset");
        charset = charset == null || characterEncoding.trim().length() == 0 ? args.get("characterEncoding") : charset;
        characterEncoding = charset == null || characterEncoding.trim().length() == 0 ? characterEncoding : charset;
    }

    @Override
    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        Response response = action.getResponse();
        request.setCharacterEncoding(characterEncoding);
        response.setCharacterEncoding(characterEncoding);
        return action.execute();
    }

}
