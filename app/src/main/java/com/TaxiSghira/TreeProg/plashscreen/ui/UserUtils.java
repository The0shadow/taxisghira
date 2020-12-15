package com.TaxiSghira.TreeProg.plashscreen.ui;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.TaxiSghira.TreeProg.plashscreen.Commun.Common;
import com.TaxiSghira.TreeProg.plashscreen.Module.DriverGeoModel;
import com.TaxiSghira.TreeProg.plashscreen.Module.FCMSendData;
import com.TaxiSghira.TreeProg.plashscreen.Module.TokenModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserUtils {

    public static void UpdateToken(Context context , String  token){
        TokenModel token1 = new TokenModel(token);
        FirebaseDatabase.getInstance()
                .getReference(Common.CLIENT_TOKEN_REFERENCE)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(token1)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(aVoid -> {});
    }
    public static void sendRequestToDriver( MapViewModel mapViewModel,Context application , DriverGeoModel foundDriver, Location location) {

        CompositeDisposable disposable = new CompositeDisposable();

        FirebaseDatabase
                .getInstance()
                .getReference(Common.DRIVER_TOKEN_REFERENCE)
                .child(foundDriver.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            TokenModel token = snapshot.getValue(TokenModel.class);
                            Map<String,String> notification = new HashMap<String,String>();
                            notification.put(Common.NOTI_TITLE,Common.REQUEST_DRIVER_TITLE );
                            notification.put(Common.NOTI_BODY,"Message For Driver Action" );
                            notification.put(Common.RIDER_KEY,Common.Current_Client_Id);

                            notification.put(Common.RIDER_PICK_UP_LOCATION,
                                    location.getLatitude() +
                                    "," +
                                    location.getLongitude());

                            FCMSendData fcmSendData = new FCMSendData(token.getToken(),notification);
                            disposable.add(mapViewModel.sendNotification(fcmSendData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(fcmResponse -> {
                                if (fcmResponse.getSuccess() == 0){
                                    disposable.clear();
                                }
                            },Throwable::printStackTrace));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
