package co.cc.flobii.ui.server;

import java.util.List;

import javax.persistence.Id;

public class LinkInfo {
     @Id String username;
     List<String> linklist;
     List<String> rating;
     List<String> foldername;
     List<String> title;
     int foldercount;
}
