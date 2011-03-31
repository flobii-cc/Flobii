package co.cc.flobii.ui.mpwidget;



import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;

import co.cc.flobii.ui.client.Flobii;
import co.cc.flobii.ui.mpwidget.slider.Slider;
import co.cc.flobii.ui.mpwidget.slider.SliderEvent;
import co.cc.flobii.ui.mpwidget.slider.SliderListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;




public class MpWidg{
	
	static int[] lista;
	static JSONArray jsona;
	static int count =99;
	static WindowPanel wplayer = new WindowPanel("Jamendo Radios");
    static int pp =0;
    static int start=0;
    static String downtrack="";
    static String downalbum="";
    final static ImageButton next = new ImageButton(new Image("wicon/next.png"));
    final static ImageButton play_pause = new ImageButton(new Image("wicon/pause.png"));
	final static ImageButton prev = new ImageButton(new Image("wicon/prev.png"));
	final static ImageButton radios = new ImageButton(new Image("wicon/radios.png"));
	final static ImageButton downloadtrack = new ImageButton(new Image("wicon/download.png"));
	final static ImageButton downloadalbum = new ImageButton(new Image("wicon/download1.png"));
	final static ImageButton jamendo = new ImageButton(new Image("wicon/jamendo.png"));
    final static PopupPanel popup= new PopupPanel();
	public static String sid="";
	static ImageButton rock = new ImageButton(new Image("jamendo/rock.gif"));
    static ImageButton hiphop = new ImageButton(new Image("jamendo/hiphop.gif"));
    static ImageButton dance = new ImageButton(new Image("jamendo/dance.gif"));
    static ImageButton jazz = new ImageButton(new Image("jamendo/jazz.gif"));
    static ImageButton lounge = new ImageButton(new Image("jamendo/lounge.gif"));
    static ImageButton metal = new ImageButton(new Image("jamendo/metal.gif"));
    static VerticalPanel vpplayer = new VerticalPanel();
    static HorizontalPanel hzplayer = new HorizontalPanel();
    static VerticalPanel vpinfoplay = new VerticalPanel();
    static FlowPanel cover = new FlowPanel();
    static VerticalPanel vpplayerui = new VerticalPanel();
    static VerticalPanel vtime = new VerticalPanel();
    static int close_open =0;
    static Image sm_pause= new Image("wicon/pause.png");
    static Image sm_play = new Image("wicon/play.png");
    static String songstream ="";

	
	public static void loadMpWidg(){
		initRadioChoose();
		vpplayerui.setSize("540px","60px");
		vpplayer.setWidth("560px");
		vpinfoplay.setSize("400px","80px");
		cover.setSize("100px", "100px");
		hzplayer.setHeight("120px");
	}
	
	public static void onJamendoClick(){
		if(!(wplayer.isShowing()||wplayer.isCollapsed())){	
			loadPlayer();
			close_open=1;
		}else{
			if(wplayer.isVisible()){
				wplayer.setVisible(false);
				close_open=0;
			}else{
				wplayer.setVisible(true);
				close_open=1;
				wplayer.toFront();
			}
		}
	}

	static void createWindowPanel() {
    	createFixedWindowPanel();
      }

	native static void setJBI()/*-{
	  $wnd.$('.jamendo_button').children('img').attr('src' , "token/mediaplayer_hover.png");
}-*/;
	
	static void loadPlayer(){
		vpplayer.clear();
		vpplayer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vpplayer.add(hzplayer);
		hzplayer.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		vpinfoplay.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hzplayer.add(vpinfoplay);
		hzplayer.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		hzplayer.add(cover);
		vpplayer.add(new HTML("<p/>"));
		vpplayer.add(vpplayerui);
		vpplayerui.setStyleName("vplaymp");
		vpplayerui.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vpplayerui.add(loadSlider());
		vpplayerui.add(vtime);
		vtime.add(new HTML("&nbsp;&nbsp;<b><font face=\"WDS\" size=\"3\">0:00/0:00</font></b>&nbsp;&nbsp;"));
		popup.center();
		}
	
	
	
