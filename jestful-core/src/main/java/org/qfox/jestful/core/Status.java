package org.qfox.jestful.core;

public interface Status {

    boolean isSuccess();

    Protocol getProtocol();

    int getCode();

    String getReason();

    String toString();

}
