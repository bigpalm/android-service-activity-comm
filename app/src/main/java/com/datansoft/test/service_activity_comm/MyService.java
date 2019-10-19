package com.datansoft.test.service_activity_comm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    // 액티비티에서 함수 호출 시 값을 넘겨줄 변수
    private int number;
    int getNumber(){
        return number++;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_SHORT).show();
        number = 0;
    }
    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    // BindService 로 서비스를 시작해야 함수호출이 가능
    private final IBinder mBinder = new LocalBinder();
    class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    private ICallback mCallback;
    // 외부 액티비티의 함수를 서비스에서 호출하고 싶을 경우 이 인터페이스를 구현하여 콜백 등록 필요
    public interface ICallback {
        public void remoteCall();
    }
    // 액티비티에서 호출하는 콜백 등록 함수
    public void registerCallback(ICallback cb){
        mCallback = cb;
    }
    void callRemoteCall() {
        // main activity 에서 등록한 remoteCall 호출
        mCallback.remoteCall();
    }

    // 메인 액티비티에서 호출하여 OtherActivity를 호출함
    void showOtherActivity() {
        Intent showIntent = new Intent(getApplicationContext(), OtherActivity.class);
        showIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(showIntent);

    }

}
