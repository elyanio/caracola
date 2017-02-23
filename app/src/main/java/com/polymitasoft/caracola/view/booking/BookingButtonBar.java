package com.polymitasoft.caracola.view.booking;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.R;

import java.util.List;

import static com.polymitasoft.caracola.util.Metrics.dp;

public class BookingButtonBar extends LinearLayout {
    private final int HIDE_VALUE = dp(getContext(), 60);
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
        this.setY(getY() + HIDE_VALUE);
    }

    public void show(List<ActionType> types) {
        this.removeAllViews();
        for (ActionType actionType : types) {
            View button = createButtonInvisible(actionType);
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

    public View createButtonInvisible(ActionType actionType) {
        Context ctx = getContext();
//        ShapeDrawable drawable = new ShapeDrawable();
//        drawable.getPaint().setColor(Color.parseColor("#607d8b"));
//        drawable.setShape(new OvalShape());

        FloatingActionButton button = new FloatingActionButton(ctx);
        button.setImageResource(R.drawable.ic_add_black_24dp);
//        Button button = new Button(ctx);
//        button.setBackground(drawable);

//        button.setWidth(dp(ctx, 60));
//        button.setHeight(dp(ctx, 60));
        button.setVisibility(INVISIBLE);

        switch (actionType) {
            case CREATE_BOOKING: {
//                button.setText("+");
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
//                button.setText("!");
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
//                button.setText("-");
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
//                button.setText("+C");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
            case EDIT_CHECK_IN: {
//                button.setText("!CHECK");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
            case DELETE_CHECK_IN: {
//                button.setText("-CHECK");
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
        vpa.setDuration(200);
        vpa.setInterpolator(new AccelerateDecelerateInterpolator());
        vpa.start();
    }

    private void downAnimate(View view) {
        ViewPropertyAnimator vpa = view.animate();
        vpa.translationY(HIDE_VALUE);
        vpa.setDuration(200);
        vpa.setInterpolator(new AccelerateDecelerateInterpolator());
        vpa.start();
    }
}
