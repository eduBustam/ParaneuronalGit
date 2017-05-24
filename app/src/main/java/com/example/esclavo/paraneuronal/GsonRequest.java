package com.example.esclavo.paraneuronal;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson mGson;
    private final Type type;
    private final Map<String, String> mHeaders;
    private final Response.Listener<T> mListener;

    public GsonRequest(int method, String url, Type type, JSONObject jsonRequest,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, type, null, jsonRequest, listener, errorListener);
    }

    public GsonRequest(int method, String url, Type type, Map<String, String> headers,
                       JSONObject jsonRequest, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        this.mGson = new Gson();
        this.type = type;
        this.mHeaders = headers;
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            T parseObject = mGson.fromJson(json, type);
            return Response.success(parseObject,HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    public void enviarPeticion(String urlRed, double longitudOri, double latitudOri, double longitudDes,
                               double latitudDes, int fecha)
    {
        //Creamos objeto para armar petición de tipo HTTP POST y le pasamos
        //como parametro al constructor un String con la URL del servidor a
        //la cual vamos a enviar los datos.
        HttpPost request = new HttpPost(baseUrl);

        EntradaRedNeuronal ern = new EntradaRedNeuronal(longitudOri, latitudOri, longitudDes,
                latitudDes, fecha);

        //Configuramos los parametos que vamos a enviar con la peticion HTTP POST
        List<EntradaRedNeuronal> postParameters = new ArrayList<EntradaRedNeuronal>(1);
        postParameters.add(new BasicNameValuePair("entradaRedNeuronal", ern));
        request.setEntity(new UrlEncodedFormEntity(postParameters));

        GsonRequest gsonRequest = new GsonRequest(request.Method.GET, urlRed, RN.class, null,
                new Response.Listener<RN>() {
                    @Override
                    public void onResponse(RN response) {
                        rn = response;
                        int tiempo /*= Función de la red neuronal que calcula el tiempo*/;
                        Snackbar.make(coord, "La micro se tardará aproximadamente" + tiempo + "minutos.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                },
                response.ErrorListener);
    }
}

