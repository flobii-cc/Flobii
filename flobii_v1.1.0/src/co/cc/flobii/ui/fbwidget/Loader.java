package co.cc.flobii.ui.fbwidget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;


public class Loader {
	final VerticalPanel fbloader = new VerticalPanel();
	
	
	public void addLoader(VerticalPanel vp){
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		Image img = new Image("fbicons/loader.gif");
		fbloader.add(img);
		vp.add(fbloader);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);	
	}
	
	public void removeLoader(){
		fbloader.clear();
	}
	
}
