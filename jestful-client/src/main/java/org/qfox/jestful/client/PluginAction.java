package org.qfox.jestful.client;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

import java.util.Collections;
import java.util.List;

/**
 * Created by yangchangpei on 17/9/30.
 */
public class PluginAction extends ActionDelegate {
    private final List<Actor> forePlugins;
    private final List<Actor> backPlugins;

    PluginAction(Action action, List<Actor> forePlugins, List<Actor> backPlugins) {
        super(action);
        this.forePlugins = Collections.unmodifiableList(forePlugins);
        this.backPlugins = Collections.unmodifiableList(backPlugins);
    }

    public List<Actor> getForePlugins() {
        return forePlugins;
    }

    public List<Actor> getBackPlugins() {
        return backPlugins;
    }
}
