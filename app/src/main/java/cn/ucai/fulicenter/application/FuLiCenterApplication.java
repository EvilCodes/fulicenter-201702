package cn.ucai.fulicenter.application;

import android.app.Application;

import cn.ucai.fulicenter.data.bean.User;

/**
 * Created by clawpo on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    private User currentUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
