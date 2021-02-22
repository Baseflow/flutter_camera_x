package com.baseflow.camera_x;

import android.app.Activity;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** Camera_xPlugin */
public class Camera_xPlugin implements FlutterPlugin, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private final PermissionManager permissionManager;
  private MethodChannelImpl methodChannelImpl;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityBinding;

  public Camera_xPlugin() {
    this.permissionManager = new PermissionManager();
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    pluginBinding = flutterPluginBinding;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activityBinding = binding;
    registerActivity();

    methodChannelImpl = new MethodChannelImpl(
        activityBinding.getActivity(),
        pluginBinding.getBinaryMessenger(),
        pluginBinding.getTextureRegistry(),
        permissionManager
    );
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    deregisterActivity();
  }

  private void registerActivity() {
    final Activity activity = activityBinding.getActivity();

    activityBinding.addRequestPermissionsResultListener(permissionManager);
    permissionManager.setActivity(activity);
  }

  private void deregisterActivity() {
    if (activityBinding == null) return;

    activityBinding.removeRequestPermissionsResultListener(permissionManager);
    permissionManager.setActivity(null);
  }
}
