package com.polymitasoft.caracola.datamodel;

import com.polymitasoft.caracola.CaracolaApplication;

/**
 * Created by rainermf on 11/2/2017.
 */
public enum Gender {
    FEMININE, MASCULINE;

    @Override
    public String toString() {
        // TODO Cambiar por una solución localizada
        if(this == FEMININE)
            return "Femenino";
        return "Masculino";
    }
}
