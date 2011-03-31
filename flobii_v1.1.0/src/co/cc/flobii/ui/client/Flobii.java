package co.cc.flobii.ui.client;

import org.gwt.mosaic.ui.client.ImageButton;

import co.cc.flobii.ui.mpwidget.MpWidg;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;



public class Flobii implements EntryPoint {
	static HorizontalPanel hzhead = new HorizontalPanel();
	static HorizontalPanel hzdock = new HorizontalPanel();
	static VerticalPanel vphead = new VerticalPanel();
	//--------Cloud Animation---------------------------
	static int[] cloudsdir = new int[20];
	static int[] cloudimg = new int[20];
	static int timeline =0;
	static int timeout =0;
	static int[] cx = new int[20];
	static int[] cy = new int[20];
	static int cnumber =0;
	//--------Dimension-----------------------------------
	static int cwidth = 0;
	static int caniheight =0;
	public static int citaniheight =0;
	
	
	native static void createCanvas(int w, int h)/*-{
		var ani = $doc.getElementById('animation');
		var canvas = document.createElement('canvas');
		canvas.setAttribute('width', w);
		canvas.setAttribute('height',h);
		canvas.setAttribute('id', 'cloud');
		ani.appendChild(canvas);
	}-*/;
	
	native static void createCanvasBg()/*-{
	var ani = $doc.getElementById('cit');
	var ca = document.createElement('canvas');
	ca.setAttribute('width', 4000);
	ca.setAttribute('height',4000);
	ca.setAttribute('id', 'citybg');
	ani.appendChild(ca);
}-*/;
	
//-----------------------------------------ModuleLoad----------------------------------------------------------
	public void onModuleLoad() {
      calcDimensions();
      changeDimensions(cwidth, Window.getClientHeight());
      if(checkforCanvasSupport()){
    	  createCanvas(cwidth,caniheight);
    	  createCanvasBg(); //cwidth,citaniheight
    	  setCloud();
    	  drawBg();
	  }else{
		  showHTML5Browser();
	  }
      loadDockMenu(); 
      setUPdTime();
	}
	
	
	
	public static void calcDimensions(){
			cwidth = Window.getClientWidth();
			caniheight = 100;
			citaniheight = Window.getClientHeight();
	}

	
	
	@SuppressWarnings("deprecation")
	public void addScript(String url) { 
	    Element e = DOM.createElement("script"); 
	    DOM.setAttribute(e, "language", "JavaScript"); 
	    DOM.setAttribute(e, "src", url); 
	    DOM.appendChild(RootPanel.get().getElement(), e); 
	  } 

	
	public void loadDockMenu(){
		vphead.setWidth(Window.getClientWidth()+"px");
		vphead.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hzdock.add(new HTML("<a class=\"search_button\"><img src=\"token/search.png\"/></a>"));
		hzdock.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		hzdock.add(new HTML("<a class=\"linksaver_button\"><img src=\"token/linksaver.png\"/></a>"));
		hzdock.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		hzdock.add(new HTML("<a class=\"jamendo_button\"><img src=\"token/mediaplayer.png\"/></a>"));
		hzdock.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		hzdock.add(new HTML("<a class=\"info_button\"><img src=\"token/info.png\"/></a>"));
		hzdock.setHeight(Window.getClientHeight()*1/6+32+"px");
		vphead.add(hzdock);
		RootPanel.get("dock").clear();
		RootPanel.get("dock").add(vphead);
		createButton();
		MpWidg.loadMpWidg();
	}	
	
