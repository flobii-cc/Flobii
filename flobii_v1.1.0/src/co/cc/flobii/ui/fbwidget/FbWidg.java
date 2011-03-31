package co.cc.flobii.ui.fbwidget;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.DecoratedLayoutPopupPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwittit.client.facebook.ConnectState;
import com.gwittit.client.facebook.FacebookApi;
import com.gwittit.client.facebook.FacebookApi.Permission;
import com.gwittit.client.facebook.FacebookConnect;
import com.gwittit.client.facebook.FacebookException;
import com.gwittit.client.facebook.Util;
import com.gwittit.client.facebook.entities.ActionLink;
import com.gwittit.client.facebook.entities.Attachment;
import com.gwittit.client.facebook.entities.Comment;
import com.gwittit.client.facebook.entities.Comments;
import com.gwittit.client.facebook.entities.Likes;
import com.gwittit.client.facebook.entities.Media;
import com.gwittit.client.facebook.entities.Media.Type;
import com.gwittit.client.facebook.entities.NotificationRequest;
import com.gwittit.client.facebook.entities.NotificationRequest.NotificationType;
import com.gwittit.client.facebook.entities.Post;
import com.gwittit.client.facebook.entities.Stream;
import com.gwittit.client.facebook.entities.UserInfo;
import com.gwittit.client.facebook.ui.ErrorResponseUI;
import com.gwittit.client.facebook.xfbml.FbName;
import com.gwittit.client.facebook.xfbml.FbPromptPermission;
import com.gwittit.client.facebook.xfbml.Xfbml;

@SuppressWarnings("deprecation")
public class FbWidg{
	//Vertical Panels
    final VerticalPanel fbmainfield = new VerticalPanel();
    final VerticalPanel fbnotification = new VerticalPanel();
    final VerticalPanel notificationfield = new VerticalPanel();
    final VerticalPanel groupInvitesWrapper = new VerticalPanel ();
    final VerticalPanel eventInvitesWrapper = new VerticalPanel ();  
    final VerticalPanel fbstream = new VerticalPanel();
    final VerticalPanel fbstreamzone= new VerticalPanel();
    final VerticalPanel fbfriendsbox= new VerticalPanel();
    //Buttons
    final Button fblogin = new Button("Login");
	final Button fbclose = new Button("Close");   
	final Button share = new Button("Share");
	final Button fbloginLink = new Button("Login");
    final Button fblogoutLink = new Button ("Logout") ;
    //Variablen
	private int notstup=0;
	int start=1;

	int fbnotstup=0;
	private String API_KEY = "cc36d76b861202046e7862e04cc650bf"; //-web-cc36d76b861202046e7862e04cc650bf-localhost-707cee0b003b01d52b2b6a707fa1202b-
	long uid;
    final FacebookApi apiClient = GWT.create ( FacebookApi.class );
	private Stream stream;
    //Horizontal Panels
    final HorizontalPanel[] fbfriends = new HorizontalPanel[100];
    final HorizontalPanel friendRequestWrapper = new HorizontalPanel ();
    //Dialog Boxes
    final WindowPanel fbpermission = new WindowPanel("Permission needed!!");
	//Text Areas
	final private TextArea  streampub = new TextArea();
	//Callbacks
	private LikeCom likecom = new LikeCom();
	//private UserInfoCallback userinfocall = new UserInfoCallback(0);
	//private UserInfoCallback friendsinfocall = new UserInfoCallback(1);
	Loader l = new Loader();
	
	WindowPanel fb = new WindowPanel("Facebook Widget");
	WindowPanel fbnotificationbox = new WindowPanel("Notification");
	ScrollPanel streamscroll = new ScrollPanel();
	final ImageButton postream = new ImageButton(Caption.IMAGES.toolRefresh());
	final ImageButton permission = new ImageButton(new Image("wicon/permission.png"));
	final ImageButton shownotbox = new ImageButton(new Image("wicon/message.png"));
	final ImageButton logout = new ImageButton(new Image("wicon/logout.png"));
	int logorout =0;
	
	public static int close_open =0;

/*-**************************************************************************************************************
 *********************     Widget and Panel Load		*********************************************************
 ***************************************************************************************************************/
	
 		public void onFacebookClick(){	
				if(!(fb.isShowing()||fb.isCollapsed())){
					close_open =1;
					fbloginLink.addClickHandler ( new LoginClickHandler ()  );
			        fblogoutLink.addClickHandler ( new LogoutClickHandler()  );
					FacebookConnect.init ( API_KEY );
			        FacebookConnect.waitUntilStatusReady ( new ThenRenderRestOfApp() );
			        if(start==1){
			        loadFBPanel(fb);
			        start=0;
			        }
			        fb.show();	
					}else{
						if(fb.isVisible()){
							fb.setVisible(false);
							close_open =0;
							fbnotificationbox.setVisible(false);
							fbpermission.setVisible(false);
						}else{
							fb.setVisible(true);
							close_open =1;
							fb.toFront();
						}
					}
	}
	
