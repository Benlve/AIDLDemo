package com.ben.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ben.aidl.IMyAIDL;
import com.ben.aidl.Person;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LXP_MainActivity";
    private Button mConnServerBtn;
    private Button mSendBtn;
    private Button mSendObjBtn;
    private EditText mContent;

    public static final String TARGET_PACKAGE_NAME = "com.ben.aidlserver";
    public static final String TARGET_SERVICE_NAME = "com.ben.aidl.AIDLService";
    IMyAIDL iMyAIDL = null;
    private static final int DATA = 10109;

    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        isBound = false;
    }

    private void initView() {
        mConnServerBtn = findViewById(R.id.conn_server);
        mSendBtn = findViewById(R.id.send_data);
        mSendObjBtn = findViewById(R.id.send_obj);
        mContent = findViewById(R.id.edit_content);
        mConnServerBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mSendObjBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conn_server:
                isBound();
                break;
            case R.id.send_data:
                sendData();
                break;
            case R.id.send_obj:
                //发送对象
                sendObj();
                break;
            default:
                break;
        }
    }

    Person p = null;

    private void sendObj() {
        if (iMyAIDL == null) {
            Toast.makeText(this, "请先连接服务哦~", Toast.LENGTH_SHORT).show();
            return;
        }

        if (p == null) {
            p = new Person("Tom", 21);
        }

        try {
            Person modify = iMyAIDL.modify(p);
            Toast.makeText(this, "发送对象 -> p = " + p, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "sendObj() modify = " + modify);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private void isBound() {
        if (isBound) {
            Toast.makeText(this, "服务已连接哦", Toast.LENGTH_SHORT).show();
            return;
        }
        bindAidlService();

    }

    private void sendData() {
        String s = mContent.getText().toString();
        if (s == null) {
            Toast.makeText(this, "请输入发送的信息~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iMyAIDL == null) {
            Toast.makeText(this, "请先连接服务哦~", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String result = iMyAIDL.sendBasicData(DATA, s);
            Log.d(TAG, "result = " + result);
            Toast.makeText(this, "发送成功啦~", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            Toast.makeText(this, "糟糕！发送失败~", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "e = " + e);
            e.printStackTrace();
        }
    }

    private void bindAidlService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(TARGET_PACKAGE_NAME, TARGET_SERVICE_NAME));
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() name = " + name + ", service = " + service);
            Log.d(TAG, "currentThread -> " + Thread.currentThread());

            isBound = true;

            iMyAIDL = IMyAIDL.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "连接服务成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected() name = " + name);

            isBound = false;

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        isBound = false;
        p = null;
    }
}