# Keep the public Artemis socket API and its Gson-backed wire models in release builds.
-keepattributes InnerClasses,Signature,RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations
-keep public class artemis.socket.** { public *; }
-keepclassmembers class artemis.socket.** { <fields>; }