	static void loadList(JSONArray json,int[] rlist){
		close_open=1;
		setJBI();
		lista = rlist;
		jsona = json;
		if(!wplayer.isActive()){
		createWindowPanel();
		}
		count=99;
		loadSong();
	}

	
	native static void checkHTML5Support(String mp3, String ogg)/*-{
	  var checkAudio = document.createElement('audio');
	  if(checkAudio.canPlayType){
	  	if(checkAudio.canPlayType('audio/mpeg')){
			@co.cc.flobii.ui.mpwidget.MpWidg::songstream= mp3;
	  	}else if(checkAudio.canPlayType('audio/ogg; codecs="vorbis"')){
	  		@co.cc.flobii.ui.mpwidget.MpWidg::songstream= ogg;
	  	}	
	  }else{	
	  		@co.cc.flobii.ui.mpwidget.MpWidg::songstream= mp3;
	  }
}-*/;
	
	
	public static void loadSong(){
		if(!sid.equals("")){
			destroySong(sid);
			}
		JSONObject jsono =jsona.get(lista[count]).isObject();
		String id = jsono.get("id").toString().replace("\"", "");
		String ida = jsono.get("album_id").toString().replace("\"", "");
		String img = "http://api.jamendo.com/get2/image/album/redirect/?id="+ida+"&imagesize=100";		
		//checkHTML5Support("http://api.jamendo.com/get2/stream/track/redirect/?id="+id+"&streamencoding=mp31", "http://api.jamendo.com/get2/stream/track/redirect/?id="+id+"&streamencoding=ogg2");
		songstream = "http://api.jamendo.com/get2/stream/track/redirect/?id="+id+"&streamencoding=mp31";
		String artist = jsono.get("artist_name").toString().replace("\"", "");
		String album = jsono.get("album_name").toString().replace("\"", "");
		String name = jsono.get("name").toString().replace("\"", "");
		downtrack ="http://www.jamendo.com/download/track/"+id+"/do";
		downalbum ="http://www.jamendo.com/download/album/"+ida+"/do";
		if(count==0){
			count=99;
		}
		count--;
		vpinfoplay.clear();
		vpinfoplay.addStyleName("vpinfomp");
		vpinfoplay.add(new HTML("&nbsp;&nbsp;<b><font face=\"Ubuntu\" size=\"4\">Song:&nbsp;</font></b><font face=\"WDS\" size=\"5\">"+name+"</font>"));
		vpinfoplay.add(new HTML("&nbsp;&nbsp;<b><font face=\"Ubuntu\" size=\"4\">Artist:&nbsp;</font></b><font face=\"WDS\" size=\"5\">"+artist+"</font>"));
		vpinfoplay.add(new HTML("&nbsp;&nbsp;<b><font face=\"Ubuntu\" size=\"4\">Album:&nbsp;</font></b><font face=\"WDS\" size=\"5\">"+album+"</font>"));
		cover.clear();
		Image cimg = new Image(img);
		cimg.setStyleName("vpimgmp");
		cover.add(cimg);
		sid =id+ida;
		loadSM2(songstream,sid);
	}
	
	native static void destroySong(String id)/*-{
	$wnd.soundManager.stop(id);
    $wnd.soundManager.destroySound(id);
}-*/;
	
	native static void loadSM2(String path, String id)/*-{
        var duration=0;
        var timeo =0;
		var s = $wnd.soundManager.createSound({
		id: id,
		url: path,
		onplay: function() {
 		@co.cc.flobii.ui.mpwidget.MpWidg::onplayChange()();
		},
		whileplaying: function() {
			var sound = $wnd.soundManager.getSoundById(id);
			var n=0;
			if(sound.loaded){
				if(n==0){
		@co.cc.flobii.ui.mpwidget.MpWidg::showDuration(Ljava/lang/String;)(""+sound.duration);		
				}n=1;
			}else{
		@co.cc.flobii.ui.mpwidget.MpWidg::showDuration(Ljava/lang/String;)("180000");		
			}
 		@co.cc.flobii.ui.mpwidget.MpWidg::showTime(Ljava/lang/String;)(""+sound.position);
		},
		onfinish: function() {
 		@co.cc.flobii.ui.mpwidget.MpWidg::loadSong()();
		}
		});
		$wnd.soundManager.play(id);
	}-*/;
	
