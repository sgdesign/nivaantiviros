package com.mobilesecurity.util;

import android.content.pm.IPackageDataObserver.Stub;

class CachePackageDataObserver extends Stub {
    CachePackageDataObserver() {
    }

    public void onRemoveCompleted(String packageName, boolean succeeded) {
    }
}
