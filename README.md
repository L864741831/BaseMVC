一、
Android Studio版本低导入项目常见问题:
1.This Gradle plugin requires a newer IDE able to request IDE model level 3 报错。
解决方法：
（1）： 升级android studio IDE的版本到3.0以上。
（2）：在项目的gradle.properties配置文件中加入以下这句：
gradle.properties中：android.injected.build.model.only.versioned = 3

2.如果无法编译生成APK，更改build.gradle文件中
classpath 'com.android.tools.build:gradle:2.3.2' (studio版本号)

3.将编译版本，编译工具版本替换为已有版本
依赖名替换为3.0以下支持的名称
implementation替换为compile
testImplementation替换为testCompile
androidTestImplementation替换为
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })







二、使用config.gradle统一编译与依赖等版本号
1.新建工程BaseMvp，创建config.gradle文件，使用gradle统一编译与依赖等版本号。

compileSdkVersion:是你SDK的版本号，也就是API Level，例如API-19、API-20、API-21等等。真正决定代码是否能
编译的关键，比如设置成23，就无法使用httpclient,低版本编译出来的apk可以在高版本上运行，因为向下兼容，高
版本编译的apk运行到低版本，minSdkVersion的也没有问题，因为编译时就会考虑兼容性，低版本会有v4,v7这些兼
容包。

buildeToolVersion是你构建工具的版本，其中包括了打包工具aapt、dx等等。这个工具的目录位
于..your_sdk_path/build-tools/XX.XX.XX。一般设置为最新即可。

applicationId:Android应用的唯一标识，在Android设备和应用市场中中所有的应用程序的applicationId都是唯一
的，要对一个应用进行更新，必须保证applicationId的值相同，否则就认为是不同的应用。如果没有在gradle中设
置applicationId的值，则项目默认读取packageName作为applicationId的值。

minSdkVersion：最小支持版本，比如设置成15，就无法在低于15的版本运行。

targetSdkVersion：只是一个标示，如果targetSdkVersion与目标设备的API版本相同时，运行效率可能会高一些，
编译阶段没有实质性作用。

VersionCode：对消费者不可见，仅用于应用市场、程序内部识别版本，判断新旧等用途。
VersionName：展示给消费者，消费者会通过它认知自己安装的版本，下文提到的版本号都是说VersionName。

support：兼容包得版本一般要配置跟编译版本一致，比如编译版本配置为22，v7兼容包也要配置为22，否则会出错
。compile 'com.android.support:appcompat-v7:22+'

为了使高版本中新的效果、功能兼容旧有版本，使旧有版本一样可以使用新的功能效果，谷歌推出了这个support 兼
容包。为此google官方提供了Android Support Library package 系列的包来保证高版本sdk开发的向下兼容性。
gradle引用appcompat-v7包的时候就不需要引用v4了，因为v7里默认包含了v4包。






























三、搭建基于OkGo的OkGoHttpUtil网络请求工具类

向config.gradle中添加okgo依赖并引用

新建APP文件夹，新建App类继承Application，在清单文件中注册 Application。

初始化OkGo，https://github.com/jeasonlzy/okhttp-OkGo/wiki/Init

创建OkGoHttpUtil类，添加doGet静态方法，OkGo实现get请求实现类，MyParams自定义集合添加请求参数，KeyValue集合添加的键值对类。
ElementCallback实现AbsCallback回调接口,实现convertResponse，onSuccess方法，重写onStart，onFinish，onError方法。
Element为接口返回json对应实体类，参数规则根据接口来定。
HttpCallbackListener为自定义回调接口，BaseHttpCallbackListener为自定义回调接口实现类。
NetWorkUtil判断是否有可用网络等。
具体内容看注释。

添加doPost静态方法,与doGet类似，添加logger日志依赖。
HttpActivity为工具类调用示例。

添加支付宝jar包并依赖，使用服务器返回orderInfo进行异步调用与Handler支付结果处理。




 
 




































