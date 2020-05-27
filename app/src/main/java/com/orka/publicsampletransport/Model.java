package com.orka.publicsampletransport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Model {
    String image,info,sname;
    long sno,timestamp;
//constructor

    public Model(){}

    //getters and setters

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public long getSno() {
        return sno;
    }

    public void setSno(long sno) {
        this.sno = sno;
    }

    public long getTimestamp() {
//        ConvertTimestamp(timestamp);
        return timestamp;
    }

    public String getStringTimestamp() {
        long time = getTimestamp();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        java.util.Date currenTimeZone=new java.util.Date(time);

        String stringTimestamp = sdf.format(currenTimeZone);
        return stringTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
