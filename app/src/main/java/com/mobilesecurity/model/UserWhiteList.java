package com.mobilesecurity.model;

import android.content.Context;

import com.mobilesecurity.iface.IProblem;

public class UserWhiteList extends JSONDataSet<IProblem> {
    public UserWhiteList(Context context) {
        super(context, "userwhitelist.json", new ProblemFactory());
    }

    public boolean checkIfSystemPackageInList(Class<?> type) {
        for (IProblem p : getSet()) {
            if (p.getType() == IProblem.ProblemType.SystemProblem && ((SystemProblem) p).getClass() == type) {
                return true;
            }
        }
        return false;
    }
}
