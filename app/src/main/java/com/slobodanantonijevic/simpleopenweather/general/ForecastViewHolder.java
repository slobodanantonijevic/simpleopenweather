package com.slobodanantonijevic.simpleopenweather.general;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.widget.CustomTextView;

/**
 * The view holder for RecyclerView cell
 */
public class ForecastViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    public CustomTextView day;
    public CustomTextView date;
    public CustomTextView temp;
    public AppCompatImageView icon;

    private ConstraintLayout additionalData;

    public CustomTextView minTemp;
    public CustomTextView maxTemp;
    public CustomTextView wind;
    public CustomTextView humidity;
    public CustomTextView pressure;

    private boolean isExpanded = false;

    public ForecastViewHolder(View v, Context context) {
        super(v);

        this.context = context;

        day = v.findViewById(R.id.day);
        date = v.findViewById(R.id.date);
        temp = v.findViewById(R.id.temp);
        icon = v.findViewById(R.id.weatherIcon);

        additionalData = v.findViewById(R.id.additionalData);

        minTemp = v.findViewById(R.id.minTemp);
        maxTemp = v.findViewById(R.id.maxTemp);
        wind = v.findViewById(R.id.wind);
        humidity = v.findViewById(R.id.humidity);
        pressure = v.findViewById(R.id.pressure);

        v.setOnClickListener(view -> {

            // Change the state
            setExpanded(isExpanded);
        });
    }

    /**
     *
     * @param expanded
     */
    private void setExpanded(boolean expanded) {

        if (expanded) {

            Animations.collapse(context, additionalData);
        } else {

            Animations.expand(context,additionalData);
        }
        isExpanded = !expanded;
    }
}
