package com.kai.library.cardanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kevin on 15/8/28.
 */
public class CardAnimation {

    public static final int FirstAnimation = 1;
    public static final int alreadyCardAnimation = 6;
    public static final int clickButtonAnimation = 3;
    public static final int cancelCardAnimation = 4;

    private Activity self;
    CardAnimListener listener;
    private RelativeLayout container;
    private boolean isplay = false;
    private int amType = 0;

    private float init_scale = 0.7f;
    private float def_scale = 0.9f;
    private float alre_scale = 1.0f;


    public CardAnimation(Activity activity, CardAnimListener listener, RelativeLayout container){
        this.self = activity;
        this.listener = listener;
        this.container = container;
    }

    public void FirstLoading(final Queue queue){
        amType = FirstAnimation;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (queue.size() > 0) {
                            View view = (View) queue.poll();
                            container.addView(view);
                            ObjectAnimator o1, o2, o3, o4, o5;
                            o1 = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 0.9f);
                            o2 = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 0.9f);
                            o3 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
                            //載入動畫 放大
                            if (queue.size() % 2 == 0) {
                                o4 = ObjectAnimator.ofFloat(view, "rotation", -10, 0);
                                o5 = ObjectAnimator.ofFloat(view, "TranslationX", -1000.0f, 0.0f);
                            } else {
                                o4 = ObjectAnimator.ofFloat(view, "rotation", 10, 0);
                                o5 = ObjectAnimator.ofFloat(view, "TranslationX", 1000.0f, 0.0f);
                            }
                            //載入動畫 旋轉
                            AnimatorSet set = new AnimatorSet();
                            if (queue.size() == 0) {
                                o1 = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 0.9f);
                                o2 = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 0.9f);
                                //載入動畫 最上面 放大
                                set.addListener(animatorListener);
                            }
                            set.setDuration(1000);
                            //載入動畫 一張的時間
                            set.playTogether(o1, o2, o3, o4,o5);
                            set.start();
                        } else {
                            cancel();
                        }
                    }
                });
            }
        }, 0, 300);
        //載入動畫 接續時間
    }

    AnimatorSet alreadyCard(View view){
        //卡片彈跳
        amType = alreadyCardAnimation;
        AnimatorSet set1 = new AnimatorSet();
        ObjectAnimator o1 = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX(), 1.03f);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY(), 1.03f);
        //卡片彈跳 第一次
        set1.playTogether(o1, o2);
        AnimatorSet set2 = new AnimatorSet();
        ObjectAnimator o3 = ObjectAnimator.ofFloat(view, "scaleX", 1.03f, 0.98f);
        ObjectAnimator o4 = ObjectAnimator.ofFloat(view, "scaleY", 1.03f, 0.98f);
        //卡片第二次
        set2.playTogether(o3, o4);
        AnimatorSet set3 = new AnimatorSet();
        ObjectAnimator o5 = ObjectAnimator.ofFloat(view, "scaleX", 0.98f, 1.0f);
        ObjectAnimator o6 = ObjectAnimator.ofFloat(view, "scaleY", 0.98f, 1.0f);
        //卡片第三次
        set3.playTogether(o5, o6);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        //卡片彈跳 時間
        set.play(set1);
        set.play(set2).after(set1);
        set.play(set3).after(set2);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(animatorListener);
        return set;
    }

    public void clickCard(View topLayer){
        //卡片按下動畫
        ObjectAnimator top_o1 = ObjectAnimator.ofFloat(topLayer,"scaleX", 1.0f, 0.95f);
        ObjectAnimator top_o2 = ObjectAnimator.ofFloat(topLayer,"scaleY", 1.0f, 0.95f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(top_o1, top_o2);
        set.start();
    }

    public void cancelCard(View topLayer,View bottomLayer){
        //卡片放開動畫
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new DecelerateInterpolator(1.5f));
        ObjectAnimator o1 = ObjectAnimator.ofFloat(topLayer,"scaleX", topLayer.getScaleX(), 1.0f);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(topLayer,"scaleY", topLayer.getScaleY(), 1.0f);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(topLayer,"translationX", topLayer.getTranslationX(), 0.0f);
        ObjectAnimator o4 = ObjectAnimator.ofFloat(topLayer,"rotation", topLayer.getRotation(), 0);
        ObjectAnimator o5, o6;
        if(bottomLayer != null){
            o5 = ObjectAnimator.ofFloat(topLayer,"bottomLayer", bottomLayer.getScaleX(), 0.9f);
            o6 = ObjectAnimator.ofFloat(topLayer,"bottomLayer", bottomLayer.getScaleY(), 0.9f);
            set.playTogether(o1, o2, o3, o4, o5, o6);
        }else {
            set.playTogether(o1, o2, o3, o4);
        }
        set.start();
    }

    public void goCard(View topLayer, View bottomLayer,float X){
        //卡片滑出去動畫
        AnimatorSet set = new AnimatorSet();
        set.setDuration(350);
        ObjectAnimator o1 = ObjectAnimator.ofFloat(topLayer,"translationX", topLayer.getTranslationX(), X);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(topLayer,"alpha", 1.0f, 0.0f);
        set.playTogether(o1, o2);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeViewAt(container.getChildCount()-1);
                listener.animatorFinish(clickButtonAnimation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        if(bottomLayer != null){
            AnimatorSet a = alreadyCard(bottomLayer);
            a.setStartDelay(300);
            a.start();
        }
    }

    public boolean isPlay(){
        return isplay;
    }

    public int getAmType(){
        return amType;
    }


    public void setInit_scale(float value){
        init_scale = value;
    }

    public void setDef_scale(float value){
        def_scale = value;
    }

    public void setAlre_scale(float value){
        alre_scale = value;
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            isplay = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            isplay = false;
            switch (amType){
                case FirstAnimation:
                    alreadyCard(container.getChildAt(container.getChildCount()-1)).start();
                    break;
                case alreadyCardAnimation:
                    listener.animatorFinish(alreadyCardAnimation);
                    break;
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
}
