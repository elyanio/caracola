package com.polymitasoft.caracola.view.consumption;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;
import com.polymitasoft.caracola.view.service.InternalServiceSelectorView;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ConsumptionBinding {

    @BindView(R.id.amountText) EditText amount;
    @BindView(R.id.priceText) EditText price;
    @BindView(R.id.service_selector) InternalServiceSelectorView service;
    private Consumption consumption;

    ConsumptionBinding(final AppCompatActivity activity, final Consumption consumption) {
        ButterKnife.bind(this, activity);
        setConsumption(consumption);
        service.setOnSelectedServiceListener(new InternalServiceSelectorView.OnSelectedServiceListener() {
            @Override
            public void onSelectedService(InternalService service) {
                consumption.setPrice(service.getDefaultPrice());
                price.setText(FormatUtils.formatMoney(service.getDefaultPrice()));
            }
        });
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder builder = new NumberPickerBuilder()
                        .setFragmentManager(activity.getSupportFragmentManager())
                        .setCurrentNumber(consumption.getAmount())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setDecimalVisibility(View.INVISIBLE)
                        .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                            @Override
                            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                                consumption.setAmount(number.intValue());
                                amount.setText(String.valueOf(number.intValue()));
                            }
                        });
                builder.show();
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder builder = new NumberPickerBuilder()
                        .setFragmentManager(activity.getSupportFragmentManager())
                        .setCurrentNumber(consumption.getPrice())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                            @Override
                            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                                consumption.setPrice(fullNumber);
                                price.setText(FormatUtils.formatMoney(fullNumber));
                            }
                        });
                builder.show();
            }
        });
    }

    public Consumption getConsumption() {
        consumption.setInternalService(service.getService());
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
        service.setService(consumption.getInternalService());
        amount.setText(String.valueOf(consumption.getAmount()));
        price.setText(FormatUtils.formatMoney(consumption.getPrice()));
    }
}
