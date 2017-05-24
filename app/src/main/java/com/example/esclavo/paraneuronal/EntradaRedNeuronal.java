package com.example.esclavo.paraneuronal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Esclavo on 23-05-2017.
 */

public class EntradaRedNeuronal
{
    public double longitudOrigen;
    public double latitudOrigen;
    public double longitudDestino;
    public double latitudDestino;
    public  int fecha;

    public EntradaRedNeuronal(double longitudOri, double latitudOri,
                              double longitudDes, double latitudDes, int fecha)
    {
        //Validaciones
        this.longitudOrigen = longitudOri;
        this.latitudOrigen = latitudOri;
        this.longitudDestino = longitudDes;
        this.latitudDestino = latitudDes;
        this.fecha = fecha;
    }

    public String getLongitudOrigen()
    {
        return Double.toString(this.longitudOrigen);
    }

    public String getLatitudOrigen()
    {
        return Double.toString(this.latitudOrigen);
    }

    public String getLongitudDestino()
    {
        return Double.toString(this.longitudDestino);
    }

    public String getLatitudDestino()
    {
        return Double.toString(this.latitudDestino);
    }

    public String getFecha()
    {
        return Integer.toString(this.fecha);
    }

    public String toJSON(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("longitudOrigen", getLongitudOrigen());
            jsonObject.put("latitudOrigen", getLatitudOrigen());
            jsonObject.put("longitudDestino", getLongitudDestino());
            jsonObject.put("latitudDestino", getLatitudDestino());
            jsonObject.put("fecha", getFecha());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
