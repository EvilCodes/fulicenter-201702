package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;

/**
 * Created by clawpo on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    private static final String TAG = "GoodsDetailActivity";
    int goodsId;
    IGoodsModel model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID,0);
        L.e(TAG,"goodsId="+goodsId);
        initData();
    }

    private void initData() {
        if (goodsId==0){
            finish();
        }else{
            model = new GoodsModel();
            loadData();
        }
    }

    private void loadData() {
        model.loadGoodsDetail(GoodsDetailActivity.this, goodsId,
                new OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        L.e(TAG,"result="+result);
                        if (result!=null){

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
