package org.qfox.jestful.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class GsonRequestSerializer implements RequestSerializer {
    private final String contentType = "application/json";
    private volatile Gson gson = new Gson();

    public String getContentType() {
        return contentType;
    }

    public void setSerializationDateFormat(DateFormat dateFormat) {
        if (dateFormat instanceof SimpleDateFormat) gson = new GsonBuilder().setDateFormat(((SimpleDateFormat) dateFormat).toPattern()).create();
    }

    public boolean supports(Action action) {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        return bodies.size() == 0 || bodies.size() == 1 && supports(bodies.get(0));
    }

    public boolean supports(Parameter parameter) {
        return true;
    }

    public void serialize(Action action, String charset, OutputStream out) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        for (Parameter parameter : parameters) {
            OutputStreamWriter osw = null;
            try {
                action.getRequest().setContentType(contentType + ";charset=" + charset);
                osw = new OutputStreamWriter(out, charset);
                gson.toJson(parameter.getValue(), osw);
                break;
            } finally {
                IOKit.close(osw);
            }
        }
    }

    public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
        OutputStreamWriter osw = null;
        try {
            Disposition disposition = new Disposition("form-data", parameter.getName());
            MediaType type = MediaType.valueOf(contentType + ";charset=" + charset);
            Multihead multihead = new Multihead(disposition, type);
            out.setNextMultihead(multihead);
            osw = new OutputStreamWriter(out, charset);
            gson.toJson(parameter.getValue(), osw);
        } finally {
            IOKit.close(osw);
        }
    }

}