	    @SuppressWarnings("deprecation")
		private void loadFBPanel(WindowPanel wfb){
				l.addLoader(fbmainfield);
				wfb.setWidget(fbmainfield);	
				fb.addStyleName("fbroom");
				fb.getHeader().clear();
	            fb.getHeader().add(logout, CaptionRegion.RIGHT);
		         fb.getHeader().add(postream, CaptionRegion.RIGHT);
		         fb.getHeader().add(shownotbox, CaptionRegion.RIGHT);
		         fb.getHeader().add(permission, CaptionRegion.RIGHT);
				streampub.setSize("500px","40px");
				
				share.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						publichStream(streampub.getText());
						streampub.setText("");
						streampub.setSize("500px","40px");
					}
				});
				
		    	streampub.addFocusListener(new FocusListener() {
					public void onLostFocus(Widget sender) {
						if(streampub.getText().equals("")){
						streampub.setSize("500px","40px");
						}
					}

					public void onFocus(Widget sender) {
						streampub.setSize("500px","100px");						
					}
				});
		    	
				permission.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(logorout==3){
						getPermission(permission.getAbsoluteLeft(),permission.getAbsoluteTop());
						}
					}
				});
				shownotbox.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(logorout==3){
						getNotifications();
						}
					}
				});
				logout.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(logorout==3){
						logout(0);
						}
					}
				});
				 
		}    
	    
/*-********************************************************************************************************
  ************************		Login - Logout         ****************************************************
  ********************************************************************************************************/
	    public void login(){
	    	fbmainfield.clear();
	        fb.setSize("600px",Window.getClientHeight()+"px");
        	fb.layout();
	        fb.show();
	        logorout=3;
	        HorizontalPanel hz = new HorizontalPanel();
    		hz.add(streampub);
    		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
    		hz.add(share);
    		fbmainfield.add(hz);
			streamscroll.add(fbstreamzone);
			streamscroll.setSize("620px", (Window.getClientHeight()-80)+"px");
			fbmainfield.add(streamscroll);
            loadStream(); 
	    }
	    
	    
	    public void logout(int info){
	    if(info==0){
	    	fbmainfield.add(new HTML("<hfb1>Facebook Login</hfb1>"));
        	fbmainfield.add(new HTML("<p></p>"));
	        fbmainfield.clear();
	        fbnotification.clear();
	        notificationfield.clear();
	        groupInvitesWrapper.clear();
	        eventInvitesWrapper.clear();
	        fbstream.clear();
	        fbstreamzone.clear();
	        fbfriendsbox.clear();
	        friendRequestWrapper.clear();
        	fbnotificationbox.setVisible(false);
        	for(int i=0;i<fbfriends.length;i++){
        	if(fbfriends[i]!=null){
            fbfriends[i].clear();
        	}
        	}
        	fbmainfield.add(new HTML("<img src=\"token/facebook_header.png\"></img>"));
        	fbmainfield.add(new HTML("<hfb1>Facebook Login</hfb1>"));
        	fbmainfield.add(new HTML("<p></p>"));
            FacebookConnect.logout();
        	fbmainfield.add(fbloginLink);
        	fbmainfield.setSize("220px","200px");
        	fb.setSize("220px", "200px");
        	fb.layout();
        	fb.show();
	    }else{
	    	
	    	fbmainfield.add(new HTML("<img src=\"token/facebook_header.png\"></img>"));
	    	fbmainfield.add(new HTML("<hfb1>Facebook Login</hfb1>"));
        	fbmainfield.add(new HTML("<p></p>"));
        	fbmainfield.add(fbloginLink);
        	fbmainfield.setSize("220px","200px");
        	fb.setSize("220px", "200px");
        	fb.layout();
        	fb.show();
	    }	   
	    logorout=0;
	    }
	    

 //------------------------------------------------------------------------------------------------
	    private class LoginClickHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	            FacebookConnect.requireSession ( new AsyncCallback<Boolean> () {
	                public void onFailure(Throwable caught) {
	                	 FacebookException fe = ( FacebookException ) caught;
                         ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                         ui.center ();
	                }
	                public void onSuccess(Boolean result) {
	                if(result){
	                	login();
	                }
	                }
	            });
	        }
	    }
	    

	    private class ThenRenderRestOfApp implements AsyncCallback<ConnectState> {
	        public void onFailure(Throwable caught) {
	        	 FacebookException fe = ( FacebookException ) caught;
                 ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                 ui.center ();
	        }
	        public void onSuccess(ConnectState result) {
                l.removeLoader();
	            if ( result.equals(ConnectState.userNotLoggedIn)) {
	            	logout(1);
	            } else {
	            	login();
	            }
	        }
	    };
	    
	    private class LogoutClickHandler implements ClickHandler {
	        public void onClick(ClickEvent event) {
	        	logout(0);
	        }
	    };

	    
	    
	
	    
	    
	    
