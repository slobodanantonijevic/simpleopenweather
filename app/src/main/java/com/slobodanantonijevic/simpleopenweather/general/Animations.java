package com.slobodanantonijevic.simpleopenweather.general;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.slobodanantonijevic.simpleopenweather.R;

class Animations {

    /**
     *
     * @param ctx
     * @param v
     */
    static void expand(Context ctx, final View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.layout_anim_expand);
        if (a != null) {

            a.reset();
            if (v != null) {

                v.clearAnimation();
                v.startAnimation(a);
            }
        }

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }
        });
    }

    /**
     *
     * @param ctx
     * @param v
     */
    static void collapse(Context ctx, final View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.layout_anim_collapse);
        if (a != null) {

            a.reset();
            if (v != null) {

                v.clearAnimation();
                v.startAnimation(a);
            }
        }

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                v.setVisibility(View.GONE);
            }
        });
    }
}