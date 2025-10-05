/*
 */
package com.hhhh.group.secwealth.mktdata.starter.base.args;

import java.util.HashMap;
import java.util.Map;

public final class ArgsHolder {

    private static final ThreadLocal<Map<String, Object>> holder = new ThreadLocal<>();

    private ArgsHolder() {}

    public static void putArgs(final String key, final Object value) {
        Map<String, Object> args = ArgsHolder.holder.get();
        if (args == null) {
            args = new HashMap<>();
        }
        args.put(key, value);
        ArgsHolder.holder.set(args);
    }

    public static Object getArgs(final String key) {
        final Map<String, Object> args = ArgsHolder.holder.get();
        if (args != null) {
            return args.get(key);
        }
        return null;
    }

    public static void removeArgs() {
        ArgsHolder.holder.remove();
    }

}
