package org.qfox.jestful.client.nio;

/**
 * Created by yangchangpei on 17/6/21.
 */
public class Performance {
    final int connectionTime, latency, bandwidth;

    public Performance(int connectionTime, int latency, int bandwidth) {
        this.connectionTime = connectionTime;
        this.latency = latency;
        this.bandwidth = bandwidth;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public int getLatency() {
        return latency;
    }

    public int getBandwidth() {
        return bandwidth;
    }
}
