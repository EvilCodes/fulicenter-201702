package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.BoutiqueBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;

/**
 * Created by clawpo on 2017/5/5.
 */

public class BoutiqueFragment extends Fragment {
    private static final String TAG = "BoutiqueFragment";
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IGoodsModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new GoodsModel();
        loadData();
    }

    private void loadData() {
        model.loadBoutiqueData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                L.e(TAG,"result="+result);
                if (result!=null){
                    L.e(TAG,"result.length="+result.length);
                    ArrayList<BoutiqueBean> list = ResultUtils.array2List(result);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
