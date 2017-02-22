package com.polymitasoft.caracola.view.booking;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class BookingButtonBar extends LinearLayout {
    private final int HIDE_CALUE = 200;
    private ReservaPrincipal reservaPrincipal;
    private boolean visible = false;

    public BookingButtonBar(Context context) {
        super(context);
        init();
    }

    public BookingButtonBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookingButtonBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setReservaPrincipal(ReservaPrincipal reservaPrincipal) {
        this.reservaPrincipal = reservaPrincipal;
        init();
    }

    public void init() {
        this.setY(getY() + HIDE_CALUE);
    }

    public void show(List<ActionType> types) {
        this.removeAllViews();
        for (ActionType actionType : types) {
            Button button = createButtonInvisible(actionType);
            button.setVisibility(VISIBLE);
        }
        if (visible == false) {
            upAnimate(reservaPrincipal.getBookingButtonBar());
        }
        visible = true;
    }

    public void hide() {
        downAnimate(reservaPrincipal.getBookingButtonBar());
        visible = false;
    }

    public Button createButtonInvisible(ActionType actionType) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(Color.parseColor("#607d8b"));
        drawable.setShape(new OvalShape());
        Button button = new Button(getContext());
        button.setBackground(drawable);
        button.setWidth(60);
        button.setHeight(60);
        button.setVisibility(INVISIBLE);
        switch (actionType) {
            case CREATE_BOOKING: {
                button.setText("+");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservaPrincipal.clickPreR();
                    }
                });
                this.addView(button);
                break;
            }
            case EDIT_BOOKING: {
                button.setText("!");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservaPrincipal.clickEditR();
                    }
                });
                this.addView(button);
                break;
            }
            case DELETE_BOOKING: {
                button.setText("-");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservaPrincipal.clickEliminarR();
                    }
                });
                this.addView(button);
                break;
            }
            case CREATE_CHECK_IN: {
                button.setText("+C");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
            case EDIT_CHECK_IN: {
                button.setText("!CHECK");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
            case DELETE_CHECK_IN: {
                button.setText("-CHECK");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
        }
        return button;
    }

    private void upAnimate(View view) {
        ViewPropertyAnimator vpa = view.animate();
        vpa.translationY(0);
        vpa.setDuration(500);
        vpa.setInterpolator(new AccelerateDecelerateInterpolator());
        vpa.start();
    }

    private void downAnimate(View view) {
        ViewPropertyAnimator vpa = view.animate();
        vpa.translationY(HIDE_CALUE);
        vpa.setDuration(500);
        vpa.setInterpolator(new AccelerateDecelerateInterpolator());
        vpa.start();
    }
}
