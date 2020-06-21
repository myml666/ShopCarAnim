package com.itfitness.shopcaranim;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itfitness.shopcaranim.manager.AnimManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textViewNum;
    private ImageView imageViewShopCar;
    private BaseQuickAdapter<String, BaseViewHolder> baseQuickAdapter;
    private int num = 0;
    private AnimManager animManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
    }

    private void initDatas() {
        ArrayList<String> datas = new ArrayList<>();
        datas.add("http://img3.imgtn.bdimg.com/it/u=2512146983,438964828&fm=26&gp=0.jpg");
        datas.add("http://img5.imgtn.bdimg.com/it/u=183198684,3948938447&fm=26&gp=0.jpg");
        datas.add("http://img3.imgtn.bdimg.com/it/u=279137391,3224557512&fm=26&gp=0.jpg");
        datas.add("http://img5.imgtn.bdimg.com/it/u=3108686514,2060168835&fm=26&gp=0.jpg");
        datas.add("http://img2.imgtn.bdimg.com/it/u=2882349646,2424630344&fm=26&gp=0.jpg");
        datas.add("http://img1.imgtn.bdimg.com/it/u=1094375344,1112487051&fm=26&gp=0.jpg");
        baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_shocar,datas) {
            @Override
            protected void convert(BaseViewHolder helper, final String item) {
                final ImageView imageViewIcon = helper.getView(R.id.item_shocar_img_icon);
                Glide.with(mContext).load(item).into(imageViewIcon);
                final ImageView imageViewAdd = helper.getView(R.id.item_shocar_img_add);
                imageViewAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        num++;
                        showAddShopCarAnim(imageViewIcon,item);
//                        showAddShopCarAnim(imageViewAdd,item);
                    }
                });
            }
        };
        recyclerView.setAdapter(baseQuickAdapter);
    }
    public void showAddShopCarAnim(View startView, String animImgUrl){
        if(startView!=null && !TextUtils.isEmpty(animImgUrl)){
            animManager = new AnimManager.Builder()
                    .with(this)
                    .animModule(AnimManager.AnimModule.BIG_CIRCLE)//图片的动画模式，小的或者大的（仿每日优鲜）
                    .startView(startView)//开始位置的控件
                    .endView(imageViewShopCar)//结束位置的控件
                    .listener(new AnimManager.AnimListener() {
                        @Override
                        public void setAnimBegin(AnimManager a) {

                        }

                        @Override
                        public void setAnimEnd(AnimManager a) {
                            //购物车回弹动画
                            TranslateAnimation anim = new TranslateAnimation(0, 0, 20, 0);
                            anim.setInterpolator(new BounceInterpolator());
                            anim.setDuration(700);
                            imageViewShopCar.startAnimation(anim);
                            textViewNum.setText(num+"");
                        }
                    })
                    .imageUrl(animImgUrl)
                    .build();
            animManager.startAnim();
        }
    }
    private void initView() {
        recyclerView = findViewById(R.id.rv);
        textViewNum = findViewById(R.id.tv_num);
        imageViewShopCar = findViewById(R.id.img_shopcar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
