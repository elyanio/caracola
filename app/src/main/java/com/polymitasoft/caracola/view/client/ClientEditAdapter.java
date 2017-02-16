package com.polymitasoft.caracola.view.client;

import com.airbnb.epoxy.EpoxyAdapter;
import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Client;

/**
 * Created by rainermf on 13/2/2017.
 */

public class ClientEditAdapter extends EpoxyAdapter {

    private final ClientEditModel_ model;

    public ClientEditAdapter() {
        enableDiffing();

        model = new ClientEditModel_();
        addModel(model);
        showModel(model);
    }

    public void setClient(Client client) {
        model.passport(client.getPassport());
        model.firstName(client.getFirstName());
        model.lastName(client.getLastName());
        // TODO Fix this
        model.birthday(client.getBirthday().toString());
        model.gender(client.getGender().toString());
        model.country(client.getCountry().toString());

        notifyModelsChanged();
    }
}
