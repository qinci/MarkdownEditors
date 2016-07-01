# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/shenqinci/Library/Android/sdk/tools/proguard/proguard-android.txt
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

#如果项目中用到了webview的复杂操作 ，最好加入
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void *(android.webkit.WebView,java.lang.String);
}

#本地方法
-keepclasseswithmembernames class * {
    native <methods>;
}

#View的构造函数
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#蒲公英
#-libraryjars libs/pgyer_sdk_2.2.2.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }


#View的构造函数
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#枚举不混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep class assets.** {*; }

#序列化值
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#序列化值
-keep class * implements java.ioSerializable {
    public static final java.ioSerializable *;
}

#注解框架butterknife
# https://github.com/JakeWharton/butterknife
# http://jakewharton.github.io/butterknife/
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#google
-dontwarn com.google.**
-keep class com.google.** { *; }

#BottomSheet
-dontwarn de.mrapp.android.**
-keep class de.mrapp.android.** { *; }

#Glide 图片框架  https://github.com/bumptech/glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#友盟
-dontwarn com.umeng.analytics.**
-keep class com.umeng.analytics.** { *; }

#LeakCanary内存检测
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }


-keep class com.loopj.** { *; }
####################################################

#指定代码的压缩级别
-optimizationpasses 3

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志∂
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*



#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.**
#如果引用了v4或者v7包
-dontwarn android.support.**

#忽略警告
-ignorewarning

##记录生成的日志数据,gradle build时在本项目根目录输出##


#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-verbose
-printmapping mapping.txt

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#不混淆资源类
-keep class **.R$* {
    *;
}
