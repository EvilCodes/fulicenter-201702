package cn.ucai.fulicenter.data.net;

import android.content.Context;

import cn.ucai.fulicenter.data.bean.User;

/**
 * Created by clawpo on 2017/5/10.
 */

public interface IUserModel {
    void register(Context context, String username, String usernick, String password,
                  OnCompleteListener<User> listener);
    void login(Context context,String username,String password,OnCompleteListener<User> listener);
}
