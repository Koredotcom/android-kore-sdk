# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Retrofit annotations
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep all classes, fields, and methods in your library package
-keep class kore.botssdk.** { *; }

# Prevent renaming of all classes in your library
-keepnames class kore.botssdk.**

# Keep subscriber classes and methods
-keepclassmembers class ** {
    public void onEvent*(**);
}

# Keep Retrofit interfaces
-keep interface * { @retrofit2.http.* <methods>; }

# Keep classes that use Gson for serialization
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter { *; }
