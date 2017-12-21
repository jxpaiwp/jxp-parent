package com.jxp.common.httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class httpclientUtils {


    private static final Logger LOG = LogManager.getLogger(httpclientUtils.class);

    private static final int CONNECT_TIMEOUT = 5000;//设置超时毫秒数

    private static final int SOCKET_TIMEOUT = 10000;//设置传输毫秒数

    private static final int REQUESTCONNECT_TIMEOUT = 3000;//获取请求超时毫秒数

    private static final int CONNECT_TOTAL = 200;//最大连接数

    private static final int CONNECT_ROUTE = 20;//设置每个路由的基础连接数

    private static final int VALIDATE_TIME = 30000;//设置重用连接时间

    private static final String RESPONSE_CONTENT = "通信失败";

    private static PoolingHttpClientConnectionManager manager = null;

    private static CloseableHttpClient client = null;

    static {
        ConnectionSocketFactory csf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory lsf = createSSLConnSocketFactory();
        Registry registry = RegistryBuilder.create()
                .register("http", csf).register("https", lsf).build();
        manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(CONNECT_TOTAL);
        manager.setDefaultMaxPerRoute(CONNECT_ROUTE);
        manager.setValidateAfterInactivity(VALIDATE_TIME);
        SocketConfig config = SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build();
        manager.setDefaultSocketConfig(config);
        RequestConfig requestConf = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(REQUESTCONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        client = HttpClients.custom().setConnectionManager(manager).setDefaultRequestConfig(requestConf).setRetryHandler(
                (exception, executionCount, context) -> {
                    if(executionCount >= 3)
                        return false;
                    if(exception instanceof NoHttpResponseException)//如果服务器断掉了连接那么重试
                        return true;
                    if(exception instanceof SSLHandshakeException)//不重试握手异常
                        return false;
                    if(exception instanceof InterruptedIOException)//IO传输中断重试
                        return true;
                    if(exception instanceof UnknownHostException)//未知服务器
                        return false;
                    if(exception instanceof ConnectTimeoutException)//超时就重试
                        return true;
                    if(exception instanceof SSLException)
                        return false;

                    HttpClientContext cliContext = HttpClientContext.adapt(context);
                    HttpRequest request = cliContext.getRequest();
                    if(!(request instanceof HttpEntityEnclosingRequest))
                        return true;
                    return false;
                }).build();
        if(manager!=null && manager.getTotalStats()!=null)
            LOG.info("客户池状态："+manager.getTotalStats().toString());
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        SSLContext context;
        try {
            context = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslsf = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("SSL上下文创建失败，由于" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return sslsf;
    }

    private String res(HttpRequestBase method) {
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse response = null;
        String content = RESPONSE_CONTENT;
        try {
            response = client.execute(method, context);//执行GET/POST请求
            HttpEntity entity = response.getEntity();//获取响应实体
            if(entity!=null) {
                Charset charset = ContentType.getOrDefault(entity).getCharset();
                content = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);
            }
        } catch(ConnectTimeoutException cte) {
            LOG.error("请求连接超时，由于 " + cte.getLocalizedMessage());
            cte.printStackTrace();
        } catch(SocketTimeoutException ste) {
            LOG.error("请求通信超时，由于 " + ste.getLocalizedMessage());
            ste.printStackTrace();
        } catch(ClientProtocolException cpe) {
            LOG.error("协议错误（比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合），由于 " + cpe.getLocalizedMessage());
            cpe.printStackTrace();
        } catch(IOException ie) {
            LOG.error("实体转换异常或者网络异常， 由于 " + ie.getLocalizedMessage());
            ie.printStackTrace();
        } finally {
            try {
                if(response!=null) {
                    response.close();
                }

            } catch(IOException e) {
                LOG.error("响应关闭异常， 由于 " + e.getLocalizedMessage());
            }

            if(method!=null) {
                method.releaseConnection();
            }

        }

        return content;
    }

    public String get(String url) {
        HttpGet get = new HttpGet(url);
        return res(get);
    }

    public String get(String url, String cookie) {
        HttpGet get = new HttpGet(url);
        if(StringUtils.isNotBlank(cookie))
            get.addHeader("cookie", cookie);
        return res(get);
    }

    public byte[] getAsByte(String url) {
        return get(url).getBytes();
    }

    public String getHeaders(String url, String cookie, String headerName) {
        HttpGet get = new HttpGet(url);
        if(StringUtils.isNotBlank(cookie))
            get.addHeader("cookie", cookie);
        res(get);
        Header[] headers = get.getHeaders(headerName);
        return headers == null ? null : headers.toString();
    }

    public String getWithRealHeader(String url) {
        HttpGet get = new HttpGet(url);
        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        get.addHeader("Accept-Language", "zh-cn");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        get.addHeader("Keep-Alive", "300");
        get.addHeader("Connection", "Keep-Alive");
        get.addHeader("Cache-Control", "no-cache");
        return res(get);
    }


}
