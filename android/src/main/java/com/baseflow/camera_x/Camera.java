package com.baseflow.camera_x;

import android.app.Activity;
import android.util.Log;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import io.flutter.view.TextureRegistry.SurfaceTextureEntry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Camera {
  private final static String TAG = "Camera";
  private final Activity activity;
  private final FlutterSurfaceProvider surfaceProvider;
  private final ExecutorService cameraExecutor;

  Camera(
      Activity activity,
      SurfaceTextureEntry surfaceTextureEntry) {
    this.activity = activity;

    cameraExecutor = Executors.newSingleThreadExecutor();
    this.surfaceProvider = new FlutterSurfaceProvider(
        surfaceTextureEntry,
        cameraExecutor);
  }

  void startCamera() {
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(activity);

    cameraProviderFuture.addListener(
        () -> {
          ProcessCameraProvider cameraProvider = null;
          try {
            cameraProvider = cameraProviderFuture.get();
          } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Camera Access Error", e);
            return;
          }

          Preview preview = new Preview.Builder().build();
          preview.setSurfaceProvider(surfaceProvider);

          CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

          try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                (LifecycleOwner)activity,
                cameraSelector,
                preview);
          } catch (Exception e) {
            Log.e(TAG, "Use case binding failed", e);
          }
        },
        ContextCompat.getMainExecutor(activity)
    );
  }
}
