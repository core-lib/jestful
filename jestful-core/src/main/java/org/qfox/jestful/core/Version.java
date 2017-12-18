package org.qfox.jestful.core;

public interface Version extends Comparable<Version> {

    int getMajor();

    int getMinor();

    String toString();

}
