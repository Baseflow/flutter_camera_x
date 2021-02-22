package com.baseflow.camera_x;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.flutter.plugin.common.PluginRegistry;
import javax.xml.transform.Result;

class PermissionManager implements PluginRegistry.RequestPermissionsResultListener {

  interface ResultCallback {
    void onResult(String errorCode, String errorDescription);
  }

  private final static int PERMISSION_REQUEST_CODE = 127;
  private Activity activity;
  private ResultCallback resultCallback;

  public void setActivity(@Nullable Activity activity) {
    this.activity = activity;
  }

  public boolean hasPermissions() {
    final int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

    return permission == PackageManager.PERMISSION_GRANTED;
  }

  public void requestPermission(@NonNull ResultCallback resultCallback) {
    if (activity == null) {
      resultCallback.onResult("NO_ACTIVITY", "Permissions requested without activity");
      return;
    }

    this.resultCallback = resultCallback;

    ActivityCompat.requestPermissions(
        activity,
        new String[] { Manifest.permission.CAMERA },
        PERMISSION_REQUEST_CODE);
  }

  @Override
  public boolean onRequestPermissionsResult(
      int requestCode,
      String[] permissions,
      int[] grantResults) {
    if (requestCode != PERMISSION_REQUEST_CODE) {
      return false;
    }

    final boolean hasPermission = hasPermissions();

    if (resultCallback != null) {
      if (hasPermission) {
        resultCallback.onResult(null, null);
      } else {
        resultCallback.onResult(
            "PERMISSION_DENIED",
            "Permissions to access the camera have been denied.");
      }
    }

    return hasPermission;
  }
}
