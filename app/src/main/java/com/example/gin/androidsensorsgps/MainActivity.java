package com.example.gin.androidsensorsgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtLatitude, txtLongtitude, txtAccuracy;
    private TextView xAxisText, yAxisText, zAxisText;
    private TextView txtGyroX, txtGyroY, txtGyroZ;

    private RadioGroup radioGroupSpeed;
    private RadioButton speed1,speed2;

    //SENSORS
    private SensorManager sensorManagerAccelerometer, sensorManagerGyroscope;
    private Sensor sensorAccelerometer, sensorGyro;

    private static final int PERMISSION_REQUEST_CODE = 1;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xAxisText = (TextView)findViewById(R.id.xText);
        yAxisText = (TextView)findViewById(R.id.yText);
        zAxisText = (TextView)findViewById(R.id.zText);

        txtGyroX = (TextView)findViewById(R.id.txtGyroX);
        txtGyroY = (TextView)findViewById(R.id.txtGyroY );
        txtGyroZ = (TextView)findViewById(R.id.txtGyroZ);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongtitude = (TextView) findViewById(R.id.txtLongtitude);
        txtAccuracy = (TextView) findViewById(R.id.txtAccuracy);


        //RADIOGROUPS & RADIOBUTTONS
        radioGroupSpeed = (RadioGroup) findViewById(R.id.radioGroupSpeed);
        speed1 = (RadioButton) findViewById(R.id.speed1);
        speed2 = (RadioButton) findViewById(R.id.speed2);
        radioGroupSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.speed1)
                {
                    sensorManagerAccelerometer.unregisterListener(accelerometerListener);
                    sensorManagerGyroscope.unregisterListener(gyroListener);

                    sensorManagerAccelerometer.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManagerGyroscope.registerListener(gyroListener, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
                }
                else if(checkedId == R.id.speed2)
                {
                    sensorManagerAccelerometer.unregisterListener(accelerometerListener);
                    sensorManagerGyroscope.unregisterListener(gyroListener);

                    sensorManagerAccelerometer.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManagerGyroscope.registerListener(gyroListener, sensorGyro, SensorManager.SENSOR_DELAY_FASTEST);
                }
            }
        });

        //SENSORS
        //SENSOR ACCELEROMETER
        sensorManagerAccelerometer = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManagerAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //SENSOR GYROSCOPE
        sensorManagerGyroscope = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorGyro = sensorManagerGyroscope.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        GPSPermission();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtLatitude.setText("Current Latitude: " + location.getLatitude());
                txtLongtitude.setText("Current Longitude: " + location.getLongitude());
                txtAccuracy.setText("Accuracy: " + location.getAccuracy());
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider)
            {
                Toast.makeText(MainActivity.this, "GPS ENABLED", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onProviderDisabled(String provider)
            {
                Toast.makeText(MainActivity.this, "GPS DISABLED", Toast.LENGTH_SHORT).show();
            }
        });
           }

    //ACCELEROMETER LISTENER
    public SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            xAxisText.setText("Accelerometer-X: " + event.values[0]);
            yAxisText.setText("Accelerometer-Y: " + event.values[1]);
            zAxisText.setText("Accelerometer-Z: " + event.values[2]);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    //GYRO LISTENER
    public SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            txtGyroX.setText("Gyro-X: " + event.values[0]);
            txtGyroY.setText("Gyro-Y: " + event.values[1]);
            txtGyroZ.setText("Gyro-Z: " + event.values[2]);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void GPSPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
}
