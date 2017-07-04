package com.example.esclavo.paraneuronal;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by WindowCtm on 15-06-2017.
 */

public class Funciones {
    public float menor(float a,float b){
        if(a>=b)
            return b;
        else
            return a;
    }
    public float mayor(float a,float b){
        if(a>=b)
            return a;
        else
            return b;
    }


}
