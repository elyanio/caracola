# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class * extends java.lang.Enum {
}

-keep class com.google.common.io.Resources {
    public static <methods>;
}

-keep class com.google.common.collect.Lists {
    public static ** reverse(**);
}

-keep class com.google.common.base.Charsets {
    public static <fields>;
}

-keep class com.google.common.base.Joiner {
    public static com.google.common.base.Joiner on(java.lang.String);
    public ** join(...);
}

-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn com.google.j2objc.annotations.RetainedWith
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.LazyInit
-dontwarn com.google.errorprone.annotations.concurrent.LazyInit
-dontwarn com.google.errorprone.annotations.ForOverride

#-dontwarn rx.internal.**
#-dontwarn android.support.**
#-dontwarn sun.misc.Unsafe
#-dontwarn com.google.common.collect.MinMaxPriorityQueue
#-dontwarn com.google.errorprone.annotations.**
#-dontwarn com.google.j2objc.**
#-dontwarn java.lang.**
#-keep class com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerController
#-keep class com.codetroopers.betterpickers.calendardatepicker.MonthAdapter$CalendarDay
#-keep class com.codetroopers.betterpickers.calendardatepicker.MonthView$OnDayClickListener
#-keep class com.codetroopers.betterpickers.radialtimepicker.RadialPickerLayout$OnValueSelectedListener
#-keep class com.codetroopers.betterpickers.widget.PickerLinearLayout
#-keep class org.threeten.bp.LocalDate
#-keep class com.polymitasoft.caracola.components.InteractivoScrollView$CapturadorEventoMoverScroll
#-keep class com.polymitasoft.caracola.datamodel.Bedroom
#-keep class com.polymitasoft.caracola.view.booking.VistaDia
#-keep class com.polymitasoft.caracola.datamodel.Booking
#-keep class com.polymitasoft.caracola.datamodel.InternalService
#-keep class com.polymitasoft.caracola.view.service.InternalServiceSelectorView$OnSelectedServiceListener
#-keep class com.polymitasoft.caracola.datamodel.Supplier
#-keep class net.sqlcipher.database.SQLiteDebug$PagerStats