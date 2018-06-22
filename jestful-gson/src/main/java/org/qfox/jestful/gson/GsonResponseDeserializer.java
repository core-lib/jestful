package org.qfox.jestful.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GsonResponseDeserializer implements ResponseDeserializer {
    private volatile Gson gson = new Gson();

    public String getContentType() {
        return "application/json";
    }

    @Override
    public void setDeserializationDateFormat(DateFormat dateFormat) {
        if (dateFormat instanceof SimpleDateFormat) gson = new GsonBuilder().setDateFormat(((SimpleDateFormat) dateFormat).toPattern()).create();
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, charset);
            deserialize(action, mediaType, isr);
        } finally {
            IOKit.close(isr);
        }
    }

    @Override
    public void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException {
        Result result = action.getResult();
        Object value = gson.fromJson(reader, result.getBody().getType());
        result.getBody().setValue(value);
    }

}