	//-------------------------------------Test-----------------------------------------------
	
	public native static void createSoundTest(String id,String path)/*-{
	var timeo =0;	
	var s = $wnd.soundManager.createSound({
		id: id,
		url: path,
		loops: 10,
		whileplaying: function() {
 		if(timeo==4){
 		@co.cc.flobii.ui.client.Flobii::clearRect()();
 		timeo=0;
 		}
 		@co.cc.flobii.ui.client.Flobii::drawFFTSpecTest(Ljava/lang/String;)(id);
 		timeo++;
		}});
}-*/; 
	
	public native static void playsTest(String id)/*-{
	$wnd.soundManager.play(id);
}-*/; 

	public native static void stopsTest(String id)/*-{
	$wnd.soundManager.pause(id);
}-*/;
	
	
	
	
	public static void onplayChange(){
		if(wplayer.isActive()){
			play_pause.setImage(new Image("wicon/pause.png"));
			pp=1;
		}
	}
	
	
	native static void pause(String id)/*-{
	$wnd.soundManager.pause(id);
}-*/;
	
	native static void play(String id)/*-{
	$wnd.soundManager.resume(id);
}-*/;
	
	native static void setSongPosition(String id,int time)/*-{
	$wnd.soundManager.getSoundById(id).setPosition(time);
}-*/;
	
	native static void setJBIOff()/*-{
	$wnd.$('.jamendo_button').children('img').attr('src' , "token/mediaplayer.png");
}-*/;

