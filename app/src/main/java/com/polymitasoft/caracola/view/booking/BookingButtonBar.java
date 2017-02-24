package com.polymitasoft.caracola.view.booking;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.R;

import java.util.List;

import static android.view.LayoutInflater.from;
import static com.polymitasoft.caracola.util.Metrics.dp;

public class BookingButtonBar extends LinearLayout {
    private final int HIDE_VALUE = dp(getContext(), 80);
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
            FloatingActionButton button = createButtonInvisible(actionType);
            button.setVisibility(VISIBLE);
        }
        if (visible == false) {
            upAnimate(reservaPrincipal.getBookingButtonBar());
        }
        visible = true;
    }

    public void hide() {
        if (visible == true) {
            downAnimate(reservaPrincipal.getBookingButtonBar());
            visible = false;
        }
    }

    public FloatingActionButton createButtonInvisible(ActionType actionType) {
        Context ctx = getContext();

        FloatingActionButton button = (FloatingActionButton) from(ctx).inflate(R.layout.round_button, null);
        button.setVisibility(INVISIBLE);

        switch (actionType) {
            case CREATE_BOOKING: {
                button.setImageResource(R.drawable.ic_add_black_24dp);
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
                button.setImageResource(R.drawable.ic_edit_black_24dp);
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
                button.setImageResource(R.drawable.ic_clear_black_24dp);
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
                button.setImageResource(R.drawable.ic_check_in_bell);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservaPrincipal.click_fisicaR();
                    }
                });
                this.addView(button);
                break;
            }
            case EDIT_CHECK_IN: {
                button.setImageResource(R.drawable.ic_edit_black_24dp);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                this.addView(button);
                break;
            }
            case DELETE_CHECK_IN: {
                button.setImageResource(R.drawable.ic_clear_black_24dp);
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
