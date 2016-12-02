package org.qfox.jestful.android;

import com.google.gson.Gson;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.core.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class GsonResponseDeserializer implements ResponseDeserializer {
    private final Gson gson = new Gson();

    public String getContentType() {
        return "application/json";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, charset);
            deserialize(action, mediaType, isr);
        } finally {
            IOUtils.close(isr);
        }
    }

    @Override
    public void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException {
        Result result = action.getResult();
        Object value = gson.fromJson(reader, result.getBody().getType());
        result.getBody().setValue(value);
    }

}
