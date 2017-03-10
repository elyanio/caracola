package com.polymitasoft.caracola.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 14/2/2017
 */
public class DateSpinner extends LinearLayout {

    @BindView(R.id.button_next) ImageButton nextButton;
    @BindView(R.id.button_previous) ImageButton previousButton;
    @BindView(R.id.text_date) TextView dateText;
    private LocalDate date = LocalDate.now();
    private LocalDate maxDate = LocalDate.MAX;
    private LocalDate minDate = LocalDate.MIN;
    private DateSpinner firstSpinner;
    private DateSpinner secondSpinner;
    private DateTimeFormatter dateFormatter;

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
        dateFormatter = DateTimeFormatter.ofPattern("d MMM");
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
     * @param date La fecha que se seleccionará en el calendario.
     */
    public void setDate(LocalDate date) {
        previousButton.setEnabled(!date.equals(minDate));
        nextButton.setEnabled(!date.equals(maxDate));

        this.date = date;
        dateText.setText(dateFormatter.format(date));
        if(firstSpinner != null) {
            firstSpinner.setMaxDate(date);
        } else if(secondSpinner != null) {
            secondSpinner.setMinDate(date);
        }
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
        nextButton.setEnabled(!date.equals(maxDate));
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
        previousButton.setEnabled(!date.equals(minDate));
    }

    public void bindForRange(DateSpinner secondSpinner) {
        this.secondSpinner = secondSpinner;
        secondSpinner.setFirstSpinner(this);
    }

    private void setFirstSpinner(DateSpinner firstSpinner) {
        this.firstSpinner = firstSpinner;
    }
}
