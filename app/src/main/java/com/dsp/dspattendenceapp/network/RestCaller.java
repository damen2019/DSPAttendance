package com.dsp.dspattendenceapp.network;

import android.content.Context;
import android.util.Log;

import com.dsp.dspattendenceapp.utills.Utillity;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestCaller {
    private int REQUEST_CODE = 0;
    ResponseHandler handler;

    public <T> RestCaller(Context context, ResponseHandler handler, Observable<T> call, final int REQUEST_CODE) throws NumberFormatException {
        if (Utillity.isNetworkAvailable(context)) {
            if (REQUEST_CODE <= 0) {
                throw new NumberFormatException();
            }
            this.REQUEST_CODE = REQUEST_CODE;
            this.handler = handler;
            callApi(call);
        } else {
            Throwable throwable = new Throwable("No Internet Connection");
            handler.onFailure(throwable,REQUEST_CODE);
            //Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }



    private <T> void callApi(Observable<T> call) {

        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        Log.e("tag" , "");
                    }

                    @Override
                    public void onNext(@NotNull T bookingResults) {
                        try {
                            handler.onSuccess(bookingResults, REQUEST_CODE);
                        }catch (Exception ex){
                            Log.e("API Crash" , ex.getMessage() + "");
                        }

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        e.printStackTrace();
                        try {
                            handler.onFailure(e, REQUEST_CODE);
                        }catch (Exception ex){
                            Log.e("API Crash" , ex.getMessage() + "");
                        }

                    }

                    @Override
                    public void onComplete() {
                        Log.e("tag" , "");
                    }
                });
    }

}
