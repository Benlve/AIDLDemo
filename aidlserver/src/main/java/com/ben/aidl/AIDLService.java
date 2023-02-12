package com.ben.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AIDLService extends Service {

    private static final String TAG = "LXP_AIDLService";

    public AIDLService() {
        Log.d(TAG, "AIDLService()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() intent = " + intent);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    //将Binder对象返回给client
    private IBinder mBinder = new IMyAIDL.Stub() {
        @Override
        public String sendBasicData(int data, String comment) throws RemoteException {
            Log.d(TAG, "sendBasicData data = " + data + ", comment = " + comment);
            Log.d(TAG, "currentThread -> " + Thread.currentThread());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "已经来自客户端的数据";
        }

        @Override
        public Person modify(Person p) throws RemoteException {
            Person person = new Person(p.getName(), 24);
            return person;
        }
    };
}