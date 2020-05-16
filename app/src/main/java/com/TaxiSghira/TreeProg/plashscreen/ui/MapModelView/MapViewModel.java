package com.TaxiSghira.TreeProg.plashscreen.ui.MapModelView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.TaxiSghira.TreeProg.plashscreen.API.FireBaseClient;
import com.TaxiSghira.TreeProg.plashscreen.Commun.Commun;
import com.TaxiSghira.TreeProg.plashscreen.Module.Chifor;
import com.TaxiSghira.TreeProg.plashscreen.Module.Demande;
import com.TaxiSghira.TreeProg.plashscreen.Module.Pickup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Delayed;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MapViewModel extends ViewModel {
    private MutableLiveData<Chifor> chiforMutableLiveData ;
    Pickup pickup;
    private MutableLiveData<Pickup> acceptMutableLiveData;

    public LiveData<Chifor> getChiforMutableLiveData() {
        chiforMutableLiveData = new MutableLiveData<>();
        return chiforMutableLiveData;
    }

    public LiveData<Pickup> getAcceptMutableLiveData() {
        acceptMutableLiveData = new MutableLiveData<>();
        return acceptMutableLiveData;
    }

    public void GetChiforDataLocation(){
        FireBaseClient.getFireBaseClient().getFirebaseFirestore()
                .collection(Commun.Chifor_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            try {
                                chiforMutableLiveData.setValue(document.toObject(Chifor.class));
                            }catch (Exception e){Timber.e(e);}
                        }
                    }
                });
    }

    public void DelateDemande(Demande demande){
        FireBaseClient.getFireBaseClient().getFirebaseFirestore()
                .collection(Commun.Demande_DataBase_Table)
                .document(demande.getClientName())
                .delete().addOnCompleteListener(task -> Toast.makeText(getApplicationContext(),"تم إلغاء الطلب",Toast.LENGTH_SHORT).show());
    }

    public void GetAcceptDemandeList(){
        FireBaseClient.getFireBaseClient().getDatabaseReference()
                .child(Commun.Pickup_DataBase_Table)
                .orderByChild(Commun.ClientName_String)
                .equalTo(Commun.Current_Client_DispalyName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        pickup = dataSnapshot1.getValue(Pickup.class);
                    }
                    acceptMutableLiveData.setValue(pickup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"لايوجد اي طلب الان !!!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AddDemande(Demande demande){
        FireBaseClient.getFireBaseClient().getFirebaseFirestore()
                .collection(Commun.Demande_DataBase_Table)
                .document(demande.getClientName())
                .set(demande);
    }
}
