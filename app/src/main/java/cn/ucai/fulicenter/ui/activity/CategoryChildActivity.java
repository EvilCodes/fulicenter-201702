package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

/**
 * Created by clawpo on 2017/5/9.
 */

public class CategoryChildActivity extends AppCompatActivity {
    Unbinder bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        bind = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID,I.CAT_ID);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,new NewGoodsFragment(catId))
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }
}
