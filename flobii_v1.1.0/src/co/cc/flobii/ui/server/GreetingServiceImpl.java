package co.cc.flobii.ui.server;



import java.util.ArrayList;

import co.cc.flobii.ui.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	private String register(String username, String pwd){
        Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(AccountInfo.class);
    	ObjectifyService.register(LinkInfo.class);
        AccountInfo nac = new AccountInfo();
        LinkInfo ls = new LinkInfo();
        String ret = "null,null";
       if(ofy.find(AccountInfo.class,username) == null){
        	nac.username = username;
        	nac.password = pwd;
            ls.username = username;
            ls.linklist = new ArrayList<String>();
            ls.linklist.add(0, "");
            ls.linklist.add(1, "##http://youtube.com/##http://vimeo.com/##http://soundcloud.com/##http://spectrum.ieee.org/");
            ls.foldername = new ArrayList<String>();
            ls.foldername.add(0,"");
            ls.foldername.add(1,"Favourites");
            ls.title = new ArrayList<String>();
            ls.title.add(0,"");
            ls.title.add(1,"##Youtube##Vimeo##SoundCloud##Spectrum IEEE");
            ls.foldercount =ls.linklist.size()-1;
            ls.rating = new ArrayList<String>();
            ls.rating.add(0,"");
            ls.rating.add(1,"##5##5##5##5");
        	ofy.put(nac);
        	ofy.put(ls);
        	ret = "true,You were successfully registered !";
       }else{
    	   ret ="false,User already exist !";
       }
       return ret;
	}

	@Override
	public String registerUser(String username, String password)
			throws IllegalArgumentException {
		return register(username, password);
	}



	@Override
	public Boolean greetServer() throws IllegalArgumentException {	
		return true;
	}

	@Override
	public String loginUser(String username, String password) {
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(AccountInfo.class);
    	if(ofy.find(AccountInfo.class,username) != null){
        	if(ofy.find(AccountInfo.class,username).password.equals(password)){
        		return"true, User logged in!";
        	}else{
        		return "false, False password!";
        	}
       }else{
    	  return "false,User doesn't exist !";
       }
	}

	@Override
	public String getList(String uid,int index) {
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(LinkInfo.class);
    	String result ="fail";
    	if(ofy.find(LinkInfo.class,uid) != null){
    		if(!ofy.get(LinkInfo.class,uid).linklist.get(index).equals("")){
    			result = "xxx"+ofy.get(LinkInfo.class,uid).linklist.get(index)+"xxx"+ofy.get(LinkInfo.class,uid).rating.get(index)+"xxx"+ofy.get(LinkInfo.class,uid).title.get(index);
    			}else{
    			result= "xxx:xxx:xxx:";	
    			}
    	}
		return result;
	}
	
	@Override
	public String changeLinklist(String uid, int index, String title,
			String url, String rating) {
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(LinkInfo.class);
    	LinkInfo li =ofy.get(LinkInfo.class,uid);
    	li.linklist.remove(index);
		li.linklist.add(index, url);
	  	li.title.remove(index);
		li.title.add(index,title);
	  	li.rating.remove(index);
		li.rating.add(index,rating);
		ofy.put(li);
		if(!ofy.get(LinkInfo.class,uid).linklist.get(index).equals("")){
		return "xxx"+ofy.get(LinkInfo.class,uid).linklist.get(index)+"xxx"+ofy.get(LinkInfo.class,uid).rating.get(index)+"xxx"+ofy.get(LinkInfo.class,uid).title.get(index);
		}else{
		return "xxx:xxx:xxx:";	
		}
		}

	@Override
	public String[] getFolders(String uid) {
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(LinkInfo.class);
    	LinkInfo li =ofy.get(LinkInfo.class,uid);
    	String[] sfolder = new String[li.foldercount+1];
    	for(int i=0;i<sfolder.length;i++){
    		sfolder[i] = ofy.get(LinkInfo.class,uid).foldername.get(i);
    	}
		return sfolder;
	}

	@Override
	public String[] createFolder(String uid, String folder) {
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(LinkInfo.class);
    	LinkInfo li =ofy.get(LinkInfo.class,uid);
    	li.foldercount = li.foldercount+1;
    	li.foldername.add(li.foldercount,folder);
        li.linklist.add(li.foldercount,"");
        li.title.add(li.foldercount,"");
        li.rating.add(li.foldercount,"");
        li.foldercount = li.foldername.size()-1;
    	ofy.put(li);
    	String[] sfolder = new String[li.foldercount+1];
    	for(int i=0;i<sfolder.length;i++){
    		sfolder[i] = ofy.get(LinkInfo.class,uid).foldername.get(i);
    	}
		return sfolder;
	}
	
	public String[] deleteFolder(String uid,int index){
		Objectify ofy = ObjectifyService.begin();
    	ObjectifyService.register(LinkInfo.class);
    	LinkInfo li =ofy.get(LinkInfo.class,uid);
    	li.foldername.remove(index);
    	li.title.remove(index);
    	li.rating.remove(index);
    	li.linklist.remove(index);
    	li.foldercount= li.linklist.size()-1;
    	ofy.put(li);
    	String[] sfolder = new String[li.foldercount+1];
    	for(int i=0;i<sfolder.length;i++){
    		sfolder[i] = ofy.get(LinkInfo.class,uid).foldername.get(i);
    	}
		return sfolder;
	}
	

}
