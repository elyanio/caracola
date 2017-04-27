package com.polymitasoft.caracola.util;

import android.util.Log;

/**
 * Created by asio on 4/24/2017.
 */

public class ActivationCode {

    public String configActivationCode(String activationCode) {

        activationCode = invertirCadena(activationCode);
        Log.e("Invertida ", activationCode);

        String[] arrayCode = dividir(activationCode, 3);
        activationCode = revolverCuatro(arrayCode);

        Log.e("Combine 4", activationCode);

        arrayCode = dividir(activationCode, 2);
        activationCode = revolverSies(arrayCode);

        Log.e("Combine 6", activationCode);

        return activationCode;
    }

    public String desconfigActivationCode(String activationCode) {

        String newActivationCode = "";

        Log.e("Code recieved", activationCode);

        String[] arrayCode = dividir(activationCode, 2);
        newActivationCode = revolverSies(arrayCode);

        Log.e("Combine 6", newActivationCode);

        arrayCode = dividir(newActivationCode, 3);
        newActivationCode = revolverCuatro(arrayCode);

        Log.e("Combine 4", newActivationCode);

        return invertirCadena(newActivationCode);
    }

    private String[] dividir(String activationCode, int split) {

        int init = 0;
        int end = split;
        int i = 0;

        String[] arrayCode = new String[activationCode.length() / split];

        while (init < 12) {
            arrayCode[i] = activationCode.substring(init, end);
            i++;
            init = end;
            end += split;
        }
        return arrayCode;
    }

    private String revolverSies(String[] arrayCode) {

        String temp = arrayCode[0];
        arrayCode[0] = arrayCode[2];
        arrayCode[2] = temp;

        temp = arrayCode[1];
        arrayCode[1] = arrayCode[3];
        arrayCode[3] = temp;

        temp = arrayCode[4];
        arrayCode[4] = arrayCode[5];
        arrayCode[5] = temp;

        String newActivationCode = "";

        for (String val : arrayCode) {
            newActivationCode += val;
        }
        return newActivationCode;
    }

    private String revolverCuatro(String[] arrayCode) {

        String temp = arrayCode[0];
        arrayCode[0] = arrayCode[2];
        arrayCode[2] = temp;

        temp = arrayCode[1];
        arrayCode[1] = arrayCode[3];
        arrayCode[3] = temp;
        String newActivationCode = "";

        for (String val : arrayCode) {
            newActivationCode += val;
        }
        return newActivationCode;

    }

    public String invertirCadena(String cadena) {
        String temp = "";

        for (int i = cadena.length() - 1; i >= 0; i--) {
            temp += cadena.charAt(i);
        }
        Log.e("Cadena Invertida", temp);
        return temp;
    }
}
