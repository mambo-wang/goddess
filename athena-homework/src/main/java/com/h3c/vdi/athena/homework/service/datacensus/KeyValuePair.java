package com.h3c.vdi.athena.homework.service.datacensus;

import java.io.Serializable;

/**
 * Created by w14014 on 2018/10/16
 * @author w14014
 */
public class KeyValuePair<T,V> implements Serializable {

    private static final long serialVersionUID = -8958199830588404975L;
    private T key;
    private V value;

    public KeyValuePair(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public KeyValuePair() {
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