	static void showHTML5Browser(){
		PopupPanel shbrow = new PopupPanel();
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hz = new HorizontalPanel();
		vp.add(new HTML("&nbsp;&nbsp;&nbsp;<font face=\"Verdana\" size=\"6\">Your browser doesn't support HTML5!</font>&nbsp;&nbsp;&nbsp;"));
		vp.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face=\"Verdana\" size=\"4\">Please use one of these...</font>"));
		final ImageButton chrome = new ImageButton(new Image("browsers/chrome_gray.png"));
		hz.add(chrome);
		chrome.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				chrome.setImage(new Image("browsers/chrome_color.png"));
				Window.open("http://www.google.com/chrome/", "Chrome", "");
			}
		});
		
		final ImageButton opera = new ImageButton(new Image("browsers/opera_gray.png"));
		hz.add(opera);
		opera.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				opera.setImage(new Image("browsers/opera_color.png"));
				Window.open("http://www.opera.com/download/", "Opera", "");
			}
		});
		
		final ImageButton firefox = new ImageButton(new Image("browsers/firefox_gray.png"));
		hz.add(firefox);
		firefox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				firefox.setImage(new Image("browsers/firefox_color.png"));
				Window.open("http://www.mozilla-europe.org/de/firefox/", "Firefox", "");
			}
		});
		final ImageButton safari = new ImageButton(new Image("browsers/safari_gray.png"));
		hz.add(safari);
		safari.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				safari.setImage(new Image("browsers/safari_color.png"));
				Window.open("http://www.apple.com/safari/", "Safari", "");
			}
		});
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		hz.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
		vp.add(hz);
		shbrow.setWidget(vp);
		shbrow.setAutoHideEnabled(true);
		shbrow.center();
	}
	
	native static boolean checkforCanvasSupport()/*-{
	try {
        	canvas_compatible = !!(document.createElement('canvas').getContext('2d')); // S60
        } catch(e) {
        	canvas_compatible = !!(document.createElement('canvas').getContext); // IE
		} 
		return canvas_compatible;
	}-*/;	

	native static void changeDimensions(int w,int h)/*-{  
		e=$doc.getElementById("animation");
		e.style.height = h + 'px';
		e.style.width = w + 'px';
		}-*/;
	
	
	native static void setUPdTime()/*-{
		setInterval(@co.cc.flobii.ui.client.Flobii::update(),50);
		}-*/;
	
	native static void drawImages(int x,int y,int cl)/*-{
		   var cloud = $doc.getElementById('cloud').getContext("2d");
           var img = new Image();
           img.src ='/cloud/cloud_'+cl+'.png';
		   cloud.drawImage(img,x,y);
	}-*/;
	
	
	
	native static void changeCanvasSize(int w,int h)/*-{
	  var canvas = $doc.getElementById('cloud');
	canvas.setAttribute('width', ''+w); 
	canvas.setAttribute('height', ''+h);
}-*/;

	native static void clear(int w,int h)/*-{
	  var cloud = $doc.getElementById('cloud').getContext("2d");
	  cloud.clearRect(0, 0, w,h);
}-*/;
	
	native static void createButton()/*-{
				
	//----------------------Jamendo-Radio-Player--------------------------------------------------
	//--------------------------------------------------------------------------------------------
	$wnd.$('.jamendo_button').click(function() {
  					@co.cc.flobii.ui.mpwidget.MpWidg::onJamendoClick()();
				});
				$wnd.$('.jamendo_button').mouseover (function() {
					$wnd.$('.jamendo_button').children('img').attr('src' , "token/mediaplayer_hover.png");
				});
				$wnd.$('.jamendo_button').mouseout(function() {
					if(@co.cc.flobii.ui.mpwidget.MpWidg::close_open==0){
					$wnd.$('.jamendo_button').children('img').attr('src' , "token/mediaplayer.png");
					}
				});
				
	//----------------------Info-Panel------------------------------------------------------------
	//--------------------------------------------------------------------------------------------
	$wnd.$('.info_button').click(function() {
  					@co.cc.flobii.ui.info.InfoWidg::onInfoClick()();
				});
				$wnd.$('.info_button').mouseover (function() {
					$wnd.$('.info_button').children('img').attr('src' , "token/info_hover.png");
				});
				$wnd.$('.info_button').mouseout(function() {
					if(@co.cc.flobii.ui.info.InfoWidg::close_open==0){
						$wnd.$('.info_button').children('img').attr('src' , "token/info.png");
					}
				});
	//----------------------Search-Panel--------------------------------------------------
	//--------------------------------------------------------------------------------------------
	$wnd.$('.search_button').click(function() {
  					@co.cc.flobii.ui.googlewidget.GooWidg::onSearchClick()();
				});
				$wnd.$('.search_button').mouseover (function() {
					$wnd.$('.search_button').children('img').attr('src' , "token/search_hover.png");
				});
				$wnd.$('.search_button').mouseout(function() {
					if(@co.cc.flobii.ui.googlewidget.GooWidg::open_close==0){
					$wnd.$('.search_button').children('img').attr('src' , "token/search.png");
					}
				});
					
	//----------------------Linksaver--------------------------------------------------
	//--------------------------------------------------------------------------------------------
	$wnd.$('.linksaver_button').click(function() {
  					@co.cc.flobii.ui.linksaver.Linksaver::loadLinkSaver()();
				});
				$wnd.$('.linksaver_button').mouseover (function() {
					$wnd.$('.linksaver_button').children('img').attr('src' , "token/linksaver_hover.png");
				});
				$wnd.$('.linksaver_button').mouseout(function() {
					if(@co.cc.flobii.ui.linksaver.Linksaver::close_open==0){
					$wnd.$('.linksaver_button').children('img').attr('src' , "token/linksaver.png");
					}
				});						
}-*/;
	
	native static void cclear()/*-{
	  var cit = $doc.getElementById('citybg').getContext("2d");
	  cit.clearRect(0, 0, 4000,4000);
}-*/;
	
	static void update(){
		if(Window.getClientHeight()>citaniheight||Window.getClientWidth()>cwidth){
			cclear();
			calcDimensions();
			drawBg();

		}else{
			calcDimensions();	
		}
		hzdock.setHeight(Window.getClientHeight()*1/6+32+"px");
		changeDimensions(cwidth,Window.getClientHeight());
		vphead.setWidth(cwidth+"px");
		hzhead.setHeight(Window.getClientHeight()+"px");
		changeCanvasSize(cwidth,caniheight);
		//-----------Animation-----------------------------
		timeline++;
		if(timeline==timeout){
			setCloud();
			timeline=0;
		}
		clear(cwidth,caniheight);
		drawClouds();
	}
	
	static void drawClouds(){
		for(int i=0;i<20;i++){
			if(cy[i]!=0){
			drawImages(cx[i],cy[i],cloudimg[i]);
			if(cloudsdir[i]==0){
		     	if(cx[i]<(cwidth+10)){
		       cx[i]=cx[i]+1;
		      } 
		      }else{
		      	if(cx[i]>-160){
		      	cx[i]=cx[i]-1; 
		      	}	
		      	}
		}
		}
	}
	
	static void setCloud(){
		timeout = (int)(Math.random()*200)+100;
		if(cnumber==20){
			cnumber=0;
		}
		int dir = (int)(Math.random()*2);
		cloudsdir[cnumber] = dir;
		if(dir==0){
			cx[cnumber] =-150;
		}else{
			cx[cnumber] =cwidth;
		}
		cloudimg[cnumber] = (int)(Math.random()*1);
		cy[cnumber] =(int)(Math.random()*65)+1;
		cnumber++;
	}
	
	native static void drawBg()/*-{
	var ccitybg = $doc.getElementById('citybg').getContext("2d");
	var iw = new Array();
	iw[0] =0;
	var imgw = new Array();
	imgw[0]=40;	//56
	imgw[1]=40;	//56
	imgw[2]=32;	//44
	imgw[3]=45;	//62
	imgw[4]=30;	//42
	imgw[5]=48;	//66
	imgw[6]=94; //130
	var hwd = @co.cc.flobii.ui.client.Flobii::citaniheight*66/100;
	var n =0;
	while(@co.cc.flobii.ui.client.Flobii::cwidth>iw[n]){
		var mr = Math.floor(Math.random()*7);
		var img = new Image();
		img.src = '/citybg/stone_'+mr+'_.png';
		iw[n+1] = iw[n]+imgw[mr];
		img.title = ''+n;
    	img.onload = function(){
			ccitybg.drawImage(this ,iw[parseInt(this.title)],@co.cc.flobii.ui.client.Flobii::citaniheight-18);
			this.title = '';
 		}n++;}
 		
 		ccitybg.fillStyle = "rgb(255,255,255)";
        ccitybg.fillRect(0,hwd,@co.cc.flobii.ui.client.Flobii::cwidth,hwd/2);	
 	
 		
 	//Towercount
 	var tower = new Array();
 	var twb = new Array();
 	var fc =0;
 	twb[0]=15;
 	while(twb[fc]<@co.cc.flobii.ui.client.Flobii::cwidth){
 		var wmr = Math.floor(Math.random()*4)+5;
 		if(fc!=0){
 		while(wmr==tower[fc-1]){
 			wmr = Math.floor(Math.random()*4)+5;
 		}
 		}
 		tower[fc] = wmr;
 		twb[fc+1] = twb[fc]+wmr*25 +30;
 		fc++;
 	}
 	
 	//TowerHeight
 	var towerh = new Array();
 	var cth = hwd*33/100;
 	var fch =0; while(fch<tower.length){
 		var hmr = Math.floor(Math.random()*((hwd-cth-40)/30)+(cth)/30);
 		towerh[fch] = hmr;
 		fch++;}
 	
 	//TowerD
 	var towerd = new Array();
 	var fd =0; while(fd<tower.length){
 		var dmr = Math.floor(Math.random()*6);
 		towerd[fd] = dmr;
 		fd++;}
 	
 	
 	var tc =0; while(tc<tower.length){
 		ccitybg.fillStyle = "rgb(255,255,255)";
 		ccitybg.strokeStyle = '#999';
		ccitybg.lineWidth   = 2;
        ccitybg.fillRect(twb[tc]-15,hwd-towerh[tc]*30-10, 25*tower[tc]+30,towerh[tc]*30+10);
        ccitybg.strokeRect(twb[tc]-15,hwd-towerh[tc]*30-10, 25*tower[tc]+30,towerh[tc]*30+10+5);
        ccitybg.strokeStyle = '#999';
		ccitybg.lineWidth   = 1;
        if(towerd[tc]==2){
        var dimg = new Image();
		dimg.src = '/citybg/house.png';
		dimg.title =''+tc;
    	dimg.onload = function(){
			ccitybg.drawImage(this ,twb[parseInt(this.title)],hwd-towerh[parseInt(this.title)]*30-30);
			this.title = '';
 		}
        }
        if(towerd[tc]==3){
        	var dimg = new Image();
        	dimg.src = '/citybg/pd.png';
			dimg.title =''+tc;
    		dimg.onload = function(){
			ccitybg.drawImage(this ,twb[parseInt(this.title)]+30,hwd-towerh[parseInt(this.title)]*30-50);
			this.title = '';
        }
        }
	var s =0;  while(s<towerh[tc]){
	var m =0;  while(m<tower[tc]){
		//var mr = Math.floor(Math.random()*6);
		//var img = new Image();
		//img.src = '/citybg/w_'+mr+'.png';
		//img.title = m+','+s+','+tc;
    	//img.onload = function(){
		//	ccitybg.drawImage(this ,twb[this.title.split(',')[2]]+parseInt(this.title.split(',')[0])*25,hwd-30-parseInt(this.title.split(',')[1])*30);
		//	this.title = '';
 		//}
 		ccitybg.strokeRect(twb[tc]+m*25,hwd-30-s*30,25,25);
 		m++;}s++;}tc++;}
 		
 		
 		var wdimg = new Image();
 		wdimg.src = '/citybg/w_d.png';
    	wdimg.onload = function(){
    		var wiwd =0;
 			while(wiwd<@co.cc.flobii.ui.client.Flobii::cwidth){
				ccitybg.drawImage(this ,wiwd,hwd);
			 	wiwd = wiwd+70;
 			}	
 		}
}-*/;

	
	public native static void clearRect()/*-{
	var ccitybg = $doc.getElementById('citybg').getContext("2d");
	ccitybg.fillStyle = "rgb(255,255,255)";
	var hwd = @co.cc.flobii.ui.client.Flobii::citaniheight*66/100;
    ccitybg.fillRect(0,hwd+20,@co.cc.flobii.ui.client.Flobii::cwidth,hwd/2-38);	
}-*/;
 		
	public native static void drawFFTSpecTest(String id)/*-{
	var w = @co.cc.flobii.ui.client.Flobii::cwidth;
	var h = @co.cc.flobii.ui.client.Flobii::citaniheight/4;
	var bit =128;
	var sound = $wnd.soundManager.getSoundById(id);
	w=(w-(bit+1)*2)/bit;
	for(var i=0;i<bit;i++){
		var eq = sound.eqData.left[i*(256/bit)]*(h-38);
        @co.cc.flobii.ui.client.Flobii::drawAmp(DDD)(w,eq,2+(w+2)*i);
	}
}-*/; 
	
	
	native static void drawAmp(double w, double h, double x)/*-{
	var ccitybg = $doc.getElementById('citybg').getContext("2d");
	h=h/8;
	for(var i =0;i<8;i++){
		if(i==0){
		ccitybg.fillStyle ="rgba(123,146,50,0.8)";
		}else if(i==1){
			ccitybg.fillStyle = "rgba(186,178,102,0.8)";
		}else if(i==2){
			ccitybg.fillStyle = "rgba(171,86,70,0.8)";
		}else if(i==3){
			ccitybg.fillStyle = "rgba(76,139,164,0.8)";
		}else if(i==4){
			ccitybg.fillStyle = "rgba(76,123,146,0.8)";
		}else if(i==5){
			ccitybg.fillStyle = "rgba(174,86,112,0.8)";
		}else if(i==6){
			ccitybg.fillStyle = "rgba(112,79,94,0.8)";
		}else{
			ccitybg.fillStyle = "rgba(166,122,133,0.8)";
		}
	    ccitybg.fillRect(x,@co.cc.flobii.ui.client.Flobii::citaniheight-18-h*(1+i),w,h);
	}
     }-*/;
	
}