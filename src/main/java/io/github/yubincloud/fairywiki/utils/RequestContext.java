package io.github.yubincloud.fairywiki.utils;

import java.io.Serializable;

public class RequestContext implements Serializable {

    private static final ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
