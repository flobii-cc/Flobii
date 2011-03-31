package co.cc.flobii.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void registerUser(String username, String password,
			AsyncCallback<String> callback);

	void loginUser(String username, String password,
			AsyncCallback<String> callback);
	
	void greetServer(AsyncCallback<Boolean> callback);

	void getList(String uid, int index, AsyncCallback<String> callback);
	
	void changeLinklist(String uid,int index,String title,String url,String rating,AsyncCallback<String> callback);

	void getFolders(String uid, AsyncCallback<String[]> callback);

	void createFolder(String uid, String folder, AsyncCallback<String[]> callback);

	void deleteFolder(String uid, int index, AsyncCallback<String[]> callback);
}
