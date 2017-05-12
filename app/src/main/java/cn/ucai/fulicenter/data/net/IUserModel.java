package cn.ucai.fulicenter.data.net;

import android.content.Context;

import java.io.File;

/**
 * Created by clawpo on 2017/5/10.
 */

public interface IUserModel {
    void register(Context context, String username, String usernick, String password,
                  OnCompleteListener<String> listener);
    void login(Context context,String username,String password,OnCompleteListener<String> listener);
    void updateNick(Context context,String username,String nickname,OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, String avatarType, File file,
                      OnCompleteListener<String> listener);
}