	public static void initRadioChoose(){
		popup.setSize("920px","130px");
		popup.addCloseHandler(new CloseHandler<PopupPanel>(){

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if(!wplayer.isActive()){
				close_open =0;
				setJBIOff();
				}
			}
			
		});
		//popup.setStyleName("dbox");
		popup.setAnimationEnabled(true);
	    popup.setAutoHideEnabled(true);
	    ImageButton pop = new ImageButton(new Image("jamendo/pop.gif"));
        HorizontalPanel hp = new HorizontalPanel();
        rock.setTitle("Rock!");
        hiphop.setTitle("Hip Hop!");
        dance.setTitle("Dance!");
        jazz.setTitle("Jazz!");
        lounge.setTitle("Lounge!");
        metal.setTitle("Metal!");
        pop.setTitle("Pop!");
		 rock.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=rock");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        hiphop.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=hiphop");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        dance.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=dance");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        jazz.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=jazz");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        lounge.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=lounge");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        metal.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=metal");
					onRadioChooseClicked();
					popup.hide();
				}
			});
	        pop.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {
					JSONRetrieve.getJSON_String("http://api.jamendo.com/get2/id+name+url+stream+album_name+album_url+album_id+artist_id+artist_name/track/json/track_album+album_artist/?n=100&order=ratingmonth_desc&tag_idstr=pop");
					onRadioChooseClicked();
					popup.hide();
				}
			});  
	        hp.add(rock);
	        hp.add(hiphop);
	        hp.add(dance);
	        hp.add(jazz);
	        hp.add(lounge);
	        hp.add(metal);
	        hp.add(pop);
	        popup.add(hp);
	}
	
	private static void onRadioChooseClicked(){
		if(!sid.equals("")){
		destroySong(sid);
		sid="";
		}
	}
	
	 	
 	static int duration=0;
 	static int time=0;
 	
 	private static void showDuration(String dti){
 		double dt = Integer.parseInt(dti)/1000;
 		m_slider.setMaximum((int)(dt*100));
 		m_slider.setMinimum(0);
 		duration =(int)dt;
 		vtime.clear();
 		String dursec="";
 		if(duration%60>9){
 			dursec=""+(duration%60);
 		}else{
 			dursec="0"+(duration%60);
 		}
 		vtime.add(new HTML("&nbsp;&nbsp;<b><font face=\"WDS\" size=\"3\">0:00/"+(duration/60)+":"+dursec+"</font></b>&nbsp;&nbsp;"));}

 	private static void showTime(String cti){
 		double ct = Integer.parseInt(cti)/1000;
 		vtime.clear();
 		m_slider.setValue((int)(ct*100));
 		time =(int)ct;
 		String timesec="";
 		if(time%60>9){
 			timesec=""+(time%60);
 		}else{
 			timesec="0"+(time%60);
 		}
 		String dursec="";
 		if(duration%60>9){
 			dursec=""+(duration%60);
 		}else{
 			dursec="0"+(duration%60);
 		}
 		vtime.add(new HTML("&nbsp;&nbsp;<b><font face=\"WDS\" size=\"3\">"+(time/60)+":"+timesec+"/"+(duration/60)+":"+dursec+"</font></b>&nbsp;&nbsp;"));
 	}
 	
 	

	private static void createFixedWindowPanel() {
		wplayer = new WindowPanel("Jamendo Radios");
		wplayer.setResizable(false);
		wplayer.setAnimationEnabled(true);
		wplayer.setWidget(vpplayer);
		wplayer.addStyleName("dbox");
		wplayer.setPopupPosition(10,Window.getClientHeight()/2-120);
	    addOptionUI();
		wplayer.setSize("560px", "220px");
		wplayer.layout();
		wplayer.setHideContentOnMove(false);
	    wplayer.show();
	}	   
	private static void addOptionUI(){
    	next.addClickHandler(new ClickHandler() {		
			@Override
			public void onClick(ClickEvent event) {
					loadSong();
			}
		});
    	prev.addClickHandler(new ClickHandler() {		
			@Override
			public void onClick(ClickEvent event) {
				if(count<98){
				count=count+2;
				loadSong();
				}
			}
		});
    	radios.addClickHandler(new ClickHandler() {		
			@Override
			public void onClick(ClickEvent event) {
				popup.center();
			}
		});
    	play_pause.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(pp==0){
				 play(sid);
				play_pause.setImage(sm_pause);
				pp=1;
				}else{
				 pause(sid);
				play_pause.setImage(sm_play);	
				pp=0;
				}
				
			}
		});
    	
    	jamendo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open("http://jamendo.com", "Jamendo", "");
			}
		});
    	
    	downloadtrack.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(downtrack, "Download Track!", "");
			}
		});
    	
    	downloadalbum.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(downalbum, "Download Album!", "");
			}
		});
    	
    	wplayer.getHeader().clear();
    	wplayer.getHeader().add(radios, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(new HTML("&nbsp;&nbsp;"), CaptionRegion.RIGHT);
    	wplayer.getHeader().add(next, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(play_pause, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(prev, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"), CaptionRegion.RIGHT);
    	wplayer.getHeader().add(downloadtrack, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font face=\"Verdana\">Track</font></b>"), CaptionRegion.RIGHT);
    	wplayer.getHeader().add(downloadalbum, CaptionRegion.RIGHT);
    	wplayer.getHeader().add(new HTML("<b><font face=\"Verdana\">Album</font></b>"), CaptionRegion.RIGHT);
    	wplayer.getHeader().add(jamendo,CaptionRegion.LEFT); 	
	}

	    private static Slider m_slider = new Slider("slider");

	    public static VerticalPanel loadSlider()
	    {
	    	VerticalPanel vp = new VerticalPanel();
	    	vp.setSize("460px", "20px");
	        vp.add(m_slider);
	        m_slider.addListener(new SliderListener() {
				@Override
				public void onStop(SliderEvent e) {}
				@Override
				public void onStart(SliderEvent e) {}
				@Override
				public void onChange(SliderEvent e) {}
				@Override
				public boolean onSlide(SliderEvent e) {
					int t = m_slider.getValue()*10;
					showTime(""+t);
					setSongPosition(sid,t);
					return true;
				}
			});    
	        return vp;
	    }
}