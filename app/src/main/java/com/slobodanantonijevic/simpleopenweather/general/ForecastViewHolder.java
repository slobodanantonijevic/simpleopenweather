package com.slobodanantonijevic.simpleopenweather.general;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.View;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.widget.CustomTextView;

/**
 * The view holder for RecyclerView cell
 */
public class ForecastViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    // Butter Knife
    @BindView(R.id.day) public CustomTextView day;
    @BindView(R.id.date) public CustomTextView date;
    @BindView(R.id.temp) public CustomTextView temp;
    @BindView(R.id.weatherIcon) public AppCompatImageView icon;

    @BindView(R.id.additionalData) ConstraintLayout additionalData;

    @BindView(R.id.minTemp) public CustomTextView minTemp;
    @BindView(R.id.maxTemp) public CustomTextView maxTemp;
    @BindView(R.id.wind) public CustomTextView wind;
    @BindView(R.id.humidity) public CustomTextView humidity;
    @BindView(R.id.pressure) public CustomTextView pressure;

    private boolean isExpanded = false;

    public ForecastViewHolder(View view, Context context) {
        super(view);

        this.context = context;

        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.forecastContainer) // Let Butter Knife handle this to reduce boilerplate code
    void setExpanded() {

        if (isExpanded) {

            Animations.collapse(context, additionalData);
        } else {

            Animations.expand(context,additionalData);
        }
        isExpanded = !isExpanded;
    }
}