/*-**********************************************************************************************************
 *****************      GetUserInfo and show Friends-and-UserInfo            ******************************** 
 ************************************************************************************************************/
	    /*
	    private class UserInfoCallback implements AsyncCallback<List<UserInfo>> {
	    	int uof;
	    	private UserInfoCallback(int uofr){
	    		this.uof=uofr;
	    	}

	        public void onFailure(Throwable caught) {
	        	 FacebookException fe = ( FacebookException ) caught;
                 ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                 ui.center ();
	        }

	        public void onSuccess(List<UserInfo> result) {
	            showUserInfo ( result, uof);
	        }
	        
	    };
	    */
	    /*
	    private void showUserInfo ( List<UserInfo> userInfo,int uf ) {
	        final UserInfo ui = userInfo.get(0);
	        final Image friendpimg = new Image(ui.getPic());
	        friendpimg.setTitle(""+ui.getName()+"%"+ui.getProfileUrl()+"%"+ui.getUid());
	        friendpimg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					
				}
			});
	        
	        if(friendui%8==0&&friendui!=0){
	        	friendrowcount++;
	        	fbfriends[friendrowcount]= new HorizontalPanel();
	        	fbfriendsbox.add(fbfriends[friendrowcount]);
	        }
	        friendui++;
	        fbfriends[friendrowcount].add(friendpimg);
	    } 	
	       */
	    /*
	    private void doGetUserInfo ( Long uid ) {
	        List<Long> uids = new ArrayList<Long> ();
	        uids.add ( new Long ( uid ) );
	        // Add fields that should be returned
	        List<String> fbuserlist = new ArrayList<String>();
	        fbuserlist.add ( "pic" );
	        fbuserlist.add ( "name" );
	        fbuserlist.add ( "political" );
	        fbuserlist.add ( "profile_url" );
	        fbuserlist.add ( "proxied_email" );
	        fbuserlist.add ( "relationship_status" );
	        fbuserlist.add ( "status");
	        apiClient.usersGetInfo ( uids, fbuserlist, userinfocall );
	    }	     
	    */
	    
	    
	    
	    
	    
/*-*********************************************************************************************************
 * *********************       GetFriends     **************************************************************
 **********************************************************************************************************/
	/*    private void doGetFriendsInfo ( Long uid ) {
	        List<Long> uids = new ArrayList<Long> ();
	        uids.add ( new Long ( uid ) );
	        // Add fields that should be returned
	        List<String> fbuserlist = new ArrayList<String>();
	        fbuserlist.add ( "pic" );
	        fbuserlist.add ( "name" );
	        fbuserlist.add ( "political" );
	        fbuserlist.add ( "profile_url" );
	        fbuserlist.add ( "proxied_email" );
	        fbuserlist.add ( "relationship_status" );
	        fbuserlist.add ( "status");
	        apiClient.usersGetInfo ( uids, fbuserlist, friendsinfocall );
	    }	   
	    
	    private void getFriends(){
			fbfriends[0]= new HorizontalPanel();
			fbfriendsbox.add(fbfriends[0]);
	    	apiClient.friendsGetExtended (  new AsyncCallback<List<User>> () {
               public void onFailure(Throwable caught) {
            	   FacebookException fe = ( FacebookException ) caught;
                   ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                   ui.center ();
               }
               
               public void onSuccess(List<User> result) {
            	     
                       for ( User user : result ) {
                    	   doGetFriendsInfo(user.getUid()); 
                    	   friendcount++;
                       }  
                      
               }     
       });		    	
}	    */


	
	    
