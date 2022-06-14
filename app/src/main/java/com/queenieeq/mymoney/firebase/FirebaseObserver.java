package com.queenieeq.mymoney.firebase;

public interface FirebaseObserver<T> {
    void onChanged(T t);
}
