package kaka.com.jbox2dapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import kaka.com.jbox2dapp.widget.JboxView;


/**
 * Created by kaka on 2017/7/27.
 */
public class FirstActivity extends AppCompatActivity implements SensorEventListener{
    private JboxView jboxView;
    private SensorManager sensorManager;
    private Sensor defaultSensor;
    private int[] imgArr = {
            R.mipmap.heart,
            R.mipmap.flat,
            R.mipmap.cloud,
            R.mipmap.phone,
            R.mipmap.vlc,
            R.mipmap.linux
    };

    @Override
    public void onContentChanged() {
        jboxView = (JboxView) findViewById(R.id.jbox_view);
        initView();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < imgArr.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgArr[i]);
            imageView.setTag(R.id.dn_view_circle_tag, true);
            jboxView.addView(imageView,layoutParams);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1] * 2.0f;
            jboxView.onSensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
