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

2.


