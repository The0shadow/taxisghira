package com.TaxiSghira.TreeProg.plashscreen.Room;

import androidx.lifecycle.LiveData;

import com.TaxiSghira.TreeProg.plashscreen.Callback.Doa;
import com.TaxiSghira.TreeProg.plashscreen.Callback.IFCMService;
import com.TaxiSghira.TreeProg.plashscreen.Module.Chifor;
import com.TaxiSghira.TreeProg.plashscreen.Module.FCMResponse;
import com.TaxiSghira.TreeProg.plashscreen.Module.FCMSendData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.http.Body;

public class Repository {
    private Doa doa;
    private IFCMService ifcmService;

    @Inject
    public Repository(Doa doa,IFCMService ifcmService) {
        this.doa = doa;this.ifcmService = ifcmService;
    }

    public Observable<FCMResponse> sendNotification(FCMSendData body){ return ifcmService.sendNotification(body);}
    public void InsertData(Chifor chifor){doa.InsertData(chifor);}
    public void DeleteData(int id){doa.DeleteData(id);}
    public LiveData<List<Chifor>> GetData(){return doa.GetData();}
}