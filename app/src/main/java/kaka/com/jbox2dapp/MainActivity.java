package kaka.com.jbox2dapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        bt1 = (Button) findViewById(R.id.bt1);
    }

    public void firstClick(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,FirstActivity.class);
        startActivity(intent);
    }


}
