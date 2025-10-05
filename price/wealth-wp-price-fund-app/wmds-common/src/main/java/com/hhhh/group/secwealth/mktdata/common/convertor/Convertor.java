/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

/**
 * The Interface Convertor.
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public interface Convertor<K, V> {

    /**
     * Do convert.
     * 
     * @param in
     *            the in
     * @return the v
     */
    public V doConvert(K in, Object... params);
}
