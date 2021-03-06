package com.mahui.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mahui.aidlservice.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    Button aidlBtn;
    IMyAidlInterface myService;// 服务
    String appName = "unknown";

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IMyAidlInterface.Stub.asInterface(service);// 获取服务对象
            aidlBtn.setEnabled(true);
        }// 连接服务

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aidlBtn = (Button) findViewById(R.id.aidl_1_btn);
        appName = getPackageName();

        // 我们没办法在构造Intent的时候就显式声明.
        Intent intent = new Intent("com.mahui.aidlservice.IMyAidlInterface");
        // 既然没有办法构建有效的component,那么给它设置一个包名也可以生效的
        intent.setPackage("com.mahui.aidlservice");// the service package
        // 绑定服务，可设置或触发一些特定的事件
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        aidlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // AIDL服务调用代码如下：
                    String msg = myService.helloAndroidAIDL(appName);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
