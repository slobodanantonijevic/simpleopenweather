/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanantonijevic.simpleopenweather.general;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.slobodanantonijevic.simpleopenweather.R;

/**
 * A class to build our basic animations into
 */
public class Animations {

    /**
     * Simple expand animation
     * @param ctx context on where the animation is taking place
     * @param v animating view
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
     * Simple collapse animation
     * @param ctx context on where the animation is taking place
     * @param v animating view
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

    public static void rotate(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.item_anim_rotation);
        if (a != null) {

            a.reset();
            a.setRepeatCount(Animation.INFINITE);
            a.setRepeatMode(Animation.INFINITE);
            if (v != null) {

                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}