/*-**********************************************************************************************************************
 *************************************         Notification         *****************************************************
 ***********************************************************************************************************************/
	    	    @SuppressWarnings("deprecation")
	   public void getNotifications(){
	                apiClient.notificationsGet(new AsyncCallback<List<NotificationRequest>> () {
	                        public void onFailure(Throwable caught) {
	                        	 FacebookException fe = ( FacebookException ) caught;
	                             ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
	                             ui.center ();

	                        }
	                        public void onSuccess( List<NotificationRequest> result ) {       
	                                for ( NotificationRequest nc : result ) {
	                                	if(nc.getTypeEnum()==NotificationType.messages){
	                                        if ( nc.getUnread() != null ) {
	                                        	HorizontalPanel hz = new HorizontalPanel();
	                                        	Image img = new Image("fbicons/messagewaiting.png");
	                                        	img.setSize("50px","50px");
	                                        	hz.add(img);
	                                        	hz.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	                                        	hz.add(new HTML("<p>Unread:"+nc.getUnread()+"</p>"));
	                                        	img.addClickHandler(new ClickHandler() {
													public void onClick(ClickEvent event) {
														Window.open("http://www.facebook.com/?sk=messages&filter=[fb]unread#!/?sk=messages&cs=2", "","");
													}
												});
	                                        	notificationfield.add (hz); 
	                                        }
	                                	}
	                                        // Friend requests.
	                                        if ( nc.getTypeEnum() == NotificationType.friend_requests){
	                                        	HorizontalPanel hz = new HorizontalPanel();
	                                        	Image img = new Image("fbicons/friend.png");
	                                        	img.setSize("50px","50px");
	                                        	hz.add(img);
	                                            if(nc.getRequestIds().size() > 0 ) {
	                                            hz.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	                                            hz.add(new HTML("<p>You have friend invites ("+nc.getRequestIds().size()+")</p>"));
	                                            notificationfield.add (hz);
	                                        	notificationfield.add (friendRequestWrapper );
	                                        	friendRequestWrapper.clear();        
	                                        }else{
	                                        	hz.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	                                        	hz.add(new HTML("<p>No friend invites</p>"));
	                                            notificationfield.add (hz);
	                                        }
	                                        }
	                                        
	                                        // Group invites
	                                           if ( nc.getTypeEnum() == NotificationType.group_invites ) {
	                                        	    HorizontalPanel hz = new HorizontalPanel();
		                                        	Image img = new Image("fbicons/group_requests.png");
		                                        	img.setSize("50px","50px");
		                                        	hz.add(img);    
		                                        	notificationfield.add ( hz );
	                                        	    notificationfield.add ( groupInvitesWrapper );
	                                        	    groupInvitesWrapper.clear();
	         
	                                                hz.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	                                                if(nc.getRequestIds().size()==0){
	                                                	hz.add(new HTML("<p>No group invites</p>"));
	                                                }else{
	                                                	hz.add(new HTML("<p>Group invites waiting ("+nc.getRequestIds().size()+")</p>"));
	                                                }
	                                        }
	                                           Xfbml.parse(notificationfield);
	                                }
	                        }
	                });
	                if(notstup==0){
	                	fbnotificationbox.setAnimationEnabled(true);
	                	fbnotificationbox.setResizable(false);
	                	VerticalPanel vp = new VerticalPanel();
	                	vp.add(fbnotification);
	                	fbnotificationbox.setWidget(vp);
	                	fbnotificationbox.setVisible(true);
	                	fbnotificationbox.setSize("240px","200px");
	                	fbnotificationbox.setPopupPosition(600, Window.getClientHeight()/2-150);
	                	fbnotificationbox.show();
	                }
	                notificationfield.clear();
	                notificationfield.setWidth("240px");
	                fbnotification.add(notificationfield);
	               
	        }	    
	     
	    	    
	    	    
	    	    
