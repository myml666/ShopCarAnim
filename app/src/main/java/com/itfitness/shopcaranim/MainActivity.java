package com.itfitness.shopcaranim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
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
    private ImageView imgShare;
    private RelativeLayout rl_container;
    private boolean isIntercept = false;
    /** 按下时的位置控件相对屏幕左上角的位置X */
    private int startDownX;
    /** 按下时的位置控件距离屏幕左上角的位置Y */
    private int startDownY;
    /** 控件相对屏幕左上角移动的位置X */
    private int lastMoveX;
    /** 控件相对屏幕左上角移动的位置Y */
    private int lastMoveY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initDatas();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("我是悬浮按钮");
            }
        });
        imgShare.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startDownX = lastMoveX = (int) event.getRawX();
                        startDownY = lastMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastMoveX;
                        int dy = (int) event.getRawY() - lastMoveY;

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        //防止超出父布局边界
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > rl_container.getWidth()) {
                            right = rl_container.getWidth();
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > rl_container.getHeight()) {
                            bottom = rl_container.getHeight();
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);
                        lastMoveX = (int) event.getRawX();
                        lastMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //当抬起手指的时候如果在中间则需忘两边靠拢
                        int left2 = v.getLeft();
                        int right2 = v.getRight();
                        if(v.getLeft()+v.getWidth()/2 >= rl_container.getWidth()/2 ){
                            //超过一半的时候靠右
                            right2 = rl_container.getWidth();
                            left2 = right2 - v.getWidth();
                        }else if(v.getLeft()+v.getWidth()/2 < rl_container.getWidth()/2){
                            //小于一半的时候靠左
                            left2 = 0;
                            right2 = left2 + v.getWidth();
                        }
                        int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
                        int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);
                        if (0 != lastMoveDx || 0 != lastMoveDy) {
                            isIntercept = true;
                        } else {
                            isIntercept = false;
                        }
                        startFloatAnim(v,left2);//执行靠拢动画
                        break;
                }
                return isIntercept;
            }
        });
    }
    /**
     * 分享按钮的悬浮动画
     * @param endLeft
     */
    private void startFloatAnim(final View v,int endLeft){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(v.getLeft(), endLeft);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                v.layout(animatedValue, v.getTop(), animatedValue+v.getWidth(), v.getBottom());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
                RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                        v.getWidth(), v.getHeight());
                lpFeedback.leftMargin = v.getLeft();
                lpFeedback.topMargin = v.getTop();
                v.setLayoutParams(lpFeedback);
            }
        });
        valueAnimator.start();
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
        imgShare = findViewById(R.id.img_share);
        rl_container = findViewById(R.id.rl_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
