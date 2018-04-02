package com.mobilesecurity.adapter;

public interface IResultsAdapterItem {

    public enum ResultsAdapterItemType {
        Header,
        AppMenace,
        SystemMenace
    }

    ResultsAdapterItemType getType();
}
