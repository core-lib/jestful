package org.qfox.jestful.client.nio;

/**
 * Created by yangchangpei on 17/6/21.
 */
public class SoLinger {
    private static final long serialVersionUID = -6836653571630722054L;

    final boolean on;
    final int linger;

    public SoLinger(boolean on, int linger) {
        this.on = on;
        this.linger = linger;
    }

    public boolean isOn() {
        return on;
    }

    public int getLinger() {
        return linger;
    }
}
