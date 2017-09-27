package org.qfox.jestful.sample.authorization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class Authorization implements Serializable {
    private static final long serialVersionUID = 5824671047419576439L;

    private Long id;
    private List<String> scopes = new ArrayList<>();
    private String note;
    private String token;
    private String url;
    private String note_url;

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
