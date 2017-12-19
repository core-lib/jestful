package org.qfox.jestful.client.nio;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;

public class Jdk1_7NioOptions implements NioOptions {
    private Boolean so_broadcast;
    private Boolean so_keepalive;
    private Integer so_sndbuf;
    private Integer so_rcvbuf;
    private Boolean so_reuseaddr;
    private Integer so_linger;
    private Integer ip_tos;
    private NetworkInterface ip_multicast_if;
    private Integer ip_multicast_ttl;
    private Boolean ip_multicast_loop;
    private Boolean tcp_nodelay;

    protected Jdk1_7NioOptions() {
    }

    public static Jdk1_7NioOptions create() {
        return new Jdk1_7NioOptions();
    }

    @Override
    public void config(SocketChannel channel) throws IOException {
        if (so_broadcast != null) channel.setOption(StandardSocketOptions.SO_BROADCAST, so_broadcast);
        if (so_keepalive != null) channel.setOption(StandardSocketOptions.SO_KEEPALIVE, so_keepalive);
        if (so_sndbuf != null) channel.setOption(StandardSocketOptions.SO_SNDBUF, so_sndbuf);
        if (so_rcvbuf != null) channel.setOption(StandardSocketOptions.SO_RCVBUF, so_rcvbuf);
        if (so_reuseaddr != null) channel.setOption(StandardSocketOptions.SO_REUSEADDR, so_reuseaddr);
        if (so_linger != null) channel.setOption(StandardSocketOptions.SO_LINGER, so_linger);
        if (ip_tos != null) channel.setOption(StandardSocketOptions.IP_TOS, ip_tos);
        if (ip_multicast_if != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ip_multicast_if);
        if (ip_multicast_ttl != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, ip_multicast_ttl);
        if (ip_multicast_loop != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, ip_multicast_loop);
        if (tcp_nodelay != null) channel.setOption(StandardSocketOptions.TCP_NODELAY, tcp_nodelay);
    }

    public Boolean getSo_broadcast() {
        return so_broadcast;
    }

    public Jdk1_7NioOptions setSo_broadcast(boolean so_broadcast) {
        this.so_broadcast = so_broadcast;
        return this;
    }

    public Boolean getSo_keepalive() {
        return so_keepalive;
    }

    public Jdk1_7NioOptions setSo_keepalive(boolean so_keepalive) {
        this.so_keepalive = so_keepalive;
        return this;
    }

    public Integer getSo_sndbuf() {
        return so_sndbuf;
    }

    public Jdk1_7NioOptions setSo_sndbuf(int so_sndbuf) {
        this.so_sndbuf = so_sndbuf;
        return this;
    }

    public Integer getSo_rcvbuf() {
        return so_rcvbuf;
    }

    public Jdk1_7NioOptions setSo_rcvbuf(int so_rcvbuf) {
        this.so_rcvbuf = so_rcvbuf;
        return this;
    }

    public Boolean getSo_reuseaddr() {
        return so_reuseaddr;
    }

    public Jdk1_7NioOptions setSo_reuseaddr(boolean so_reuseaddr) {
        this.so_reuseaddr = so_reuseaddr;
        return this;
    }

    public Integer getSo_linger() {
        return so_linger;
    }

    public Jdk1_7NioOptions setSo_linger(int so_linger) {
        this.so_linger = so_linger;
        return this;
    }

    public Integer getIp_tos() {
        return ip_tos;
    }

    public Jdk1_7NioOptions setIp_tos(int ip_tos) {
        this.ip_tos = ip_tos;
        return this;
    }

    public NetworkInterface getIp_multicast_if() {
        return ip_multicast_if;
    }

    public Jdk1_7NioOptions setIp_multicast_if(NetworkInterface ip_multicast_if) {
        this.ip_multicast_if = ip_multicast_if;
        return this;
    }

    public Integer getIp_multicast_ttl() {
        return ip_multicast_ttl;
    }

    public Jdk1_7NioOptions setIp_multicast_ttl(int ip_multicast_ttl) {
        this.ip_multicast_ttl = ip_multicast_ttl;
        return this;
    }

    public Boolean getIp_multicast_loop() {
        return ip_multicast_loop;
    }

    public Jdk1_7NioOptions setIp_multicast_loop(boolean ip_multicast_loop) {
        this.ip_multicast_loop = ip_multicast_loop;
        return this;
    }

    public Boolean getTcp_nodelay() {
        return tcp_nodelay;
    }

    public Jdk1_7NioOptions setTcp_nodelay(boolean tcp_nodelay) {
        this.tcp_nodelay = tcp_nodelay;
        return this;
    }
}
