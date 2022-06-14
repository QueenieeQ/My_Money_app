package com.queenieeq.mymoney.firebase.viewmodels;



import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import com.queenieeq.mymoney.firebase.FirebaseElement;
import com.queenieeq.mymoney.firebase.FirebaseObserver;
import com.queenieeq.mymoney.firebase.FirebaseQueryLiveDataElement;
import com.queenieeq.mymoney.firebase.models.User;

public class UserProfileBaseViewModel extends ViewModel {
    private final FirebaseQueryLiveDataElement<User> liveData;

    public UserProfileBaseViewModel(String uid) {
        liveData = new FirebaseQueryLiveDataElement<>(User.class, FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid));
    }

    public void observe(LifecycleOwner owner, FirebaseObserver<FirebaseElement<User>> observer) {
        if(liveData.getValue() != null) observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<FirebaseElement<User>>() {
            @Override
            public void onChanged(@Nullable FirebaseElement<User> firebaseElement) {
                if(firebaseElement != null) observer.onChanged(firebaseElement);

            }
        });
    }

}