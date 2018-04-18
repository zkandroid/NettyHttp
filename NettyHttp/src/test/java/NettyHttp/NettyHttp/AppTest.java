package NettyHttp.NettyHttp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.net.URI;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     * @throws IOException 
     * @throws ParseException 
     * @throws URISyntaxException 
     */
    public static void mian( String [] args ) throws ParseException, IOException, URISyntaxException
    {
    	//这是post方式的teest
		String url="http://127.0.0.1:8085/pay";
		Map<String, String> map = new HashMap<String, String>();
		map.put("machineId", "1");
		map.put("tradeid", "1");
		map.put("price", "2");
		//map.put("dfc", "1");
		//map.put("charset", "utf-8");
		String body = send(url, map,"utf-8");
		System.out.println("交易响应结果：");
		System.out.println(body);
		
		//这是get方式的test
		testGet();
    }

    /**
     * 对get请求方式的test
     * @return the suite of tests being tested
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static void testGet() throws URISyntaxException, ClientProtocolException, IOException
    {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
        
        URI uri = new URIBuilder("http://127.0.0.1:8085/pay/machineId=1/price=0.01/tradeid=1").build();
                // 创建http GET请求
        
        HttpGet httpGet = new HttpGet(uri);
        //response 对象
        CloseableHttpResponse response = null;
        try {
            // 执行http get请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("内容："+content);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }

	/**
	 * post方式的模拟客户端
	 * 
	 * @param url		资源地址
	 * @param map	参数列表
	 * @param encoding	编码
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String send(String url, Map<String,String> map,String encoding) throws ParseException, IOException{
		String body = "";

		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);
		
		//装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(map!=null){
			for (Entry<String, String> entry : map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		//设置参数到请求对象中
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		System.out.println("请求地址："+url);
		System.out.println("请求参数："+nvps.toString());
		
		//设置header信息
		//指定报文头【Content-type】、【User-Agent】
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		
		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();
        return body;
	}
	
}
