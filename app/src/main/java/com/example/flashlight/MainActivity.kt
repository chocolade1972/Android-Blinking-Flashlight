package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var flashLightStatus: Boolean = false
    var flashLightOn = false
    var counter: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val seek = findViewById<SeekBar>(R.id.seekBar)
        seek.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Log.d("seekbar", "Your Progress: ${seekBar?.progress}"

                    counter = progress.toLong()
                    textView.text = counter.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            },
        )

        startFlashLight()

    }


    override fun onBackPressed() {

        finish()
        System.exit(0)

    }

    private fun openFlashLight() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false
            } catch (e: CameraAccessException) {
            }
        }
    }

    private fun closeFlashLight()
    {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true
            } catch (e: CameraAccessException) {
            }
        }
    }

    fun startFlashLight() {
        openFlashLight()
        flashLightOn = true

        handler.removeCallbacksAndMessages(null)

        handler.postDelayed(object: Runnable {
            override fun run() {
                if (flashLightOn) {
                    closeFlashLight()
                } else {
                    openFlashLight()
                }

                // Changing the toggle and calling the same Runnable again after 5 seconds
                flashLightOn = !flashLightOn
                handler.postDelayed(this, counter)
            }
        }, counter)
    }
}