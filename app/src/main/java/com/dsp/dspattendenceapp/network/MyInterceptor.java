package com.dsp.dspattendenceapp.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class MyInterceptor implements Interceptor {
   @Override
   public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    String stringurl = request.url().toString();
    stringurl = stringurl.replace("%2B", "+");

    Request newRequest = new Request.Builder()
        .url(stringurl)
        .build();

    return chain.proceed(newRequest);
 }
}