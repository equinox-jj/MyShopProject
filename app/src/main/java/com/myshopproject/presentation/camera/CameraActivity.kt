package com.myshopproject.presentation.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import com.myshopproject.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private lateinit var cameraExecutor: CameraXExecutors
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}