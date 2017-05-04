package cn.ucai.fulicenter.data.net;

import android.content.Context;

import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.utils.OkHttpUtils;

/**
 * Created by clawpo on 2017/5/4.
 */

public interface IGoodsModel {
    void loadNewGoodsData(Context context,int catId, int pageId, int pageSize,
                          OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener);
}
