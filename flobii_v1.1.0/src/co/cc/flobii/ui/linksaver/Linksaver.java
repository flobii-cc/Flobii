package co.cc.flobii.ui.linksaver;


import org.apache.jsp.ah.datastoreViewerBody_jsp;
import org.cobogw.gwt.user.client.ui.Rating;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import co.cc.flobii.ui.client.GreetingService;
import co.cc.flobii.ui.client.GreetingServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class Linksaver {

	static boolean login_out =false;
	static int close_open =0;
	final static TextBox usereg = new TextBox();
	final static PasswordTextBox pwdreg = new PasswordTextBox();
	final static PasswordTextBox repeatpwdreg = new PasswordTextBox();
	final static TextBox username = new TextBox();
	final static PasswordTextBox pwd = new PasswordTextBox();
	final static PopupPanel pl = new PopupPanel();
	static String uid ="";
	static Anchor[] afolder ;
	static WindowPanel wplink = new WindowPanel("Save your links on the Web");
	static VerticalPanel vplinksaver = new VerticalPanel();
	static HorizontalPanel hztoolbar = new HorizontalPanel();
	static HorizontalPanel hzui = new HorizontalPanel();
	static ScrollPanel scfolder = new ScrollPanel();
	static VerticalPanel vpfolder = new VerticalPanel();
	static ScrollPanel sclink = new ScrollPanel();
	static VerticalPanel vplinklist = new VerticalPanel();
	static int foldernumber = 0;
	static int c =0;
	static String[] splitter;
	static int foldernch=0;
	static int v =0;
	static RadioButton[] radiolink;
	static int rlchooseix=0;
	
	public static void logoutfromSession(){
		
		login_out = false;
		uid ="";
		afolder = null;
		vplinksaver.clear();
		hztoolbar.clear();
		hzui.clear();
	    scfolder.clear();
		vpfolder.clear();
		sclink.clear();
		vplinklist.clear();
		foldernumber = 0;
		c =0;
		splitter= null;
		foldernch=0;
		v =0;
		radiolink = null;
		rlchooseix=0;
		wplink.setVisible(false);
		deactivateIcon();
	}
	
	public static void loadLinkSaver(){
		if(!login_out){
			showLogin();
			close_open=1;
			wplink.setVisible(false);
		}else{
			if(!(wplink.isShowing()||wplink.isCollapsed())){	
				loadLinkPanel();
				close_open=1;
			}else{
				if(wplink.isVisible()){
					wplink.setVisible(false);
					close_open=0;
				}else{
					wplink.setVisible(true);
					close_open=1;
					wplink.toFront();
				}
		}
	}
	}
	
	
	public static void loadPanel(){
		wplink.setSize("600px", "360px");
		vplinksaver.setSize("600px", "360px");
		vpfolder.setWidth("150px");
		scfolder.setHeight("286px");
		scfolder.setWidth("164px");
		//scfolder.setStyleName("scfolder");
		sclink.setHeight("280px");
		sclink.setStyleName("sclink");
		vpfolder.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vpfolder.setSpacing(5);
		sclink.setWidth("436px");
		sclink.add(vplinklist);
		vplinksaver.add(hztoolbar);
		hztoolbar.setStyleName("hztoolbar");
		hztoolbar.setWidth("590px");
		vplinksaver.add(hzui);
		hzui.add(scfolder);
		scfolder.add(vpfolder);
		hzui.add(sclink);
		wplink.getHeader().clear();
		wplink.setWidget(vplinksaver);
		wplink.setResizable(false);
		wplink.addStyleName("dbox");
	}
	
	public static void loadToolbar(){
		
		Anchor logolink = new Anchor("Logout");
		logolink.setStyleName("linklogo");
		logolink.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				logoutfromSession();
			}
		});
		
		Anchor cfolder = new Anchor("Create Folder");
		cfolder.setStyleName("linktoolbar");
		cfolder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				createFolder();
			}
		});
		Anchor delfolder = new Anchor("Delete Folder");
		delfolder.setStyleName("linktoolbar");
		delfolder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			    deleteFolder();
			}
		});
		Anchor change = new Anchor("Change");
		change.setStyleName("linktoolbar");
		change.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ChangeLinkPopup();
			}
		});
		Anchor deletelink = new Anchor("Delete");
		deletelink.setStyleName("linktoolbar");
		deletelink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteLink();	
			}
		});
		Anchor addlink = new Anchor("Add");
		addlink.setStyleName("linktoolbar");
		addlink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addNewLink();
			}
		});
		hztoolbar.setSpacing(5);
		for(int s =0;s<1;s++){
			hztoolbar.add(new HTML("&nbsp;"));
		}
		hztoolbar.add(cfolder);
		hztoolbar.add(delfolder);
		for(int s =0;s<4;s++){
			hztoolbar.add(new HTML("&nbsp;"));
		}
		hztoolbar.add(logolink);
		for(int s =0;s<4;s++){
			hztoolbar.add(new HTML("&nbsp;"));
		}
		hztoolbar.add(addlink);
		hztoolbar.add(change);
		hztoolbar.add(deletelink);
		for(int s =0;s<1;s++){
			hztoolbar.add(new HTML("&nbsp;"));
		}
	}
	

	
	public static void loadLinkPanel(){
		loadPanel();
		loadToolbar();
		loadFolders();
		wplink.center();
		wplink.setVisible(true);
		wplink.setHideContentOnMove(false);
	}
	
	
   public static void setButtonStyle(){
	   for(int m =1;m<afolder.length;m++){
		   afolder[m].setStyleName("linkfolder");
	   }
   }
	
	public static void loadFolders(){
		datastore.getFolders(uid, new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
				showInfo(caught.getMessage(),pl,"#c93");
				
			}
			@Override
			public void onSuccess(String[] result) {
				vpfolder.clear();
				afolder = new Anchor[result.length];
				for(int s =0; s<result.length;s++){
					String fname =result[s];
					final int ix = s;
					if(!fname.equals("")){
					final Anchor anchor = new Anchor(fname);
					afolder[s] = anchor;
					anchor.setStyleName("linkfolder");
					anchor.setWidth("80px");
					anchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
								foldernch = ix;
								setButtonStyle();
								anchor.setStyleName("linkfolderactive");
								datastore.getList(uid, ix, new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										showInfo(caught.getMessage(),pl,"#c93");
									}

									@Override
									public void onSuccess(String result) {
										if(!result.equals("xxx:xxx:xxx:")){
											splitter = result.split("xxx");
											final String[] links = splitter[1].split("##");
											final String[] rating = splitter[2].split("##");
											final String[] title = splitter[3].split("##");
											loadPanelLinks( links, rating, title);
											}else{
												vplinklist.clear();
												splitter[1] ="";
												splitter[2]="";
												splitter[3]="";
											}
									}
								});
						}
					});
					vpfolder.add(anchor);
				}
				}
				if(result.length>0){
					foldernch = 1;
					setButtonStyle();
					afolder[1].setStyleName("linkfolderactive");
					datastore.getList(uid, 1, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							showInfo(caught.getMessage(),pl,"#c93");
						}

						@Override
						public void onSuccess(String result) {
							if(!result.equals("xxx:xxx:xxx:")){
								splitter = result.split("xxx");
								final String[] links = splitter[1].split("##");
								final String[] rating = splitter[2].split("##");
								final String[] title = splitter[3].split("##");
								loadPanelLinks( links, rating, title);
								}else{
									vplinklist.clear();
									splitter[1] ="";
									splitter[2]="";
									splitter[3]="";
								}
						}
					});
				}
			}
		});
	}

	
	public static void loadPanelLinks(final String[] l , String[] r, final String[] t){
		v=0;
		vplinklist.clear();
		vplinklist.setSpacing(10);
		radiolink = new RadioButton[l.length];
		while(v<l.length){
			if(!l[v].equals("")){
		final int ixl =v;
		HorizontalPanel hz = new HorizontalPanel();
		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		final Anchor anchor = new Anchor(t[v]);
		anchor.setWidth("250px");
		radiolink[v]= new RadioButton("Link","");
		anchor.setStyleName("linklink");
		anchor.addClickHandler(new ClickHandler() {		
			@Override
			public void onClick(ClickEvent event) {
				Window.open(l[ixl], t[ixl], "");	
			}
		});
		hz.add(radiolink[v]);
		hz.add(new HTML("&nbsp;&nbsp;"));
		hz.add(anchor);
		Rating rat = new Rating();
		rat.setReadOnly(true);
		rat.setStyleName("rstarstyle");
		rat.setValue(Integer.parseInt(r[v]));
		hz.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		hz.add(rat);
		vplinklist.add(hz);
			}
		v++;
		}
	}
	
	
	
	
	public static void addNewLink(){
		final PopupPanel padd = new PopupPanel();
		padd.setAnimationEnabled(true);
		padd.setAutoHideEnabled(true);
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("200px");
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\" color=\"#93c\">Add Link</font>"));
		final TextBox linkt = new TextBox();
		linkt.setAlignment(TextAlignment.CENTER);
		linkt.setText("title");
		linkt.setStyleName("textbox");
		final TextBox linkurl = new TextBox();
		linkurl.setAlignment(TextAlignment.CENTER);
		linkurl.setText("url");
		linkurl.setStyleName("textbox");
		Anchor addbutton = new Anchor("Add");
		addbutton.setStyleName("anchor");
		vp.add(linkt);
		vp.add(linkurl);
		//-----------------------------------------
		linkt.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(linkt.getText().equals("")){
					linkt.setText("title");
				}
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(linkt.getText().equals("title")){
					linkt.setText("");
				}
			}
		});
		
       linkurl.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(linkurl.getText().equals("")){
					linkurl.setText("url");
				}
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(linkurl.getText().equals("url")){
					linkurl.setText("");
				}
			}
		});
		
		//-----------------------------------------
		HorizontalPanel hz = new HorizontalPanel();
		final Rating rlink = new Rating();
		rlink.setValue(5);
		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		hz.add(rlink);
		rlink.setStyleName("rstarstyle");
		hz.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
		hz.add(addbutton);
		vp.add(hz);
		padd.setWidget(vp);
		linkurl.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					String eurl =linkurl.getText(); 
					if(!eurl.startsWith("http://")&&!eurl.startsWith("https://")&&!eurl.startsWith("ftp://")){
						eurl = "http://"+eurl;
					}
					expandLinks(linkt.getText(), eurl, rlink.getValue()+"",padd);
				}
			}
	       });
		addbutton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String eurl =linkurl.getText(); 
				if(!eurl.startsWith("http://")&&!eurl.startsWith("https://")&&!eurl.startsWith("ftp://")){
					eurl = "http://"+eurl;
				}
				expandLinks(linkt.getText(), eurl, rlink.getValue()+"",padd);
			}
		});
		padd.center();
	}
	
	
	
	
	public static void expandLinks(String title, String url, String rating, final PopupPanel p){
		if(title.equals("")||url.equals("")||title.equals("title")||url.equals("url")){
			showInfo("Invalid !", p, "c93");
		}else{
			splitter[1]= splitter[1]+"##"+url;
			splitter[2]= splitter[2]+"##"+rating;
			splitter[3]= splitter[3]+"##"+title;
			datastore.changeLinklist(uid,foldernch, splitter[3], splitter[1], splitter[2], new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
				 showInfo(caught.getMessage(),pl,"#c93");
					
				}

				@Override
				public void onSuccess(String result) {
					splitter = result.split("xxx");
					final String[] links = splitter[1].split("##");
					final String[] rating = splitter[2].split("##");
					final String[] title = splitter[3].split("##");
					loadPanelLinks( links, rating, title);	
					p.hide();
				}
			});
		}
	}
	
	
	public static void createFolder(){
		final PopupPanel pcf = new PopupPanel();
		pcf.setAnimationEnabled(true);
		pcf.setAutoHideEnabled(true);
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\" color=\"#93c\">Create Folder</font>"));
		final TextBox folderc = new TextBox();
		folderc.setAlignment(TextAlignment.CENTER);
		folderc.setText("folder name");
		folderc.setStyleName("textbox");
		folderc.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
				createFolderAlg(folderc.getText(),pcf);
				}
			}
		});
		folderc.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(folderc.getText().equals("")){
					folderc.setText("folder name");
				}
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(folderc.getText().equals("folder name")){
					folderc.setText("");
				}
			}
		});
		Anchor anchor = new Anchor("Create");
		anchor.setStyleName("anchor");
		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createFolderAlg(folderc.getText(),pcf);
			}
		});	
		vp.add(folderc);
		vp.add(anchor);
		pcf.setWidget(vp);
		pcf.center();
	}
	
	
	public static void createFolderAlg(String foldertext,PopupPanel p){
		if(!(foldertext.equals("")||foldertext.equals("folder name"))){ 
			p.hide();
			datastore.createFolder(uid,foldertext, new AsyncCallback<String[]>() {
				@Override
				public void onFailure(Throwable caught) {
					showInfo(caught.getMessage(),pl,"#c93");
				}

				@Override
				public void onSuccess(String[] result) {
					vpfolder.clear();
					afolder = new Anchor[result.length];
					for(int s =0; s<result.length;s++){
						String fname =result[s];
						final int ix = s;
						if(!fname.equals("")){
						final Anchor anchor = new Anchor(fname);
						afolder[s] = anchor;
						anchor.setStyleName("linkfolder");
						anchor.setWidth("80px");
						anchor.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
									foldernch = ix;
									setButtonStyle();
									anchor.setStyleName("linkfolderactive");
									datastore.getList(uid, ix, new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											showInfo(caught.getMessage(),pl,"#c93");
										}

										@Override
										public void onSuccess(String result) {
											if(!result.equals("xxx:xxx:xxx:")){
												splitter = result.split("xxx");
												final String[] links = splitter[1].split("##");
												final String[] rating = splitter[2].split("##");
												final String[] title = splitter[3].split("##");
												loadPanelLinks( links, rating, title);
												}else{
													vplinklist.clear();
													splitter[1] ="";
													splitter[2]="";
													splitter[3]="";
												}
										}
									});
							}
						});
						vpfolder.add(anchor);
					}
					}
				}
			});
			}else{
				showInfo("Invalid !", p, "#c93");
			}
	}
	
	
	
	
	public static void deleteFolder(){
		datastore.deleteFolder(uid, foldernch, new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				showInfo(caught.getMessage(),pl,"#c93");
			}

			@Override
			public void onSuccess(String[] result) {
				vpfolder.clear();
				vplinklist.clear();
				afolder = new Anchor[result.length];
				for(int s =0; s<result.length;s++){
					String fname =result[s];
					final int ix = s;
					if(!fname.equals("")){
					final Anchor anchor = new Anchor(fname);
					afolder[s] = anchor;
					anchor.setStyleName("linkfolder");
					anchor.setWidth("80px");
					anchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
								foldernch = ix;
								setButtonStyle();
								anchor.setStyleName("linkfolderactive");
								datastore.getList(uid, ix, new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										showInfo(caught.getMessage(),pl,"#c93");
									}

									@Override
									public void onSuccess(String result) {
										splitter = result.split("xxx");
										if(!splitter[3].equals("")){
										final String[] links = splitter[1].split("##");
										final String[] rating = splitter[2].split("##");
										final String[] title = splitter[3].split("##");
										loadPanelLinks( links, rating, title);
										}else{
											vplinklist.clear();
											splitter[1] ="";
											splitter[2]="";
											splitter[3]="";
										}
									}
								});
						}
					});
					vpfolder.add(anchor);
				}
				}
			}
		});
	}
	
	
	
	public static void deleteLink(){
		if(radiolink.length>0){
			for(int g=1;g<radiolink.length;g++){
				if(radiolink[g].getValue()){
					final String[] links = splitter[1].split("##");
					final String[] rating = splitter[2].split("##");
					final String[] title = splitter[3].split("##");
					splitter[1]="";
					splitter[2]="";
					splitter[3]="";
					for(int q=1;q<g;q++){
						splitter[1] = splitter[1]+"##"+links[q];
						splitter[2] = splitter[2]+"##"+rating[q];
						splitter[3] = splitter[3]+"##"+title[q];
					}
					for(int q=g+1;q<links.length;q++){
						splitter[1] = splitter[1]+"##"+links[q];
						splitter[2] = splitter[2]+"##"+rating[q];
						splitter[3] = splitter[3]+"##"+title[q];
					}
					datastore.changeLinklist(uid,foldernch, splitter[3], splitter[1], splitter[2], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							showInfo(caught.getMessage(),pl,"#c93");
						}

						@Override
						public void onSuccess(String result) {
							if(!result.equals("xxx:xxx:xxx:")){
								splitter = result.split("xxx");
								final String[] links = splitter[1].split("##");
								final String[] rating = splitter[2].split("##");
								final String[] title = splitter[3].split("##");
								loadPanelLinks( links, rating, title);
								}else{
									vplinklist.clear();
									splitter[1] ="";
									splitter[2]="";
									splitter[3]="";
								}
						}
					});	
				}
			}
		}
	}
	
	
	
	public static void changeLink(String stitle, String surl, String srating,int g){
				if(radiolink[g].getValue()){
					final String[] links = splitter[1].split("##");
					final String[] rating = splitter[2].split("##");
					final String[] title = splitter[3].split("##");
					links[g] = surl;
					rating[g] = srating;
					title[g] = stitle;
					splitter[1] ="";
					splitter[2] ="";
					splitter[3] ="";
					for(int q=1;q<links.length;q++){
						splitter[1] = splitter[1]+"##"+links[q];
						splitter[2] = splitter[2]+"##"+rating[q];
						splitter[3] = splitter[3]+"##"+title[q];
					}
					datastore.changeLinklist(uid,foldernch, splitter[3], splitter[1], splitter[2], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							showInfo(caught.getMessage(),pl,"#c93");
						}

						@Override
						public void onSuccess(String result) {
							if(!result.equals("xxx:xxx:xxx:")){
								splitter = result.split("xxx");
								final String[] links = splitter[1].split("##");
								final String[] rating = splitter[2].split("##");
								final String[] title = splitter[3].split("##");
								loadPanelLinks( links, rating, title);
								}else{
									vplinklist.clear();
									splitter[1] ="";
									splitter[2]="";
									splitter[3]="";
								}
						}
					});	
				}
	}
	

	
	public static void ChangeLinkPopup(){
		final PopupPanel padd = new PopupPanel();
		padd.setAnimationEnabled(true);
		padd.setAutoHideEnabled(true);
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("200px");
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\" color=\"#93c\">Change Link</font>"));
		final TextBox linkt = new TextBox();
		linkt.setAlignment(TextAlignment.CENTER);
		linkt.setStyleName("textbox");
		final TextBox linkurl = new TextBox();
		linkurl.setAlignment(TextAlignment.CENTER);
		linkurl.setStyleName("textbox");
		Anchor chbutton = new Anchor("Change");
		chbutton.setStyleName("anchor");
		vp.add(linkt);
		vp.add(linkurl);
		final Rating rlink = new Rating();
		//--------------------------------------------------------------------------
		if(radiolink.length>0){
			rlchooseix=0;
			for(int g=1;g<radiolink.length;g++){
				if(radiolink[g].getValue()){
					rlchooseix =g;
					final String[] links = splitter[1].split("##");
					final String[] rating = splitter[2].split("##");
					final String[] title = splitter[3].split("##");
					linkurl.setText(links[g]);
					linkt.setText(title[g]);
					rlink.setValue(Integer.parseInt(rating[g]));
				}
			}
		}
		//--------------------------------------------------------------------------
		HorizontalPanel hz = new HorizontalPanel();
		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		hz.add(rlink);
		rlink.setStyleName("rstarstyle");
		hz.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
		hz.add(chbutton);
		vp.add(hz);
		padd.setWidget(vp);
		linkurl.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
						String eurl =linkurl.getText(); 
						if(!eurl.startsWith("http://")&&!eurl.startsWith("https://")&&!eurl.startsWith("ftp://")){
							eurl = "http://"+eurl;
						}
				changeLink(linkt.getText(), eurl, ""+rlink.getValue(),rlchooseix);
				padd.hide();
				}
			}
	       });
		linkt.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					String eurl =linkurl.getText(); 
					if(!eurl.startsWith("http://")&&!eurl.startsWith("https://")&&!eurl.startsWith("ftp://")){
						eurl = "http://"+eurl;
					}
					changeLink(linkt.getText(),eurl, ""+rlink.getValue(),rlchooseix);
					padd.hide();
					}
			}
	       });
		chbutton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String eurl =linkurl.getText(); 
				if(!eurl.startsWith("http://")&&!eurl.startsWith("https://")&&!eurl.startsWith("ftp://")){
					eurl = "http://"+eurl;
				}
				changeLink(linkt.getText(), eurl, ""+rlink.getValue(),rlchooseix);
				padd.hide();
			}
		});
		if(rlchooseix!=0){
		padd.center();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void showLogin(){
			pl.clear();
			pl.setAnimationEnabled(true);
			pl.setAutoHideEnabled(true);
			VerticalPanel vploginreg = new VerticalPanel();
			HorizontalPanel hzloginreg = new HorizontalPanel();
			VerticalPanel vplogin = new VerticalPanel();
			VerticalPanel vpregister = new VerticalPanel();
			hzloginreg.add(vplogin);
			hzloginreg.add(vpregister);
			//Button Login/Register
			HorizontalPanel hzloginbutton = new HorizontalPanel();
			Anchor login = new Anchor("Login");
			login.setStyleName("loginstyle");
			login.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
						startLogin(username.getText(), pwd.getText());
				}
			});
			pwd.addKeyUpHandler(new KeyUpHandler() {	
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if(event.getNativeKeyCode()== KeyCodes.KEY_ENTER){
						startLogin(username.getText(), pwd.getText());
					}
				}
			});
			vploginreg.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			hzloginbutton.add(login);
			hzloginbutton.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
			Anchor register = new Anchor("Register");
			register.setStyleName("regstyle");
			register.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					 startRegistration(usereg.getText(),pwdreg.getText(),repeatpwdreg.getText());				
				}
			});
			repeatpwdreg.addKeyUpHandler(new KeyUpHandler() {
				
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if(event.getNativeKeyCode()== KeyCodes.KEY_ENTER){
						startRegistration(usereg.getText(),pwdreg.getText(),repeatpwdreg.getText());
					}
				}
			});
			hzloginbutton.add(register);
			//Login Panel
			loadLoginPanel(vplogin,hzloginbutton);
			//Register Panel
			loadRegisterPanel(vpregister);
			vploginreg.add(hzloginreg);
			pl.setWidget(vploginreg);
			pl.addCloseHandler(new CloseHandler<PopupPanel>() {
				
				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					if(!login_out){
						close_open=0;
						deactivateIcon();
					}
				}
			});
			pl.center();
	}
	
	native static void deactivateIcon()/*-{
		$wnd.$('.linksaver_button').children('img').attr('src' , "token/linksaver.png");
	}-*/;
		
	
	
	private static final GreetingServiceAsync datastore = GWT
	.create(GreetingService.class);
	
	
	private static void startLogin(final String username, final String pwd){
		datastore.loginUser(username, pwd, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				showInfo(caught.getMessage(), pl, "#c93");
			}
			@Override
			public void onSuccess(String result) {
				String[] res = result.split(",");
				if(res[0].equals("true")){
					login_out =true;
					pl.hide();
					uid = username;
					loadLinkPanel();
				}else{
					showInfo(res[1], pl, "#c93");
				}
			}
		});
	}
	
	private static void startRegistration(final String username, final String pwd,final String repeatpwd){
		if(pwd.equals(repeatpwd)&&pwd.length()>0){
		datastore.registerUser(username, pwd, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				showInfo(caught.getMessage(), pl, "#c93");
			}

			@Override
			public void onSuccess(String result) {
				String[] res = result.split(",");
				if(res[0].equals("true")){
					usereg.setText("username");
					pwdreg.setText("password");
					repeatpwdreg.setText("password");
					Linksaver.username.setText(username);
					Linksaver.pwd.setText(pwd);
					showInfo(res[1], pl, "#3c9");
				}else{
					showInfo(res[1], pl, "#c93");
				}
			}
		});
		}else{
			showInfo("Passwords do not match!", pl, "#c93");
		}
	}
	
	
	private static void loadLoginPanel(VerticalPanel vp, HorizontalPanel hz){
		vp.setWidth("250px");
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\" color=\"#39c\">Login</font>"));
		username.setStyleName("textboxlog");
		username.setAlignment(TextAlignment.CENTER);
		username.setText("username");
		username.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(username.getText().equals("")){
					username.setText("username");
				}	
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(username.getText().equals("username")){
				username.setText("");
				}
			}
		});
		vp.add(username);

		pwd.setText("password");
		pwd.setStyleName("textboxlog");
		pwd.setAlignment(TextAlignment.CENTER);
		pwd.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(pwd.getText().equals("")){
					pwd.setText("password");
				}	
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(pwd.getText().equals("password")){
				pwd.setText("");
				}
			}
		});
		vp.add(pwd);
		vp.add(hz);
	}
	
	private static void loadRegisterPanel(VerticalPanel vp){
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\" color=\"#3c9\">Register</font>"));
		usereg.setStyleName("textboxreg");
		usereg.setAlignment(TextAlignment.CENTER);
		usereg.setText("username");
		usereg.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(usereg.getText().equals("")){
					usereg.setText("username");
				}	
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(usereg.getText().equals("username")){
					usereg.setText("");
				}
			}
		});
		vp.add(usereg);

		pwdreg.setText("password");
		pwdreg.setStyleName("textboxreg");
		pwdreg.setAlignment(TextAlignment.CENTER);
		pwdreg.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(pwdreg.getText().equals("")){
					pwdreg.setText("password");
				}	
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(pwdreg.getText().equals("password")){
					pwdreg.setText("");
				}
			}
		});
		vp.add(pwdreg);
		
		repeatpwdreg.setText("password");
		repeatpwdreg.setStyleName("textboxreg");
		repeatpwdreg.setAlignment(TextAlignment.CENTER);
		repeatpwdreg.addFocusListener(new FocusListener() {
			
			@Override
			public void onLostFocus(Widget sender) {
				if(repeatpwdreg.getText().equals("")){
					repeatpwdreg.setText("password");
				}	
			}
			
			@Override
			public void onFocus(Widget sender) {
				if(repeatpwdreg.getText().equals("password")){
					repeatpwdreg.setText("");
				}
			}
		});
		vp.add(repeatpwdreg);
	}

	
	public static void showInfo(String text, PopupPanel p,String color){
		PopupPanel infop = new PopupPanel();
		infop.setAnimationEnabled(true);
		infop.setAutoHideEnabled(true);
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML("<font face=\"Ubuntu\" size=\"4\" color=\""+color+"\">"+text+"</font>"));
		infop.setWidget(vp);
		infop.setPopupPosition(p.getAbsoluteLeft()+100, p.getAbsoluteTop()-20);
		infop.show();
	}
	
	

}
