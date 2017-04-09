package com.polymitasoft.caracola.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.polymitasoft.caracola.view.booking.VistaMes;


/**
 * Triggers a event when scrolling reaches bottom.
 * <p/>
 * Created by martinsandstrom on 2010-05-12.
 * Updated by martinsandstrom on 2014-07-22.
 * <p/>
 * Usage:
 * <p/>
 * scrollView.setOnBottomReachedListener(
 * new InteractiveScrollView.CapturadorEventoMoverScroll() {
 *
 * @Override public void onMover() {
 * // do something
 * }
 * }
 * );
 * <p/>
 * <p/>
 * Include in layout:
 * <p/>
 * <se.marteinn.ui.InteractiveScrollView
 * android:layout_width="match_parent"
 * android:layout_height="match_parent" />
 */
public class InteractivoScrollView extends ScrollView {
    CapturadorEventoMoverScroll mListener;

    public InteractivoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InteractivoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InteractivoScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View ultimaView = getChildAt(getChildCount() - 1);
//        View iniView = (View) getChildAt(0);
        //si llego al principio
        if (0 >= getScrollY() && mListener != null) {
            mListener.onMover(CapturadorEventoMoverScroll.SENTIDO_SCROLL_ARRIBA);
        }
        //si llego al final
        int diff = (ultimaView.getBottom() - (getHeight() + getScrollY()));
        if (diff <= 0 && mListener != null) {
            mListener.onMover(CapturadorEventoMoverScroll.SENTIDO_SCROLL_ABAJO);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void moverScrollMesActual() {
        LinearLayout view = (LinearLayout) getChildAt(0);

        VistaMes vistaMes;
        for (int i = 0; i < view.getChildCount(); i++) {
            vistaMes = (VistaMes) view.getChildAt(i);
            if (vistaMes.estaElDiaHoy()) {
                int index = vistaMes.getHeight() * i;
                this.setScrollY(index);
            }
        }
    }
    // Getters & Setters

    public CapturadorEventoMoverScroll getOnBottomReachedListener() {
        return mListener;
    }

    public void setOnBottomReachedListener(
            CapturadorEventoMoverScroll capturadorEventoMoverScroll) {
        mListener = capturadorEventoMoverScroll;
    }

    //
//
//    /**
//     * Event listener.
//     */
    public interface CapturadorEventoMoverScroll {
        int SENTIDO_SCROLL_ARRIBA = 1;
        int SENTIDO_SCROLL_ABAJO = 0;

        void onMover(int sentido);
    }

}