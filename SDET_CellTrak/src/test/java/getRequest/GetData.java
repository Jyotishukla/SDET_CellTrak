package getRequest;

import io.restassured.RestAssured;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.*;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matcher;
import org.json.JSONObject;

import static org.hamcrest.Matchers.*;
import org.testng.*;
import static com.jayway.restassured.RestAssured.*;



public class GetData {

	 @Test
	    public void given_UserOrTopicExists_when_UserInfoIsRetrieved_then_200StatusCodeIsReceivedWithCount() throws ClientProtocolException, IOException {
	        // Given
	        final String name = randomAlphabetic(10);
	        List<String> nameList = Arrays.asList("Jyoti&sort=created&order=asc", 
													randomAlphabetic(10),
													"topic:java+topic:java");
	        OkHttpClient client = new OkHttpClient();
	        List<Response> httpResponses = new ArrayList();
	        for(String nme: nameList) {
	        	 Request request = new Request.Builder()
	       	          .url("http://api.github.com/search/repositories?q=" + nme)
	       	          .get()
	       	          .build();
	        	httpResponses.add(client.newCall(request).execute());
	        	
	        }
	        JSONObject obj = new JSONObject(httpResponses.get(0).body().string());
	        System.out.println(obj.getInt("total_count" ));
	        httpResponses.forEach(nme->System.out.println(nme));
	        // Then
	        assertEquals(httpResponses.get(0).isSuccessful(), true);
	        assertEquals(httpResponses.get(1).isSuccessful(),true );
	        assertEquals(httpResponses.get(2).isSuccessful(), true);;
	    }
	 
	 
	 // Testing repository with topic vue.js and have folks more than 205
	 
	 @Test
	    public void given_RepositoriesofTopicExists_when_RepositoryInfoIsRetrieved_then_higestFolks() throws InterruptedException,ClientProtocolException,IOException {
	        // Given
	        String topic = "vue&forks%3A>%3D205";
	        OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder()
	       	          .url("http://api.github.com/search/repositories?q=" + topic)
	       	          .get()
	       	          .build();
	        Response httpResponses = client.newCall(request).execute();
	        
	        JSONObject obj = new JSONObject(httpResponses.body().string());
	        System.out.println(obj);
	        System.out.println(obj.getJSONArray("items").getJSONObject(0).getString("full_name"));
	       
	        // Then
	        assertEquals(httpResponses.isSuccessful(), true);
	        assertEquals(obj.getJSONArray("items").getJSONObject(0).getString("full_name"), "vuejs/vue");
	        
	    }
	 
	 // exceeding rate limit more than 10 for unauthenticated users, will hold for 1 mn to run this case

	 @Test
	    public void given_listmorethan10_when_exceededTheRateLimit_then_forbiddenResponseisThrown() throws InterruptedException,ClientProtocolException, IOException {
	        // Given
		 Thread.sleep(60000);
	        final String name = randomAlphabetic(10);
	        List<String> nameList = Arrays.asList("Jyoti&sort=created&order=asc", 
	        									  "vue&forks%3A>%3D205&type=Repositories",
	        									  "topic:java+topic:java", "Jyoti&sort=created&order=asc",
	        									  "Jyoti&sort=created&order=asc", 
	        									  "Jyoti&sort=created&order=asc", 
	        									  "Jyoti&sort=created&order=asc", 
	        									  "Jyoti&sort=created&order=asc",
	        									  "Jyoti&sort=created&order=asc",
	        									  "Jyoti&sort=created&order=asc",
	        									  "Jyoti&sort=created&order=asc",
	        									  "Jyoti&sort=created&order=asc");
	        OkHttpClient client = new OkHttpClient();
	        List<Response> httpResponses = new ArrayList();
	        for(String nme: nameList) {
	        	 Request request = new Request.Builder()
	       	          .url("http://api.github.com/search/repositories?q=" + nme)
	       	          .get()
	       	          .build();
	        	httpResponses.add(client.newCall(request).execute());
	        	
	        }
	        JSONObject obj = new JSONObject(httpResponses.get(10).body().string());
	        System.out.println(obj);
	        httpResponses.forEach(nme->System.out.println(nme));
	        // Then
	        assertEquals(httpResponses.get(10).isSuccessful(), false);
	    }
	 


	 // Pagination 
	 @Test
	    public void given_pageParameter_when_jumpOnPageParameter_then_showFirstNameOnList() throws InterruptedException,ClientProtocolException, IOException {
	        // Given
		 OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder()
	       	          .url("https://api.github.com/search/code?q=addClass+user:mozilla&page=2")
	       	          .get()
	       	          .build();
	        Response httpResponses = client.newCall(request).execute();
	        
	        JSONObject obj = new JSONObject(httpResponses.body().string());
	        System.out.println(obj);
	        System.out.println(obj.getJSONArray("items"));
	       
	        // Then
	        assertEquals(httpResponses.isSuccessful(), true);
	    }
	 
	 // Search Repository 
	 @Test
	    public void given_className_when_langAndFile_then_showRepo() throws ClientProtocolException, IOException {
	        // Given
		 OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder()
	       	          .url("https://api.github.com/search/code?q=addClass+in:file+language:js+repo:jquery/jquery")
	       	          .get()
	       	          .build();
	        Response httpResponses = client.newCall(request).execute();
	        
	        JSONObject obj = new JSONObject(httpResponses.body().string());
	        System.out.println(obj);
	        System.out.println(obj.getJSONArray("items"));
	       
	        // Then
	        assertEquals(httpResponses.isSuccessful(), true);
	    }
	 


}