/*-**********************************************************************************************************************
 *************************************   Stream      ********************************************************************
 ***********************************************************************************************************************/


	    
	    	    
	    	    private void updateStream(final VerticalPanel streamBody){
	    	    	apiClient.streamGet ( new AsyncCallback<Stream> () {
	    	            public void onFailure(Throwable caught) {
	    	            	 FacebookException fe = ( FacebookException ) caught;
	                         ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
	                         ui.center ();
	    	            }

	    	            public void onSuccess(Stream result) {
	    	                stream = result;
	    	                renderPosts ( streamBody, result.getPosts () );
	    	            }
	    	        } );
	    	    }
	    	    
	    	    void renderMainContent(final VerticalPanel addContentToPnl) {
	    	        final VerticalPanel streamBody = new VerticalPanel ();
	    	        streamBody.setWidth("600px");
	    	        // Click posts link
	    	        postream.addClickHandler ( new ClickHandler () {
	    	            public void onClick(ClickEvent event) {
	    	            	if(logorout==3){
	    	            	updateStream(fbstreamzone);
	    	            	}
	    	            }
	    	        } );
	    	        addContentToPnl.add ( streamBody );

	    	        // Start loading
   	        // Get stream from facebook.
	    	        apiClient.streamGet ( new AsyncCallback<Stream> () {
	    	            public void onFailure(Throwable caught) {
	    	                Window.alert("Permission needed");
	    	                getPermission(permission.getAbsoluteLeft(),permission.getAbsoluteTop());
	    	            }

	    	            public void onSuccess(Stream result) {
	    	                stream = result;
	    	                renderPosts ( streamBody, result.getPosts () );
	    	            }
	    	        } );
	    	    }

	    	    void renderPosts(VerticalPanel addContentToPnl, JsArray<Post> posts) {

	    	        addContentToPnl.clear ();
	    	        addContentToPnl.setWidth("600px");

	    	        for (Post post : Util.iterate ( posts )) {

	    	            try {
	    	                GWT.log ( "Render: " + new JSONObject ( post ).toString (), null );
	    	                if(!(post.getMessage().length()==0&&post.getAttachment().getDescription().length()<20)){
	    	                final VerticalPanel postContentPnl = new VerticalPanel ();
	    	                postContentPnl.addStyleName ( "post" );
	    	                String message = post.getMessage();
	    	                message = message.replace("\n", "<br/>");
	    	                HorizontalPanel hztext= new HorizontalPanel();
	    	                hztext.add ( new FbName ( post.getActorId ()));
	    	                hztext.add ( new HTML ("&nbsp;&nbsp;"+message) );
	    	                postContentPnl.add(hztext);
	    	                if (post.getAttachment().getDescription() != null) {
	    	                    postContentPnl.add ( createAttachmentUI ( post.getAttachment () ) );
	    	                } 
	    	                postContentPnl.add ( createOptionUI ( post,0));
	    	                // Add profilepic on the left, postcontent on the right
	    	                HorizontalPanel p = new HorizontalPanel ();
	    	                p.addStyleName ( "post" );
	    	                VerticalPanel vpic = new VerticalPanel();
	    	                getUserPic(post.getActorId(), vpic);	    	                
	    	                //vpic.add ( new FbProfilePic ( post.getActorId () ) );
	    	                
	    	                p.add(vpic);
	    	                p.add(new HTML("&nbsp;&nbsp;"));
	    	                p.add ( postContentPnl );
	    	                addContentToPnl.add ( p );
	    	                Xfbml.parse(fbstreamzone);
	    	                }
	    	            } catch ( Exception e ) {
	    	                GWT.log ( "Unkown exception ", e );
	    	            }
	    	         
	    	        }
	    	    }
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    private void getUserPic( Long uid , VerticalPanel p ) {
	    	        List<Long> uids = new ArrayList<Long> ();
	    	        uids.add ( new Long ( uid ) );
	    	        // Add fields that should be returned
	    	        List<String> fbuserlist = new ArrayList<String>();
	    	        fbuserlist.add ( "pic_square" );
	    	        apiClient.usersGetInfo ( uids, fbuserlist, new UserPicCallback(p) );
	    	    }	     
	    	    
	    	    
	    	    
	    	    private class UserPicCallback implements AsyncCallback<List<UserInfo>> {
	    	    	VerticalPanel p;
	    	    	private UserPicCallback(VerticalPanel p){
	    	    	this.p =p;
	    	    	}

	    	        public void onFailure(Throwable caught) {
	    	        	 FacebookException fe = ( FacebookException ) caught;
	                     ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
	                     ui.center ();
	    	        }

	    	        public void onSuccess(List<UserInfo> result) {
	    	           p.add(getUserPicPanel(result));
	    	        }
	    	        
	    	    };
	    	    
	    	   private Image getUserPicPanel( List<UserInfo> result){
	    		        final UserInfo ui = result.get(0);
	    		        return new Image(ui.getPicSquare());   		   
	    	   }
	    	    
	    	    	 	 

	    	    public Widget createAttachmentUI(Attachment attachment) {
	    	        VerticalPanel p = new VerticalPanel ();
	    	        HorizontalPanel thumbs = new HorizontalPanel ();
	    	        thumbs.setSpacing ( 10 );
	    	        for (Media m : Util.iterate ( attachment.getMedia () )) {	
	    	        	if(m.getSrc()!=null){
	    	        		thumbs.add ( new Image ( m.getSrc () ) );
	    	                thumbs.add(new HTML("<hfdescribe>"+attachment.getDescription()+"</hfdescribe>"));	
	    	        	}
	    	            if (m.getTypeEnum () == Type.video) {	    	            	
	    	            	final Button showVideo = new Button("See Video");
	    	                String video=  m.getVideo ().getSourceUrl();
	    	               	final DecoratedLayoutPopupPanel videopopup = new DecoratedLayoutPopupPanel(true);
	    	                String[] gplatform= video.split("\\.");
	    	               String iplatform="";
	    	                for(int i=0;i<gplatform.length;i++){
	    	                	if(gplatform[i].equals("http://vimeo")){
	    	                		iplatform="vimeo";
	    	                	}else if(gplatform[i].equals("youtube")){
	    	                		iplatform="youtube";
	    	                	}
	    	                }
	    	          
	    	                final String platform = iplatform;
	    	                if(platform.equals("youtube")){
	    	                	final String[] ivideo =video.replace("/v/", "/embed/").split("&");
	    	                showVideo.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
			
									loadvideopopup(ivideo[0],showVideo,platform,videopopup);
								}
							});
	    	                }else if(platform.equals("vimeo")){
	    	                	final String[] ivideo =video.replace("v", "embed").split("=");
		    	                showVideo.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										
											loadvideopopup(ivideo[ivideo.length-1],showVideo,platform,videopopup);
											
									}
								});
	    	                }else{
	    	                	final String ivideo =video;
		    	                showVideo.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										
											loadvideopopup(ivideo,showVideo,"",videopopup);
																		
									}
								});	
	    	                }
	    	                p.add (showVideo);
	    	            }
	    	        }
	    	        p.add ( thumbs );
	    	        return p;
	    	    }
	    	    
	    	    public void loadvideopopup(String source, Button b, String p, DecoratedLayoutPopupPanel d){
	    	    	d.setAnimationEnabled(true);
	    	    	LayoutPanel box = new LayoutPanel();
	                if(p.equals("youtube")){
	    	    	box.add(new HTML("<iframe class=\"youtube-player\" type=\"text/html\" width=\"480\" height=\"385\" src=\""+source+"\" frameborder=\"0\"></iframe>"));
	                }else if(p.equals("vimeo")){
	                box.add(new HTML("<iframe src=\"http://player.vimeo.com/video/"+source+"\" width=\"400\" height=\"225\" frameborder=\"0\"></iframe>"));	
	                }else{
	                box.add(new HTML("<iframe width=\"480\" height=\"385\" src=\""+source+"\"></iframe>"));	
	                }
	                d.add(box);
	                d.pack();
	    	    	d.center();
	    	    }

	    	    public Widget createCommentsUI(Comments comments, String s,int option) {
	    	        VerticalPanel o = new VerticalPanel();
	    	        o.addStyleName("comment");
	    	        doCommentsGet(s,o,option);
	    	        return o;
	    	    }

	    	    public Widget createOptionUI(final Post post, int option) {
	    	    	if(option==0){
	    	    	final Likes l = post.getLikes();
	    	    	final VerticalPanel v = new VerticalPanel();
	    	    	final VerticalPanel comvp = new VerticalPanel();
	    	    	final HorizontalPanel h = new HorizontalPanel();
	    	    	h.addStyleName("postoption");
	    	        boolean likeuser = l.getUserLikes();
	    	        final Button like = new Button();
	    	        if(likeuser){
	    	        	like.setText("Remove Like!");
	    	        }else{
	    	        	like.setText("I like!");
	    	        }
	    	        if(post.getLikes().getUserLikes()){
						like.setTitle("like");
					}else{
						like.setTitle("notlike");
					}
	    	        like.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							if(like.getTitle().equals("like")){						
								apiClient.streamRemoveLike(post.getPostId(), likecom);
							}else{
								apiClient.streamAddLike(post.getPostId(), likecom);
							}
							
						}
					});
	    	        h.add(like); 
	    	        Button likes = new Button(l.getCountString()+" like this.");
	    	        if(l.getCount()!=0){
	    	        likes.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

						}
					});
	    	        h.add(new HTML("&nbsp;&nbsp;&nbsp;"));
	    	        h.add(likes);
	    	        }
	    	        final Comments c = post.getComments();
	    	        v.add(h);
	    	        
	    	        if(c.getCount()>3){
	    	        comvp.add(createCommentsUI ( c,post.getPostId(),0));	
	    	        final Button commen = new Button("Show all "+c.getCount()+" comments.");	
	    	        commen.setTitle("show");
	    	        commen.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							comvp.clear();
							if(commen.getTitle().equals("show")){
								commen.setText("Hide Comments");
								commen.setTitle("hide");
								comvp.add(createCommentsUI ( c,post.getPostId(),1));
							}else{
								comvp.add(createCommentsUI ( c,post.getPostId(),0));
								commen.setText("Show all "+c.getCount()+" comments.");
								commen.setTitle("show");
							}
						}
					});
	    	        h.add(new HTML("&nbsp;&nbsp;&nbsp;"));
	    	        h.add(commen);
	    	        
	    	        }else{
	    	        	comvp.add(createCommentsUI ( c,post.getPostId(),0));	
	    	        }
	    	        v.add(comvp);
	    	        
	    	        return v;  
	    	    }else{
	    	    	return null;
	    	    }
	    	    }
	    	   
	    	    public HTML asJson(String header, JavaScriptObject o) {
	    	        return new HTML ( "<b>" + header + "</b>:" + new JSONObject ( o ).toString () );
	    	    }
	    	        
	    	    private void loadStream(){
	                renderMainContent(fbstreamzone);
	    	    }

	    	    
	    	    
	    	    
