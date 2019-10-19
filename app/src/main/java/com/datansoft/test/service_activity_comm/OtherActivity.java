package com.datansoft.test.service_activity_comm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Intent passedIntent = getIntent();
        processIntent(passedIntent); // 새로 만들어 졌을 때 처리
    }

    @Override
    protected void onNewIntent(Intent intent){
        processIntent(intent); // 이미 만들어져 있을 때 호출된 경우 처리
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        Log.i("OtherActivity", "액티비티 초기화");
    }
}
