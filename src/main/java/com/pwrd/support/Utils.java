package com.pwrd.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.message.ParameterizedMessage;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class Utils {
    /**
     * MD5加密
     *
     * @param str 被加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String str) {
        try {
            //处理空值
            if (str == null) str = "";

            //编码
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes("UTF-8"));
            byte[] rst = digest.digest();

            //转换为字符串
            int len = rst.length;
            char[] cs = new char[len * 2];
            int index = 0;
            for (int i = 0; i < len; i++) {
                byte byte0 = rst[i];
                cs[index++] = MD5_HEX_DIGITS[byte0 >>> 4 & 0xf];
                cs[index++] = MD5_HEX_DIGITS[byte0 & 0xf];
            }

            return new String(cs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] MD5_HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 基于参数创建字符串
     *
     * @param str
     * @param params
     * @return
     */
    public static String createStr(String str, Object... params) {
        return ParameterizedMessage.format(str, params);
    }

    /**
     * 读取配置文件
     *
     * @return
     */
    public static Properties readProperties(String name) {
        String filePath = Utils.class.getClassLoader().getResource(name).getPath();
        try (FileInputStream in = new FileInputStream(filePath)) {
            Properties p = new Properties();
            p.load(in);

            return p;
        } catch (Exception e) {
            throw new SysException(e);
        }
    }

    /**
     * 构造List对象
     * <p>
     * 如果传入的是参数仅仅为一个对象数组(Object[])或原生数组(int[], long[]等)
     * 那么表现结果表现是不同的，Object[]为[obj[0], obj[1], obj[2]]
     * 而原生数组则为[[int[0], int[1]，int[2]]]
     * 多了一层嵌套，需要对原生数组进行特殊处理。
     *
     * @param <T>
     * @param ts
     * @return
     */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> ofList(T... ts) {
        List result = new ArrayList();

        //对Null进行特殊处理
        if (ts == null) {
            result.add(null);
            return result;
        }

        //对单独的原始数组类型进行特殊处理
        if (ts.length == 1 && ts[0] != null && OFLIST_ARRAY_CLASS.contains(ts[0].getClass())) {
            if (ts[0] instanceof int[]) {
                int[] val = (int[]) ts[0];
                for (int v : val) {
                    result.add(v);
                }
            } else if (ts[0] instanceof long[]) {
                long[] val = (long[]) ts[0];
                for (long v : val) {
                    result.add(v);
                }
            } else if (ts[0] instanceof boolean[]) {
                boolean[] val = (boolean[]) ts[0];
                for (boolean v : val) {
                    result.add(v);
                }
            } else if (ts[0] instanceof byte[]) {
                byte[] val = (byte[]) ts[0];
                for (byte v : val) {
                    result.add(v);
                }
            } else if (ts[0] instanceof double[]) {
                double[] val = (double[]) ts[0];
                for (double v : val) {
                    result.add(v);
                }
            }
        } else {    //对象数组
            for (T t : ts) {
                result.add(t);
            }
        }

        return result;
    }

    //专供ofList类使用 对于数组类型进行特殊处理
    private static final List<?> OFLIST_ARRAY_CLASS = Utils.ofList(int[].class, long[].class, boolean[].class, byte[].class, double[].class);

    /**
     * 构造Map对象
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> ofMap(Object... params) {
        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();

        //无参 返回空即可
        if (params == null || params.length == 0) {
            return result;
        }

        //处理成对参数
        int len = params.length;
        for (int i = 0; i < len; i += 2) {
            K key = (K) params[i];
            V val = (V) params[i + 1];

            result.put(key, val);
        }

        return result;
    }

    /**
     * String转为int型
     * 如果出错 则为0
     *
     * @param value
     * @return
     */
    public static int intValue(String value) {
        Integer v = Ints.tryParse(value);

        return v == null ? 0 : v;
    }

    public static int intValue(Integer value) {
        if (null == value)
            return 0;
        else
            return value;
    }

    /**
     * String转为long型
     * 如果出错 则为0
     *
     * @param value
     * @return
     */
    public static long longValue(String value) {
        Long v = Longs.tryParse(value);

        return v == null ? 0L : v;
    }

    public static long longValue(Long value) {
        if (null == value)
            return 0L;
        else
            return value;
    }

    /**
     * String转为double型
     * 如果出错 则为0.0
     *
     * @param value
     * @return
     */
    public static double doubleValue(String value) {
        Double v = Doubles.tryParse(value);

        return v == null ? 0.0 : v;
    }

    public static double doubleValue(Double value) {
        if (null == value)
            return 0.0;
        else
            return value;
    }

    /**
     * String转为boolean型
     * 如果出错 则为false
     *
     * @param value
     * @return
     */
    public static boolean booleanValue(String value) {
        if ("true".equalsIgnoreCase(value))
            return true;
        else
            return false;
    }

    /**
     * 进行Get请求操作
     *
     * @return
     */
    public static String httpGet(String url, Map<String, String> params) {
        try {
            //默认参数
            if (params == null) params = new HashMap<>();

            //1 拼接地址
            StringBuilder urlSB = new StringBuilder(url);
            //1.1 有需要拼接的参数
            if (!params.isEmpty()) {
                urlSB.append("?");
            }

            //1.2 拼接参数
            for (Entry<String, String> entry : params.entrySet()) {
                Object value = entry.getValue();
                String v = (value == null) ? "" : URLEncoder.encode(entry.getValue().toString(), "UTF-8");

                urlSB.append(entry.getKey()).append("=").append(v).append("&");
            }

            //1.3 最终地址
            String urlStrFinal = urlSB.toString();

            //1.4 去除末尾的&
            if (urlStrFinal.endsWith("&")) {
                urlStrFinal = urlStrFinal.substring(0, urlStrFinal.length() - 1);
            }

            //请求地址
            HttpGet get = new HttpGet(urlStrFinal);

            //准备环境
            try (CloseableHttpClient http = HttpClients.createDefault();
                 CloseableHttpResponse response = http.execute(get);) {
                //返回内容
                HttpEntity entity = response.getEntity();

                //主体数据
                InputStream in = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                //读取
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            }
        } catch (Exception e) {
            throw new SysException(e);
        }
    }


    /**
     * 进行异步Get请求操作
     *
     * @param url
     * @param params
     * @return
     */
    public static void httpGetAsync(String url, Map<String, String> params, int socketTimeOut, int connectTimeOut, Consumer<String> consumer) {
        try {
            //默认参数
            if (params == null) params = new HashMap<>();

            //1 拼接地址
            StringBuilder urlSB = new StringBuilder(url);
            //1.1 有需要拼接的参数
            if (!params.isEmpty()) {
                urlSB.append("?");
            }

            //1.2 拼接参数
            for (Entry<String, String> entry : params.entrySet()) {
                Object value = entry.getValue();
                String v = (value == null) ? "" : URLEncoder.encode(entry.getValue().toString(), "UTF-8");

                urlSB.append(entry.getKey()).append("=").append(v).append("&");
            }

            //1.3 最终地址
            String urlStrFinal = urlSB.toString();

            //1.4 去除末尾的&
            if (urlStrFinal.endsWith("&")) {
                urlStrFinal = urlStrFinal.substring(0, urlStrFinal.length() - 1);
            }

            //请求地址
            HttpGet httpGet = new HttpGet(urlStrFinal);

            //异步请求
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(socketTimeOut)
                    .setConnectTimeout(connectTimeOut).build();
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            try {
                httpclient.start();
                final CountDownLatch latch = new CountDownLatch(1);
                httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {

                    public void completed(final HttpResponse response2) {
                        latch.countDown();
                        try {
                            //返回内容
                            HttpEntity entity = response2.getEntity();
                            //主体数据
                            InputStream in = entity.getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                            //读取
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            String str = sb.toString();
                            //返回处理
                            consumer.accept(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void failed(final Exception ex) {
                        latch.countDown();
                    }

                    public void cancelled() {
                        latch.countDown();
                    }

                });
                latch.await();

            } finally {
                httpclient.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过HTTPS请求获取json格式的返回
     *
     * @param urlStr
     * @return
     */
    public static String httpsGet(String urlStr, Map<String, String> params) {
        String html = null;
        try {
            if (params == null) params = new HashMap<String, String>();

            StringBuilder sb = new StringBuilder(urlStr);

            if (!params.isEmpty()) {
                sb.append("?");
            }

            for (Entry<String, String> entry : params.entrySet()) {
                Object value = entry.getValue();
                String v = (value == null) ? "" : URLEncoder.encode(entry.getValue().toString(), "UTF-8");

                sb.append(entry.getKey()).append("=").append(v).append("&");
            }

            String urlStrFinal = sb.toString();

            //去除末尾的&
            if (urlStrFinal.endsWith("&")) {
                urlStrFinal = urlStrFinal.substring(0, urlStrFinal.length() - 1);
            }

            HttpClient httpclient = new DefaultHttpClient();
            httpclient = wrapHttpsClient(httpclient);
            HttpGet httpGet = new HttpGet(urlStrFinal);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                try (InputStream instreams = httpEntity.getContent()) {
                    html = httpsGetConvertStreamToString(instreams);
                    httpGet.abort();
                }
            }
            return html;
        } catch (Exception e) {
            throw new SysException("返回内容为:" + html, e);
        }
    }

    /**
     * 解决部分乱码情况
     *
     * @param is
     * @return
     */
    private static String httpsGetConvertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            throw new SysException(e);
        }

        return sb1.toString();
    }

    /**
     * 构造一个可以接受任意HTTPS协议的client
     *
     * @param base
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HttpClient wrapHttpsClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 进行Post请求操作
     *
     * @return
     */
    public static String httpPost(String url, Map<String, String> params) {
        try {
            //默认参数
            if (params == null) params = new HashMap<>();

            //参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : params.entrySet()) {
                Object key = entry.getKey();
                Object val = entry.getValue();
                String valStr = (val == null) ? "" : val.toString();

                nvps.add(new BasicNameValuePair(key.toString(), valStr));
            }

            //请求地址
            HttpPost post = new HttpPost(url);
            //设置参数
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            //准备环境
            try (CloseableHttpClient http = HttpClients.createDefault();
                 CloseableHttpResponse response = http.execute(post);) {

                //返回内容
                HttpEntity entity = response.getEntity();

                //主体数据
                InputStream in = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //读取
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            }
        } catch (Exception e) {
            throw new SysException(e);
        }
    }

    /**
     * 获取对象的属性
     * 会先尝试利用getter方法获取 然后再直接访问字段属性
     * 如果给定的属性不存在 会返回null
     *
     * @param obj
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fieldRead(Object obj, String fieldName) {
        try {
            //返回值
            Object result = null;

            Class<? extends Object> clazz = obj.getClass();

            //先通过自省来获取字段的值(getter方法)
            boolean hasGetter = false;
            BeanInfo bi = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for (PropertyDescriptor p : pds) {
                if (!p.getName().equals(fieldName)) continue;

                result = p.getReadMethod().invoke(obj);
                hasGetter = true;
            }

            //如果通过getter方法没找到 那么就尝试直接读取字段
            if (!hasGetter) {
                for (Field f : clazz.getFields()) {
                    if (!f.getName().equals(fieldName)) continue;

                    result = f.get(obj);
                }
            }

            return (T) result;
        } catch (Exception e) {
            throw new SysException(e);
        }
    }


    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";  
    
	/*
     * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
	 * @return 
	 * @throws Exception
	 * 
	  public static String initMacKey() throws Exception {
	  //得到一个 指定算法密钥的密钥生成器
	  KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME); 
	  //生成一个密钥
	  SecretKey secretKey =keyGenerator.generateKey();
	  return null;
	  }
	 */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    // 哈希分布增强
    public static int hash(int hash) {
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    /**
     * 普通返回
     *
     * @param response
     * @param retMap
     */
    public static void commonOut(HttpServletResponse response, Map<String, Object> retMap) {
        try {
            String retJson = JSON.toJSON(retMap).toString();
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(retJson);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回内容至客户端
     *
     * @param resultCode
     * @param resultMsg
     * @param resultData
     * @throws IOException
     */
    public static void syncOut(HttpServletResponse response, int resultId, long resultCode, String resultMsg, Object resultData, Object extendData) {
        response.setHeader("content-type", "text/plain;charset=UTF-8");
        try (OutputStream outputStream = response.getOutputStream()) {
            JSONObject resultObj = new JSONObject();
            resultObj.put("resultCode", resultCode);
            resultObj.put("resultId", resultId);
            resultObj.put("resultMsg", resultMsg);

            if (resultData == null) {
                resultObj.put("resultData", "");
            } else {
                resultObj.put("resultData", resultData);
            }

            if (extendData == null) {
                resultObj.put("extendData", "");
            } else {
                resultObj.put("extendData", extendData);
            }

            String returnStr = resultObj.toJSONString();
            Log.temp.info("----------resultId:" + resultId + "--------resultCode:" + resultCode + "---------resultMsg:" + resultMsg + "---------resultData:" + returnStr);
            byte[] dataByteArr = returnStr.getBytes("UTF-8");
            outputStream.write(dataByteArr);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开启异步返回
     *
     * @param req
     * @return
     */
    public static AsyncContext asyncOpen(HttpServletRequest req) {
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0);
        return asyncContext;
    }


    /**
     * 返回内容至客户端
     *
     * @param asyncContext
     * @param resultCode
     * @param resultMsg
     */
    public static void asyncOut(AsyncContext asyncContext, int resultId, long resultCode, String resultMsg, Object resultData, Object extendData) {
        asyncContext.getResponse().setContentType("text/plain;charset=utf-8");
        try (OutputStream outputStream = asyncContext.getResponse().getOutputStream()) {
            JSONObject resultObj = new JSONObject();
            resultObj.put("resultCode", resultCode);
            resultObj.put("resultId", resultId);
            resultObj.put("resultMsg", resultMsg);

            if (resultData == null) {
                resultObj.put("resultData", "");
            } else {
                resultObj.put("resultData", resultData);
            }

            if (extendData == null) {
                resultObj.put("extendData", "");
            } else {
                resultObj.put("extendData", extendData);
            }


            String returnStr = resultObj.toJSONString();
            Log.temp.info("----------resultId:" + resultId + "--------resultCode:" + resultCode + "---------resultMsg:" + resultMsg + "---------resultData:" + returnStr);
            byte[] dataByteArr = returnStr.getBytes("UTF-8");
            outputStream.write(dataByteArr);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncContext.complete();
        }
    }


    /**
     * 解析JSON字符串，返回JSON对象<br/>
     * 字符串不允许为空
     *
     * @param str
     * @return
     */
    public static <T> T json(String str) {
        return json(str, "解析JSON时发现空字符串，不符合预期，请检查。");
    }

    /**
     * 解析JSON字符串，返回JSON对象<br/>
     * 字符串不允许为空<br/>
     *
     * @param str           待解析字符串
     * @param blankErrorMsg 当传入数据数据为空时输出的错误信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T json(String str, String blankErrorMsg, Object... blankErrorArgs) {
        if (StringUtils.isBlank(str)) {
            throw new SysException(blankErrorMsg, blankErrorArgs);
        }

        try {
            return (T) JSON.parse(str);
        } catch (Exception e) {
            throw new SysException(e, "发现无法解析的JSON字符串：str={}", str);
        }
    }

    /**
     * 将对象转化为JSON字符串
     *
     * @param obj
     * @return
     */
    public static String jsonString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

}
