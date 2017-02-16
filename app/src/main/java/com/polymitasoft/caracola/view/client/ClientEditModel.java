package com.polymitasoft.caracola.view.client;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.polymitasoft.caracola.R;

/**
 * Created by rainermf on 13/2/2017.
 */

public class ClientEditModel extends EpoxyModel<ClientEditView> {

    @EpoxyAttribute String passport;
    @EpoxyAttribute String firstName;
    @EpoxyAttribute String lastName;
    @EpoxyAttribute String country;
    @EpoxyAttribute String gender;
    @EpoxyAttribute String birthday;

    @Override
    protected int getDefaultLayout() {
        return R.layout.client_edit_model;
    }

    @Override
    public void bind(ClientEditView view) {
        view.setPassport(passport);
        view.setFirstName(firstName);
        view.setLastName(lastName);
        view.setCountry(country);
        view.setGender(gender);
        view.setBirthday(birthday);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }
}
