# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class app.kodatek.keygenfm.**$$serializer { *; }
-keepclassmembers class app.kodatek.keygenfm.** {
    *** Companion;
}
-keepclasseswithmembers class app.kodatek.keygenfm.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Media3
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**
