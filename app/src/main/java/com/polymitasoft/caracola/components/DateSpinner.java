package com.polymitasoft.caracola.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rainermf on 14/2/2017.
 */

public class DateSpinner extends LinearLayout {

    @BindView(R.id.button_next) ImageButton nextButton;
    @BindView(R.id.button_previous) ImageButton previousButton;
    @BindView(R.id.text_date) TextView dateText;
    private LocalDate date;
    private LocalDate maxDate = LocalDate.MAX;
    private LocalDate minDate = LocalDate.MIN;

    public DateSpinner(Context context) {
        super(context);
        init();
    }

    public DateSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.date_spinner, this);
        if(isInEditMode()) {
            return;
        }
        ButterKnife.bind(this);
        setDate(LocalDate.now());
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                previous(v);
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next(v);
            }
        });
    }

    /**
     * La fecha pasada como parámetro tiene que estar en el rango (inclusivo) establecido
     * por setMinDate y setMaxDate
     * @param date
     */
    public void setDate(LocalDate date) {
        previousButton.setEnabled(!date.equals(minDate));
        nextButton.setEnabled(!date.equals(maxDate));

        this.date = date;
        dateText.setText(FormatUtils.formatDate(date));
    }

    public LocalDate getDate() {
        return date;
    }

    public void next(View view) {
        setDate(date.plusDays(1));
    }

    public void previous(View view) {
        setDate(date.minusDays(1));
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }
}
