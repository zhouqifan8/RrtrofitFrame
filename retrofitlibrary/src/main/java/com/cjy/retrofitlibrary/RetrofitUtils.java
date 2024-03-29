package com.cjy.retrofitlibrary;

import com.cjy.retrofitlibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.utils.RequestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工具类
 * 获取Retrofit 默认使用OkHttpClient
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class RetrofitUtils {

    private static RetrofitUtils instance = null;

    private RetrofitUtils() {

    }

    public static RetrofitUtils get() {
        RetrofitUtils retrofitUtils = instance;
        if (retrofitUtils == null) {
            synchronized (RetrofitUtils.class) {
                retrofitUtils = instance;
                if (retrofitUtils == null) {
                    instance = retrofitUtils = new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }

    /**
     * 获取Retrofit
     *
     * @param headerMap 请求头
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, Map<String, Object> headerMap) {
        Retrofit.Builder retrofit = new Retrofit.Builder();

        retrofit.client(getOkHttpClientBase(headerMap))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl
     * @param client
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, OkHttpClient client) {
        Retrofit.Builder retrofit = new Retrofit.Builder();

        retrofit.client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }


    /**
     * 获取OkHttpClient
     * 备注:下载时不能使用OkHttpClient单例,在拦截器中处理进度会导致多任务下载混乱
     *
     * @param newClient        是否新建 OkHttpClient
     * @param timeout
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClient(boolean newClient, long timeout, TimeUnit timeUnit, Interceptor... interceptorArray) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
//        if (newClient) {
//            okHttpClient = new OkHttpClient.Builder();
//        }
        //超时设置
        okHttpClient.connectTimeout(timeout, timeUnit)
                .writeTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit);

        /**
         * https设置
         * 备注:信任所有证书,不安全有风险
         */
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
//        okHttpClient.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        /**
         * 配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
         * 备注:google平台不允许直接返回true
         */
        //okHttpClient.hostnameVerifier(new HostnameVerifier() {        });

        //Interceptor设置
        if (interceptorArray != null) {
            for (Interceptor interceptor : interceptorArray) {
                okHttpClient.addInterceptor(interceptor);
            }
        }
        return okHttpClient.build();
    }


    /**
     * 获取下载时使用 OkHttpClient
     *
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClientDownload(Interceptor... interceptorArray) {
        final long timeout = Constants.TIME_OUT;//超时时长
        final TimeUnit timeUnit = TimeUnit.SECONDS;//单位秒
        return getOkHttpClient(true, timeout, timeUnit, interceptorArray);
    }

    /**
     * 获取基础Http请求使用 OkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClientBase(final Map<String, Object> headerMap) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> LogUtils.i("okHttp:" + message));
        //must
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        //Header 拦截器
        Interceptor headerInterceptor = chain -> {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();
            //统一设置 Header
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                    requestBuilder.addHeader(entry.getKey(), String.valueOf(RequestUtils.getHeaderValueEncoded(entry.getValue())));
                }
            }
            return chain.proceed(requestBuilder.build());
        };
        //网络请求拦截器
        Interceptor httpInterceptor = chain -> {
            Request request = chain.request();
            Response response;
            try {
                response = chain.proceed(request);
            } catch (final Exception e) {
                //httpObserver.onCanceled();
                LogUtils.w(e);
                throw e;
            }
            return response;
        };

        Interceptor[] interceptorArray = new Interceptor[]{logInterceptor, headerInterceptor, httpInterceptor};
        return getOkHttpClient(false, RetrofitHttp.Configure.get().getTimeout(), RetrofitHttp.Configure.get().getTimeUnit(), interceptorArray);
    }

}
