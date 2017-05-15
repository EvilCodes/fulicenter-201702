package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.AlbumsBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.PropertiesBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by clawpo on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    private static final String TAG = "GoodsDetailActivity";
    int goodsId;
    IGoodsModel model;
    @BindView(R.id.tv_good_name_english)
    TextView mTvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView mTvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView mTvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView mTvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView mSalv;
    @BindView(R.id.indicator)
    FlowIndicator mIndicator;
    @BindView(R.id.wv_good_brief)
    WebView mWvGoodBrief;
    Unbinder bind;
    User user;
    IUserModel userModel;
    boolean isCollect = false;
    @BindView(R.id.iv_good_collect)
    ImageView mIvGoodCollect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e(TAG, "goodsId=" + goodsId);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        if (mSalv != null) {
            mSalv.stopPlayLoop();
        }
    }

    private void initData() {
        if (goodsId == 0) {
            finish();
        } else {
            model = new GoodsModel();
            userModel = new UserModel();
            loadData();
        }
    }

    private void loadData() {
        model.loadGoodsDetail(GoodsDetailActivity.this, goodsId,
                new OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        L.e(TAG, "result=" + result);
                        if (result != null) {
                            showData(result);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        loadCollectStatus();
    }

    private void loadCollectStatus() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            userModel.isCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            isCollect = result != null && result.isSuccess() ? true : false;
                            updateCollectUI();
                        }

                        @Override
                        public void onError(String error) {
                            isCollect = false;
                            updateCollectUI();
                        }
                    });
        }
    }

    private void updateCollectUI() {
        mIvGoodCollect.setImageResource(isCollect?R.mipmap.bg_collect_out:R.mipmap.bg_collect_in);
    }

    private void showData(GoodsDetailsBean bean) {
        mTvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        mTvGoodName.setText(bean.getGoodsName());
        mTvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        mTvGoodPriceShop.setText(bean.getShopPrice());
        mSalv.startPlayLoop(mIndicator, getAlbumImgUrl(bean), getAlbumImgCount(bean));
        mWvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            String[] urls = new String[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
                urls[i] = imgs[i].getImgUrl();
            }
            return urls;
        }
        return null;
    }

    private int getAlbumImgCount(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            return imgs.length;
        }
        return 0;
    }

    private AlbumsBean[] getAlbumImg(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            PropertiesBean propertiesBean = bean.getProperties()[0];
            if (propertiesBean != null && propertiesBean.getAlbums() != null) {
                return propertiesBean.getAlbums();
            }
        }
        return null;
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        setResult(RESULT_OK,new Intent().putExtra(I.Goods.KEY_GOODS_ID,goodsId)
        .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));

        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent().putExtra(I.Goods.KEY_GOODS_ID,goodsId)
                .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));
        super.onBackPressed();
    }

    @OnClick(R.id.iv_good_collect)
    public void onCollectClick(){
        if (user==null){
            startActivityForResult(new Intent(GoodsDetailActivity.this,LoginActivity.class),0);
        }else{
            if (isCollect){
                userModel.removeCollect(GoodsDetailActivity.this,String.valueOf(goodsId),
                        user.getMuserName(),mListener);
            }else{
                userModel.addCollect(GoodsDetailActivity.this,String.valueOf(goodsId),
                        user.getMuserName(),mListener);
            }
        }
    }

    OnCompleteListener<MessageBean> mListener = new OnCompleteListener<MessageBean>() {
        @Override
        public void onSuccess(MessageBean result) {
            isCollect = !isCollect;
            updateCollectUI();

//            if (result!=null && result.getMsg()!=""){
                CommonUtils.showLongToast(result.getMsg());
//            }
        }

        @Override
        public void onError(String error) {
            CommonUtils.showLongToast(error);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            loadCollectStatus();
        }
    }
}
