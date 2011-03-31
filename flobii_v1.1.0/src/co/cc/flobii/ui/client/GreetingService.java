package co.cc.flobii.ui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Boolean greetServer() throws IllegalArgumentException;
	String registerUser(String username,String password) throws IllegalArgumentException;
	String loginUser(String username, String password);
	String getList(String uid, int index);
	String changeLinklist(String uid, int index, String title, String url,
			String rating);
	String[] getFolders(String uid);
	String[] deleteFolder(String uid,int index);
	String[] createFolder(String uid,String folder);
}
