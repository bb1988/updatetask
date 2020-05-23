package com.bb.updatetask;

import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

public class AppManager {
    public static List<Activity> activities = new LinkedList();

    public void a() {
        for (Activity finish : activities) {
            finish.finish();
        }
        System.exit(0);
    }
}
