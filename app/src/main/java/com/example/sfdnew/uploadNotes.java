package com.example.sfdnew;

import com.google.firebase.database.Exclude;

public class uploadNotes {
    public String name,url;
    public String mKey;

    public uploadNotes() {
    }

    public uploadNotes(String name, String url) {
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

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }
}
