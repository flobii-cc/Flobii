package co.cc.flobii.ui.googlewidget;


import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GooWidg {
	final static TextBox goosearchfield = new TextBox();
	final static CheckBox taborframe = new CheckBox("Open search results in a new Tab");
	final static RadioButton web = new RadioButton("Goo","Web");
	final static RadioButton img = new RadioButton("Goo","Image");
	final static RadioButton map = new RadioButton("Goo","Map");
	final static RadioButton tube = new RadioButton("Goo","Youtube");
	final static RadioButton wiki = new RadioButton("Goo","Wikipedia");
	static WindowPanel wp = new WindowPanel("Search Widget");
  	static VerticalPanel vpgooglefield = new VerticalPanel();
  	static WindowPanel frame = new WindowPanel("Searchbox");
  	final static Image[] searchimg = new Image[5];
  	final static LayoutPanel layoutPanel = new LayoutPanel();
	final static FillLayoutData searcheader = new FillLayoutData(false);
	final static FlowPanel sh = new  FlowPanel();
	static int start=1;
	static int open_close =0;
	
	
	public static void onSearchClick(){
				if(!(wp.isShowing()||wp.isCollapsed())){
					open_close=1;
			        wp.addStyleName ("dbox");
			        wp.setHideContentOnMove(false);
			        goosearchfield.setStyleName("textboxgoosearch");
			        goosearchfield.setTextAlignment(TextBox.ALIGN_CENTER);
					goosearchfield.addKeyUpHandler(new KeyUpHandler() {
						public void onKeyUp(KeyUpEvent event) {
							if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
								searchonGoogle(goosearchfield.getText());
								goosearchfield.setText("");
							}	
						}
					});
					createFixedWindowPanel();
				}else{
					if(wp.isVisible()){
						open_close=0;
						wp.setVisible(false);
					}else{
						open_close=1;
						wp.setVisible(true);
						wp.toFront();
					}
				}
	}
	
	private static VerticalPanel loadGooPanel(){
		searchimg[0] = new Image("searcheader/1.png");
    	searchimg[1] = new Image("searcheader/2.png");
    	searchimg[2] = new Image("searcheader/3.png");
    	searchimg[3] = new Image("searcheader/4.png");
    	searchimg[4] = new Image("searcheader/5.png");
    	taborframe.setValue(true);
		VerticalPanel vpsearch = new VerticalPanel();
		HorizontalPanel checkbox = new HorizontalPanel();
		layoutPanel.setAnimationEnabled(true);
    	layoutPanel.setSize("400px","64px");
    	layoutPanel.add(sh, searcheader);
    	vpsearch.setSpacing(4);
    	vpsearch.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    	vpsearch.add(layoutPanel);
		checkbox.add(web);
		checkbox.add(img);
		checkbox.add(map);
		checkbox.add(tube);
		checkbox.add(wiki);
		vpsearch.add(checkbox);
		vpsearch.add(goosearchfield);
		goosearchfield.setSize("380px", "32px");
		vpsearch.add(taborframe);
    	if(start==1){
    	web.setValue(true);
        start=0;
		sh.clear();
		sh.add(searchimg[0]);
		web.addClickHandler(new ClickHandler() {
  	      public void onClick(ClickEvent event) {
  	    	slidePic(0); 	        
  	      }
  	    });
		img.addClickHandler(new ClickHandler() {
	  	      public void onClick(ClickEvent event) {
	  	       slidePic(1);
	  	      }
	  	    });
		tube.addClickHandler(new ClickHandler() {
	  	      public void onClick(ClickEvent event) {
	  	      slidePic(2);
	  	   
	  	      }
	  	    });
		map.addClickHandler(new ClickHandler() {
	  	      public void onClick(ClickEvent event) {
	  	    	slidePic(3);
	  	       
	  	      }
	  	    });
		wiki.addClickHandler(new ClickHandler() {
	  	      public void onClick(ClickEvent event) {
	  	      slidePic(4);
	  	      }
	  	    });
    	}
    	vpsearch.addStyleName("vpgoo");
		return vpsearch;
    }

	
	private static void slidePic(int ty){
		      sh.clear();
	    	  sh.add(searchimg[ty]);
	    	  searcheader.setHorizontalAlignment(halign[(int) Math.round(Math.random() * 3)]);
	    	  searcheader.setVerticalAlignment(valign[(int) Math.round(Math.random() * 3)]);
	          layoutPanel.invalidate();
	          layoutPanel.layout();
	          searcheader.setHorizontalAlignment(halign[(int) Math.round(Math.random() * 3)]);
	    	  searcheader.setVerticalAlignment(valign[(int) Math.round(Math.random() * 3)]);
	          layoutPanel.invalidate();
	          layoutPanel.layout();

	}
	
	 private static HorizontalAlignmentConstant[] halign = {
		      null, HasHorizontalAlignment.ALIGN_LEFT,
		      HasHorizontalAlignment.ALIGN_CENTER, HasHorizontalAlignment.ALIGN_RIGHT};

		  /**
		   * Vertical alignment values.
		   */
		  private static VerticalAlignmentConstant[] valign = {
		      null, HasVerticalAlignment.ALIGN_TOP, HasVerticalAlignment.ALIGN_MIDDLE,
		      HasVerticalAlignment.ALIGN_BOTTOM};
		  
		  
    private static void searchonGoogle(String keywords){
    	String adress = "http://www.google.com/";
    	if(web.getValue()){
    		adress = adress+"search?q=";	
    	}else if(img.getValue()){
    		adress = adress+"images?q=";
    	}else if(map.getValue()){
    		adress = adress+"maps?q=";
    	}else if(tube.getValue()){
    		adress ="http://www.youtube.com/results?search_query=";
    	}else if(wiki.getValue()){
    		adress ="http://wikipedia.org/wiki/";
    	}
    	if(taborframe.getValue()){
    		Window.open(adress+keywords,"Search", "");
    	}else{
    		startSearchResultFrame(adress+keywords);
    	}
    }
  

    private static void startSearchResultFrame(String s_adress){
    	vpgooglefield.clear();
    	int ww = Window.getClientWidth();
    	int wh = Window.getClientHeight();
    	int h = wh-80;
    	int w = ww*90/100;
    	int l = ww/2- w/2;
    	int t = 60;
    	if(!(frame.isCollapsed()||frame.isShowing())){
    		createBasicWindowPanel(l,t,w, h, vpgooglefield,frame);
    	}else{
    		frame.setPopupPosition(l, t);
            frame.setSize(w+"px", h+"px");
            frame.setCollapsed(false);
    	}	
    	vpgooglefield.add(new HTML("<iframe src =\""+s_adress+"\" width=\""+w+"\" height=\""+h+"\"></iframe>"));
    }
    
    private static void createFixedWindowPanel() {
	    wp.setResizable(false);
	    wp.setAnimationEnabled(true);
	    final LayoutPanel w = new LayoutPanel();
	    w.add(loadGooPanel());
	    wp.setWidget(w);
	    wp.setSize("426px", "195px");
        wp.getHeader().clear();
	    wp.center();
	      }
    
    private static void createBasicWindowPanel(int l,int t, int w, int h, VerticalPanel vp,WindowPanel frame) {
        frame.setAnimationEnabled(true);
        frame.setHideContentOnMove(false);
        frame.setPopupPosition(l, t);
        frame.setSize(w+"px", h+"px");
        frame.setWidget(vp);
        frame.show();
      }
    
    
    
}
