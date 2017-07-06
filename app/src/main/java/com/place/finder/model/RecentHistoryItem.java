package com.place.finder.model;

import io.realm.RealmObject;

/**
 * Created by rohit.anvekar on 5/21/2017.
 */

public class RecentHistoryItem extends RealmObject{
    public String itemName;

    public RecentHistoryItem(){

    }

    // constructor for adding sample data
    public RecentHistoryItem(String itemName){

        this.itemName = itemName;
    }
}
