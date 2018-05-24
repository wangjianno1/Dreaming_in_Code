package com.sohu.testmaven.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/** 
 * HTTP/HTTPS���󹤾��� 
 * 
 * @author : wangjianno1@sina.com 
 * @version : 1.0.0 
 * @date : 2018/05/23
 * @see : https://blog.csdn.net/happylee6688/article/details/47148227
 * <dependency>
 *     <groupId>org.apache.httpcomponents</groupId>
 *     <artifactId>httpclient</artifactId>
 *     <version>4.5.3</version>
 * </dependency>
 */  

public class HttpUtils {

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

    static {
        // �������ӳ�
        connMgr = new PoolingHttpClientConnectionManager();
        // �������ӳش�С
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // �������ӳ�ʱ
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // ���ö�ȡ��ʱ
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // ���ô����ӳػ�ȡ����ʵ���ĳ�ʱ
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // ���ύ����֮ǰ ���������Ƿ����
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    /**
     * ����HTTP GET�������������
     * @param reqUrl
     * @return
     */
    public static String doGet(String reqUrl) {
        return doGet(reqUrl, new HashMap<String, Object>());
    }

    /**
     * ����HTTP GET���󣬲�����K-V��ʽ
     * @param reqUrl
     * @param reqParams
     * @return
     */
    public static String doGet(String reqUrl, Map<String, Object> reqParams) {
        String strParams = "";
        for (String name : reqParams.keySet()) {
            String value = (String) reqParams.get(name);
            String nameValuePair = "".equals(strParams) ? String.format("?%s=%s", name, value)
                    : String.format("&%s=%s", name, value);
            strParams += nameValuePair;
        }
        reqUrl += strParams;

        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(reqUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ����HTTP POST���󣬲����������
     * @param reqUrl
     * @return
     */
    public static String doPost(String reqUrl) {
        return doPost(reqUrl, new HashMap<String, Object>());
    }

    /**
     * ����HTTP POST���󣬲�����K-V��ʽ
     * @param reqUrl API�ӿ�URL
     * @param reqParams ����map
     * @return
     */
    public static String doPost(String reqUrl, Map<String, Object> reqParams) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : reqParams.entrySet()) {
                NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                nameValuePairList.add(nvp);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, Charset.forName("UTF-8")));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ����HTTP POST���󣬲�����JSON��ʽ
     * @param reqUrl
     * @param reqJson json����
     * @return
     */
    public static String doPost(String reqUrl, Object reqJson) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.setConfig(requestConfig);
            StringEntity strEntity = new StringEntity(reqJson.toString(), "UTF-8");//���������������
            strEntity.setContentEncoding("UTF-8");
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ����HTTPS POST���󣬲�����K-V��ʽ
     * @param reqUrl API�ӿ�URL
     * @param reqParams ����map
     * @return
     */
    public static String doPostSSL(String reqUrl, Map<String, Object> reqParams) {
        String result = null;
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(createSSLConnSocketFactory());
        httpClientBuilder.setConnectionManager(connMgr);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : reqParams.entrySet()) {
                NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                nameValuePairList.add(nvp);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, Charset.forName("utf-8")));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ����HTTPS POST ���󣬲�����JSON��ʽ
     * @param reqUrl API�ӿ�URL
     * @param reqJson JSON����
     * @return
     */
    public static String doPostSSL(String reqUrl, Object reqJson) {
        String result = null;
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(createSSLConnSocketFactory());
        httpClientBuilder.setConnectionManager(connMgr);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.setConfig(requestConfig);
            StringEntity strEntity = new StringEntity(reqJson.toString(), "UTF-8"); // ���������������
            strEntity.setContentEncoding("UTF-8");
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ����SSL��ȫ����
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslConnSocketFactory = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslConnSocketFactory = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslConnSocketFactory;
    }

    /**
     * ���Է���
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("===============�Բ���=================");
        String result1 = HttpUtils.doGet("http://httpbin.org/get");
        System.out.println("doGet���Խ��==>" + result1);
        
        Map<String, Object> reqParams = new HashMap<String, Object>();
        reqParams.put("show_env", "1");
        String result2 = HttpUtils.doGet("http://httpbin.org/get", reqParams);
        System.out.println("����doGet���Խ��==>" + result2);
        
        String result3 = HttpUtils.doPost("http://httpbin.org/post");
        System.out.println("doPost���Խ��==>" + result3);
        
        String result4 = HttpUtils.doPost("http://httpbin.org/post", reqParams);
        System.out.println("����doPost���Խ��==>" + result4);
        
        String result5 = HttpUtils.doPostSSL("https://httpbin.org/post", new HashMap<String, Object>());
        System.out.println("HTTPS doPost���Խ��==>" + result5);
    }
}
