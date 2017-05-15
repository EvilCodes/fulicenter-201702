package cn.ucai.fulicenter.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.data.bean.CollectBean;
import cn.ucai.fulicenter.data.bean.MessageBean;

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
    void loadCollectsCount(Context context, String username, OnCompleteListener<MessageBean> listener);
    void addCollect(Context context,String goodsId,String username,OnCompleteListener<MessageBean> listener);
    void removeCollect(Context context,String goodsId,String username,OnCompleteListener<MessageBean> listener);
    void isCollect(Context context,String goodsId,String username,OnCompleteListener<MessageBean> listener);
    void loadCollects(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
}