/*-**********************************************************************************************************************
 *********************************      Get Permission        ***********************************************************
 ***********************************************************************************************************************/	    		    	    
	    	    	
	    	    public void getPermission(int al,int tl) {  
							DecoratedLayoutPopupPanel popup = new DecoratedLayoutPopupPanel(true);
					        popup.setAnimationEnabled(true);
							 final VerticalPanel per = new VerticalPanel ();
				    	        per.add ( new HTML ( "FbPromptPermission" ) );
				    	  
				    	        FbPromptPermission promptPerm  = 
				    	            new FbPromptPermission ("Get Permission", 
				    	                        Permission.create_event, 
				    	                        Permission.create_note,
				    	                        Permission.read_stream,
				    	                        Permission.publish_stream);
				    	        per.add ( promptPerm );
				    	        Xfbml.parse (per);		
				    	        popup.add(per);
						        popup.pack();
						        popup.setPopupPosition(al-150, tl);
						        popup.show();							    	    
	    	    }	    	    

	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
/*-**********************************************************************************************************************
 *********************************      Publish Stream        ***********************************************************
 ***********************************************************************************************************************/	    		    	    
	    	    
	    	    private void publichStream(String message){
	    	    List<ActionLink> links = new ArrayList<ActionLink> ();
	            links.add ( ActionLink.newInstance ( "Go to Gwittit", "http://gwittit.appspot.com" ) ); 
	            links.add ( ActionLink.newInstance ( "Go to GWT", "http://code.google.com/webtoolkit/" ) );
	            apiClient.streamPublish (message,
	                                        null, 
	                                        null, 
	                                        null, 
	                                        "", 
	                                        false, 
	                                        null, 
	                                        true,
	                                        new MyCallback () );
	    	    }
	    	    
	    	    private class MyCallback implements AsyncCallback<JavaScriptObject> {
	    	        public void onFailure(Throwable caught) {
	    	        	FacebookException fe = ( FacebookException ) caught;
                        ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                        ui.center ();
	    	        }
	    	        public void onSuccess(JavaScriptObject result) {
	    	            renderMainContent(fbstream);
	    	        }
	    	    }
	    	    
	    	    public class PublishStreamSimpleHandler implements ClickHandler {
	    	        public void onClick(ClickEvent event) {
	    	            FacebookConnect.streamPublish ();
	    	        }
	    	    }

	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	        
