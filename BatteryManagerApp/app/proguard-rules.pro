# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in the build.gradle file.
#
# For more details, see
#   https://developer.android.com/topic/performance/shrinking

# Keep AndroidX DataStore
-keep class androidx.datastore.** { *; }

# Keep Kotlin Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep Material Components
-keep class com.google.android.material.** { *; }

# Keep custom views
-keep class com.google.android.material.slider.** { *; }

# Keep all public classes
-keep public class * extends androidx.lifecycle.ViewModel

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
    public *** <init>(...);
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}