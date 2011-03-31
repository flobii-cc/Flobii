package co.cc.flobii.ui.info;


import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import co.cc.flobii.ui.mpwidget.MpWidg;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoWidg {

	static WindowPanel wp = new  WindowPanel("Info");
	public static String version = "1.1.0";
	public static String email ="whats.up.flobii@gmail.com";
	public String gwtversion = "2.1.1";
	public String eclipsesdk = "3.6.0";
	public String appengine ="1.4.0";
	public static Anchor play_pause = new Anchor("Start brief Test");
	public static int ps =0;
	public static int close_open =0;
	
	public static void onInfoClick(){
		if(!wp.isShowing()){
			createFixedWindowPanel();
			close_open=1;
			wp.setHideContentOnMove(false);
	}else{
		if(wp.isVisible()){
			close_open=0;
		   wp.setVisible(false);
		}else{
			close_open=1;
			wp.setVisible(true);
			wp.toFront();
		}
	}
	}
	
	private static VerticalPanel loadInfoPanel(){
		MpWidg.createSoundTest("fft", "song.mp3");
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(new HTML("<font face=\"Bohemica\" size=\"6\">Flobii </font><font face=\"Bohemica\" size=\"4\"> A GWT Based Internet Project</font>"));
		vp.add(new HTML("<p/>"));
		vp.add(new HTML("<font face=\"Ubuntu\" size=\"4\">Project</font>"));
		vp.add(new HTML("Version: "+version));
		vp.add(new HTML("<p/>"));
		vp.add(new HTML("<font face=\"Ubuntu\" size=\"4\">Contact</font>"));
		vp.add(new HTML("Follow me on <a target=\"_blank\" href=\"http://twitter.com/kevko14\">Twitter</a>"));
		vp.add(new HTML(email));
		vp.add(new HTML("<p/>"));
		vp.add(new HTML("<font face=\"Ubuntu\" size=\"4\">Creative Link</font>"));
		vp.add(new HTML("<a target=\"_blank\" href=\"http://martinvanrovan.at.tf/\">martinvanrovan.at.tf</a>"));
		vp.add(new HTML("<p/>"));
		vp.add(new HTML("<font face=\"Ubuntu\" size=\"4\">in development</font>"));
		HorizontalPanel hz = new HorizontalPanel();
		hz.add(new HTML("FFT-Spectrum&nbsp;"));
		hz.add(play_pause);
		vp.add(hz);
		vp.add(new HTML("<p/>"));
		play_pause.setStyleName("anchorinfo");
		play_pause.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(ps==0){
				MpWidg.playsTest("fft");
				play_pause.setText("Stop brief Test");
				ps=1;
				}else{
					MpWidg.stopsTest("fft");	
					play_pause.setText("Start brief Test");
				ps=0;
				}
			}
		});
		return vp;  
    }
	

	private static void createFixedWindowPanel() {
	    wp.setResizable(false);
	    wp.setAnimationEnabled(true);
	    final LayoutPanel w = new LayoutPanel();
	    w.add(loadInfoPanel());
	    wp.setWidget(w);
	    wp.setSize("360px", "300px");
	    wp.setPopupPosition(Window.getClientWidth()-380-100,Window.getClientHeight()/2-150);
	    wp.getHeader().clear();
	    wp.addStyleName("dbox");
	    wp.show();
	      }

}
