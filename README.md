# RetrofitHLibrary
1.
allprojects {
    repositories {
        google()
        jcenter()
    }
}
2.
   /*网络请求框架*/
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    /*RxJava&RxAndroid*/
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    implementation 'io.reactivex.rxjava2:rxjava:2.1.3'
    /*RxLifecycle基础库*/
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'  
3.
   implementation project(':retrofitlibrary')
4.基本用法
 在Application中onCreate()方法里进行注册：
  必须：
  RetrofitHLibrary.init(this, BaseUrl);
  配置（可选）：
  RetrofitHLibrary.getmHttpConfigure().setTipDialog(true);　// 设置请求错误时 显示dialog（默认关闭）
  RetrofitHLibrary.getmHttpConfigure().showLog(true);// 设置打印日志 显示dialog（默认关闭）
  RetrofitHLibrary.getmHttpConfigure().setTimeOut(long);// 设置超时时长（默认60s）
  onTerminate()方法里进行注销：
  RetrofitHLibrary.onDestory();
5.
  RetrofitLibrary.getRetrofitHttp()
       .post()　//请求方式
       .apiUrl(url地址)
       .addParameter(map)　// 参数类型
       .build()
       .request(new HttpObserver<Ｔ(实体类)>(context, true, true) { //(Context, 是否加载弹窗, 点击返回键是否取消加载弹窗)多个重载方法
         
                /**
                 * 上传进度回调
                 *
                 * @param action       标识（请求不设置，默认是apiUrl）
                 * @param value        返回结果
                 */
                @Override
                public void onSuccess(String action, T value) {
               
                }
                
            });
            
实体类: 继承BaseResponseModel<Ｔ>
6.RetrofitHLibrary.getRetrofitHttp()
       .post()　//请求方式
       .apiUrl(url地址)
       .addParameter(map)　// 参数类型
       .file(上传文件)
       .build()
       .upload(new UploadObserver<T(实体类)>(context, false){//(Context, 是否加载弹窗)、无参数构造器
 
                @Override
                public void onSuccess(String action, T value) {
               
                }
                
                /**
                 * 上传进度回调
                 *
                 * @param file         源文件
                 * @param currentSize  当前上传值
                 * @param totalSize    总大小
                 * @param progress     进度
                 * @param currentIndex 当前下标
                 * @param totalFile    总文件数
                 */
                public void onProgress(File file, long currentSize, long totalSize, 
                               float progress, int currentIndex, int totalFile);
 
               }）
  
