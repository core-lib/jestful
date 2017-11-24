package org.qfox.jestful.client.nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Jdk1_4NioOptions implements NioOptions {
    private Boolean keepAlive;
    private Boolean oobInline;
    private Integer connectionTime;
    private Integer latency;
    private Integer bandwidth;
    private Boolean reuseAddress;
    private Integer receiveBufferSize;
    private Integer sendBufferSize;
    private Integer soLinger;
    private Integer soTimeout;
    private Boolean tcpNoDelay;
    private Integer trafficClass;

    public static Jdk1_4NioOptions create() {
        return new Jdk1_4NioOptions();
    }

    @Override
    public void config(SocketChannel channel) throws IOException {
        Socket socket = channel.socket();
        if (socket == null) return;

        if (keepAlive != null) socket.setKeepAlive(keepAlive);
        if (oobInline != null) socket.setOOBInline(oobInline);
        if (connectionTime != null && latency != null && bandwidth != null) socket.setPerformancePreferences(connectionTime, latency, bandwidth);
        if (reuseAddress != null) socket.setReuseAddress(reuseAddress);
        if (receiveBufferSize != null) socket.setReceiveBufferSize(receiveBufferSize);
        if (sendBufferSize != null) socket.setSendBufferSize(sendBufferSize);
        if (soLinger != null) socket.setSoLinger(soLinger >= 0, soLinger);
        if (soTimeout != null) socket.setSoTimeout(soTimeout);
        if (tcpNoDelay != null) socket.setTcpNoDelay(tcpNoDelay);
        if (trafficClass != null) socket.setTrafficClass(trafficClass);
    }

    public Jdk1_4NioOptions setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public Jdk1_4NioOptions setOobInline(Boolean oobInline) {
        this.oobInline = oobInline;
        return this;
    }

    public Jdk1_4NioOptions setReuseAddress(Boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }

    public Jdk1_4NioOptions setReceiveBufferSize(Integer receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
        return this;
    }

    public Jdk1_4NioOptions setSendBufferSize(Integer sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
        return this;
    }

    public Jdk1_4NioOptions setSoLinger(Integer soLinger) {
        this.soLinger = soLinger;
        return this;
    }

    public Jdk1_4NioOptions setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
        return this;
    }

    public Jdk1_4NioOptions setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    public Jdk1_4NioOptions setTrafficClass(Integer trafficClass) {
        this.trafficClass = trafficClass;
        return this;
    }

    public Jdk1_4NioOptions setPerformance(int connectionTime, int latency, int bandwidth) {
        this.connectionTime = connectionTime;
        this.latency = latency;
        this.bandwidth = bandwidth;
        return this;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public Boolean getOobInline() {
        return oobInline;
    }

    public Integer getConnectionTime() {
        return connectionTime;
    }

    public Integer getLatency() {
        return latency;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public Boolean getReuseAddress() {
        return reuseAddress;
    }

    public Integer getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public Integer getSendBufferSize() {
        return sendBufferSize;
    }

    public Integer getSoLinger() {
        return soLinger;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public Integer getTrafficClass() {
        return trafficClass;
    }
}
