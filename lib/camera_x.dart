import 'dart:async';

import 'package:flutter/services.dart';

class Camera_x {
  static const MethodChannel _channel = const MethodChannel('camera_x');

  static Future<bool> get hasPermission =>
      _channel.invokeMethod('hasPermission');

  static Future<bool> get requestPermission =>
      _channel.invokeMethod('requestPermission');

  static Future<int> get startCamera => _channel.invokeMethod('startCamera');
}
