package com.mobilesecurity.iface;

public interface IFactory<T> {
    T createInstance(String str);
}
