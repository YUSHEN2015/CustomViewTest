package cn.ac.iscas.customviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ac.iscas.customviewtest.view.CustomCircleControlBar;
import cn.ac.iscas.customviewtest.view.CustomCircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private CustomCircleControlBar mCustomCircleControlBar;
    private CustomCircleProgressBar mCustomCircleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomCircleControlBar = (CustomCircleControlBar)findViewById(R.id.test3);
        mCustomCircleControlBar.setCurrentCount(10);
        mCustomCircleProgressBar = (CustomCircleProgressBar)findViewById(R.id.test4);
        mCustomCircleProgressBar.setCurrentCount(20);
    }

    public void CustomLayout(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
