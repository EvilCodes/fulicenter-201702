package cn.ucai.fulicenter.data.net;

import android.content.Context;

/**
 * Created by clawpo on 2017/5/10.
 */

public interface IUserModel {
    void register(Context context, String username, String usernick, String password,
                  OnCompleteListener<String> listener);
    void login(Context context,String username,String password,OnCompleteListener<String> listener);
}
