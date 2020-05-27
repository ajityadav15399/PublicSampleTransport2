package com.orka.publicsampletransport;

public class ExampleItem {
    private String mImageResource;
    public String mText;
    public String mText2;
    public ExampleItem(String img,String text1,String text2){
        mImageResource=img;
        mText=text1;
        mText2=text2;
    }

    public String getmImageResource(){
        return mImageResource;
    }

    public String getmText() {
        return mText;
    }

    public String getmText2() {
        return mText2;
    }
}
