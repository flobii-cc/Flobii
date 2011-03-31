package co.cc.flobii.ui.mpwidget;



import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JSONRetrieve {
	

	static void getJSON_String(String url){	
		 url = URL.encode(url);
		  RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);			   
		   try {
		    Request request = builder.sendRequest("", new RequestCallback() {	
		      public void onError(Request request, Throwable exception) {
		    	  Window.alert("Couldn't retrieve JSON");  
		      }
		      public void onResponseReceived(Request request, Response response) {
		        if (200 == response.getStatusCode()) {
		        	JSONValue jsonValue = JSONParser.parse(response.getText());
		        	JSONArray jsonArray = jsonValue.isArray();
		        	MpWidg.loadList(jsonArray,JSONRetrieve.randomAlgo(100));

		        } else {
		        	Window.alert("Couldn't retrieve JSON (" + response.getStatusText() + ")");
		        }
		      }       
		    });
		  } catch (RequestException e) {
			  Window.alert("Couldn't retrieve JSON ("+e.getMessage()+")");   
		  }
}
	
	static int[] randomAlgo(int length){
		int[] array  = new int[length];
		for(int i=0;i<length;i++){
			array[i]=i;
		}
		for(int n=(length-1);n>=0;n--){
			int rand = (int)(Math.random()*n);
			int save = array[n];
			array[n] = array[rand];
			array[rand]=save;
		}
		return array;
	}
	
}
