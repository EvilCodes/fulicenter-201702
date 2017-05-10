package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

/**
 * Created by clawpo on 2017/5/10.
 */

class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                finish();
                break;
            case R.id.btn_register:
                break;
        }
    }
}
