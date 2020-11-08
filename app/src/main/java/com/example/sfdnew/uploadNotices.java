package com.example.sfdnew;

import com.google.firebase.database.Exclude;

public class uploadNotices {
    public String name,url;
    public String mKey;

    public uploadNotices() {
    }

    public uploadNotices(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getUrl() {
        return url;
    }

//    public void setUrl(String url) {
//        this.url = url;
//    }
    @Exclude
   public String getKey(){
    return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }

}