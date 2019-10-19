package com.datansoft.test.service_activity_comm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// 액티비티 -> 서비스 메소드 호출 출처: https://newland435.tistory.com/7 [뉴랜드's 서랍장]
// 서비스 -> 액티비티 메소드 호출 출처: https://goodtogreate.tistory.com/entry/Activity와-Service간의-통신 [GOOD to GREAT]
public class MainActivity extends AppCompatActivity {

    MyService myService;
    boolean isService = false; // 서비스 중인 확인용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 서비스 시작
                Intent intent = new Intent(
                        MainActivity.this, // 현재 화면
                        MyService.class); // 다음넘어갈 컴퍼넌트

                bindService(intent, // intent 객체
                        conn, // 서비스와 연결에 대한 정의
                        Context.BIND_AUTO_CREATE);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 서비스 종료
                if(isService){
                    unbindService(conn); // 서비스 종료
                    isService = false;
                }
                else{
                    Toast.makeText(MainActivity.this, "연결된 서비스 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 서비스 데이터 확인
                if (!isService) {
                    Toast.makeText(getApplicationContext(),
                            "서비스중이 아닙니다, 데이터받을수 없음",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                int num = myService.getNumber();//서비스쪽 메소드로 값 전달 받아 호출
                Toast.makeText(getApplicationContext(),
                        "받아온 데이터 : " + num,
                        Toast.LENGTH_LONG).show();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 서비스가 액티비티의 메소드를 호출
                if (!isService) {
                    Toast.makeText(getApplicationContext(),
                            "서비스중이 아닙니다, 데이터받을수 없음",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                myService.callRemoteCall();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 서비스가 새로운 액티비티를 호출
                if (!isService) {
                    Toast.makeText(getApplicationContext(),
                            "서비스중이 아닙니다, 데이터받을수 없음",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                myService.showOtherActivity();
            }
        });
    }

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MyService.LocalBinder mb = (MyService.LocalBinder) service;
            myService = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true;

            // 서비스 콜백 등록
            myService.registerCallback(mCallback);
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Toast.makeText(getApplicationContext(),
                    "서비스 연결 해제",
                    Toast.LENGTH_LONG).show();
        }
    };

    // call below callback in service. it is running in Activity.
    private MyService.ICallback mCallback = new MyService.ICallback() {
        @Override public void remoteCall() {
            Toast.makeText(getApplicationContext(),
                    "서비스가 액티비티 메소드 호출",
                    Toast.LENGTH_LONG).show();
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isService){
            unbindService(conn); // 서비스 종료
            isService = false;
        }
    }
}
