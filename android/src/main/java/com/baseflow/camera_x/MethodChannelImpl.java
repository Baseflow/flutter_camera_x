package com.baseflow.camera_x;

import android.app.Activity;
import androidx.annotation.NonNull;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.view.TextureRegistry;
import io.flutter.view.TextureRegistry.SurfaceTextureEntry;

class MethodChannelImpl implements MethodChannel.MethodCallHandler {
  private final Activity activity;
  private final BinaryMessenger messenger;
  private final TextureRegistry textureRegistry;
  private final PermissionManager permissionManager;
  private final MethodChannel methodChannel;

  private Camera camera;

  MethodChannelImpl(
      @NonNull Activity activity,
      @NonNull BinaryMessenger messenger,
      @NonNull TextureRegistry textureRegistry,
      @NonNull PermissionManager permissionManager
  ) {
    this.activity = activity;
    this.messenger = messenger;
    this.textureRegistry = textureRegistry;
    this.permissionManager = permissionManager;

    methodChannel = new MethodChannel(messenger, "camera_x");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    if (call.method.equals("hasPermission")) {
      result.success(permissionManager.hasPermissions());
    } else if (call.method.equals("requestPermission")) {
      permissionManager.requestPermission(
          (errorCode, errorDescription) -> {
            if (errorCode == null) {
              result.success(true);
            } else {
              result.error(errorCode, errorDescription, null);
            }
          }
      );
    } else if (call.method.equals("startCamera")) {
      permissionManager.requestPermission(
          ((errorCode, errorDescription) -> {
            if(errorCode == null) {
              SurfaceTextureEntry surfaceTextureEntry = textureRegistry.createSurfaceTexture();
              camera = new Camera(activity, surfaceTextureEntry);
              camera.startCamera();

              result.success(surfaceTextureEntry.id());
            } else {
              result.error(errorCode, errorDescription, null);
            }
          })
      );
    } else {
      result.notImplemented();
    }
  }
}
