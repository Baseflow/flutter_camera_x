package com.baseflow.camera_x;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.camera.core.Preview.SurfaceProvider;
import androidx.camera.core.SurfaceRequest;
import io.flutter.view.TextureRegistry.SurfaceTextureEntry;
import java.util.concurrent.Executor;

class FlutterSurfaceProvider implements SurfaceProvider {
  private final Executor executor;
  private final SurfaceTextureEntry surfaceTextureEntry;

  public static final int MAX_PREVIEW_WIDTH = 1920;
  public static final int MAX_PREVIEW_HEIGHT = 1080;

  FlutterSurfaceProvider(SurfaceTextureEntry surfaceTextureEntry, Executor executor) {
    this.surfaceTextureEntry = surfaceTextureEntry;
    this.executor = executor;
  }

  @Override
  public void onSurfaceRequested(@NonNull SurfaceRequest request) {
    // Create the surface and attempt to provide it to the camera.
    SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
    surfaceTexture.setDefaultBufferSize(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT);

    // Provide the surface and wait for the result to clean up the surface.
    request.provideSurface(new Surface(surfaceTexture), executor, (result) -> {
    });
  }
}
