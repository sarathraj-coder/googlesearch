package com.sarathraj.google.search;

import java.util.List;

public class SearchResult {

       private String kind;
      
	   private List<Item> items;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
}


