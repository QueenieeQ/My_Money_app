package com.queenieeq.mymoney.firebase.viewmodels;



import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import com.queenieeq.mymoney.firebase.FirebaseElement;
import com.queenieeq.mymoney.firebase.FirebaseObserver;
import com.queenieeq.mymoney.firebase.FirebaseQueryLiveDataSet;
import com.queenieeq.mymoney.firebase.ListDataSet;
import com.queenieeq.mymoney.firebase.models.WalletEntry;

public class WalletEntriesBaseViewModel extends ViewModel {
    protected final FirebaseQueryLiveDataSet<WalletEntry> liveData;
    protected final String uid;

    public WalletEntriesBaseViewModel(String uid, Query query) {
        this.uid=uid;
        liveData = new FirebaseQueryLiveDataSet<>(WalletEntry.class, query);
    }

    public void observe(LifecycleOwner owner, FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>> observer) {
        observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<FirebaseElement<ListDataSet<WalletEntry>>>() {
            @Override
            public void onChanged(@Nullable FirebaseElement<ListDataSet<WalletEntry>> element) {
                if(element != null) observer.onChanged(element);
            }
        });
    }

    public void removeObserver(Observer<FirebaseElement<ListDataSet<WalletEntry>>> observer) {
        liveData.removeObserver(observer);
    }


}