/*-**********************************************************************************************************************
 *********************************      Get Comment        *************************************************************
 ***********************************************************************************************************************/	    		    	    
	    	     
	    	    @SuppressWarnings("deprecation")
				private void doCommentsGet(final String id, VerticalPanel vpc, int option) {
	    	    	HorizontalPanel hzac = new HorizontalPanel();
	    	    	hzac.add(new HTML("&nbsp;&nbsp;&nbsp;"));
	    	    	final TextArea addc = new TextArea();
	    	    	addc.addFocusListener(new FocusListener() {
						
						public void onLostFocus(Widget sender) {
							if(addc.getText().equals("")){
							addc.setWidth("200px");
							}
						}
						
						public void onFocus(Widget sender) {
							addc.setWidth("400px");
							
						}
					});
	    	    	Button add = new Button("Add");
	    	    	hzac.add(addc);
	    	    	add.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							addComment(addc.getText(), id);
							addc.setText("");
							addc.setWidth("200px");
						}
					});
	    	    	hzac.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
	    	    	hzac.add(add);
	    	        apiClient.streamGetComments(id, new CommentsGetCallback(vpc,hzac,option));
	    	    }

	    	    private class CommentsGetCallback implements AsyncCallback<List<Comment>> {
	    	    	VerticalPanel comment = new VerticalPanel();
	    	    	int opti =0;
	    	    	public CommentsGetCallback(VerticalPanel vpc,HorizontalPanel hzac,int opt){
	    	    		vpc.clear();
	    	    		vpc.add(comment);
	    	    		vpc.add(hzac);
	    	    		opti= opt;
	    	    	}
	    	        public void onFailure(Throwable caught) {
	    	        	FacebookException fe = ( FacebookException ) caught;
                        ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                        ui.center ();
	    	        }
	    	        public void onSuccess(List<Comment> result) {
	    	        	comment.clear();
	    	            comment.add(makeCommentsUI(result,opti));
	    	        }
	    	       
	    	    }
	    	    
	    	    
	    	    private VerticalPanel makeCommentsUI(List<Comment> result,int option){
	    	         VerticalPanel commentbox= new VerticalPanel();
	    	         if(option==1){
	    	    	for(int i=0;i<result.size();i++){
	    	    		Comment c = result.get(i);    	    		
	    	    		HorizontalPanel hz = new HorizontalPanel();
	    	    		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
	    	    		VerticalPanel vpicc = new VerticalPanel();
	    	    		getUserPic(c.getFromId(), vpicc);
	    	    		hz.add(vpicc);
	    	    		hz.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
	    	    		TextArea t = new TextArea();
	    	    		t.setText(c.getText());
	    	    		t.setReadOnly(true);
	    	    		t.setSize("400px","50px");
	    	    		hz.add(t);
	    	    		commentbox.add(hz);
	    	    		if(c.getFromId().equals(apiClient.getLoggedInUser())){
	    	    			final Button remove = new Button("Remove");
	    	    			final String xid = c.getXid();
	    	    			final String id = c.getId();
	    	    			remove.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									removeComment(xid, id);										
								}
							});
	    	    			commentbox.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
	    	    			commentbox.add(remove);	
	    	    			commentbox.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
	    	    		}
	    	    	}
	    	         }else{	
	    	        	 int l=0;
	    	        	 if(result.size()>3){
	    	        		 l=3;
	    	        	 }else{
	    	        		 l=result.size();
	    	        	 }
	    	        	 for(int i=0;i<l;i++){
	    	        		 
	    	        		 Comment c = result.get(i);
	 	    	    		HorizontalPanel hz = new HorizontalPanel();
	 	    	    		hz.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
	 	    	    		VerticalPanel vpicc = new VerticalPanel();
		    	    		getUserPic(c.getFromId(), vpicc);
		    	    		hz.add(vpicc);
		    	    		hz.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
	 	    	    		TextArea t = new TextArea();
	 	    	    		t.setText(c.getText());
	 	    	    		t.setReadOnly(true);
	 	    	    		t.setSize("400px","50px");
	 	    	    		hz.add(t);
	 	    	    		commentbox.add(hz);
	 	    	    		if(c.getFromId().equals(apiClient.getLoggedInUser())){
		    	    			final Button remove = new Button("Remove");
		    	    			final String xid = c.getXid();
		    	    			final String id = c.getId();
		    	    			remove.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										removeComment(xid, id);										
									}
								});
		    	    			commentbox.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		    	    			commentbox.add(remove);	
		    	    			commentbox.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		    	    		}
	    	        	
	 	    	    	}	 
	    	         }
	    	    	
	    	    	return commentbox;
	    	    }
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
/*-**********************************************************************************************************************
 *********************************      Add Comment        *************************************************************
 ***********************************************************************************************************************/	    	    
	
	    	    private void addComment(String text, String postid){
	    	    	apiClient.streamAddComment(postid, text, new AddCmt());
	    	    	updateStream(fbstreamzone);
	    	    }
	    	    
	    	    
	    	    private class LikeCom implements AsyncCallback<JavaScriptObject> {
	    	        public void onFailure(Throwable caught) {
	    	        	FacebookException fe = ( FacebookException ) caught;
                        ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                        ui.center ();
	    	        }
	    	        public void onSuccess(JavaScriptObject o) {
	    	        	
	    	        }
	    	    }
	    	    
	    	    
	    	    private class AddCmt implements AsyncCallback<JavaScriptObject> {
	    	        public void onFailure(Throwable caught) {
	    	        	FacebookException fe = ( FacebookException ) caught;
                        ErrorResponseUI ui = new ErrorResponseUI ( fe.getErrorMessage () );
                        ui.center ();
	    	        }
	    	        public void onSuccess(JavaScriptObject o) {
	    	        	updateStream(fbstreamzone);
	    	        }
	    	    }
	    	    
	    	     
	    	    	    	   
	    	    private void removeComment(String xid,String commentId){
	    	    	apiClient.commentsRemove(xid, commentId, new AsyncCallback<JavaScriptObject>() {
	    	    		public void onFailure(Throwable caught){
	    	    			
	    	    		}
	    	    		public void onSuccess(JavaScriptObject o){
	    	    			updateStream(fbstreamzone);
	    	    		}
					});
	    	    }
	    	    
	    	    
	    	    
}
