// package com.whelksoft.flutter_native_timezone

// import android.os.Build
// import androidx.annotation.NonNull
// import io.flutter.embedding.engine.plugins.FlutterPlugin
// import io.flutter.plugin.common.BinaryMessenger
// import java.time.ZoneId
// import io.flutter.plugin.common.MethodCall
// import io.flutter.plugin.common.MethodChannel
// import io.flutter.plugin.common.MethodChannel.MethodCallHandler
// import io.flutter.plugin.common.MethodChannel.Result
// import io.flutter.plugin.common.PluginRegistry.Registrar
// import java.util.*
// import kotlin.collections.ArrayList

// class FlutterNativeTimezonePlugin : FlutterPlugin, MethodCallHandler {

//     private lateinit var channel: MethodChannel

//     // backward compatibility with flutter api v1
//     companion object {
//         @JvmStatic
//         fun registerWith(registrar: Registrar) {
//             val plugin = FlutterNativeTimezonePlugin()
//             plugin.setupMethodChannel(registrar.messenger())
//         }
//     }

//     override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//         setupMethodChannel(binding.binaryMessenger)
//     }

//     override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//         channel.setMethodCallHandler(null)
//     }

//     override fun onMethodCall(call: MethodCall, result: Result) {
//         when (call.method) {
//             "getLocalTimezone" -> result.success(getLocalTimezone())

//             "getAvailableTimezones" -> result.success(getAvailableTimezones())

//             else -> result.notImplemented()
//         }
//     }

//     private fun getLocalTimezone(): String {
//         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             ZoneId.systemDefault().id
//         } else {
//             TimeZone.getDefault().id
//         }
//     }

//     private fun getAvailableTimezones(): List<String> {
//         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             ZoneId.getAvailableZoneIds().toCollection(ArrayList())
//         } else {
//             TimeZone.getAvailableIDs().toCollection(ArrayList())
//         }
//     }

//     private fun setupMethodChannel(messenger: BinaryMessenger) {
//         channel = MethodChannel(messenger, "flutter_native_timezone")
//         channel.setMethodCallHandler(this)
//     }
// }


package com.whelksoft.flutter_native_timezone

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.time.ZoneId
import java.util.TimeZone

/** FlutterNativeTimezonePlugin */
class FlutterNativeTimezonePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_native_timezone")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "getLocalTimezone" -> {
        val timezone = TimeZone.getDefault().id
        result.success(timezone)
      }
      "getAvailableTimezones" -> {
        val timezones = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          ZoneId.getAvailableZoneIds().toList()
        } else {
          TimeZone.getAvailableIDs().toList()
        }
        result.success(timezones)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
