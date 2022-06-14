package com.queenieeq.mymoney.firebase.viewmodel_factories;


import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import com.queenieeq.mymoney.firebase.viewmodels.WalletEntriesBaseViewModel;

public class TopWalletEntriesViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    TopWalletEntriesViewModelFactory(String uid) {
        this.uid = uid;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new Model(uid);
    }

    public static Model getModel(String uid, FragmentActivity activity) {
        return ViewModelProviders.of(activity, new TopWalletEntriesViewModelFactory(uid)).get(Model.class);
    }

    public static class Model extends WalletEntriesBaseViewModel {

        public Model(String uid) {
            super(uid, FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp"));
        }

        public void setDateFilter(Calendar startDate, Calendar endDate) {
            liveData.setQuery(FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp")
                    .startAt(-endDate.getTimeInMillis()).endAt(-startDate.getTimeInMillis()));
        }
    }
}