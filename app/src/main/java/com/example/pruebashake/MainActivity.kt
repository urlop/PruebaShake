package com.example.pruebashake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private lateinit var mShakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setupAvailableSensor()
        mShakeDetector = ShakeDetector()
        mShakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                handleShakeEvent(count)
            }
        })
    }

    private fun setupAvailableSensor(){
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            val gravSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_GRAVITY)
            // Use the version 3 gravity sensor.
            sensor =
                gravSensors.firstOrNull { it.vendor.contains("Google LLC") && it.version == 3 }
        }
        if (sensor == null) {
            // Use the accelerometer.
            sensor =
                if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                } else {
                    null
                }
        }
    }

    private fun handleShakeEvent(count: Int) {
        Toast.makeText(this, "Shaking $count times", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        if (sensor == null) return
        sensorManager.registerListener(
            mShakeDetector,
            sensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        if (sensor == null) return
        sensorManager.unregisterListener(mShakeDetector)
    }
}
