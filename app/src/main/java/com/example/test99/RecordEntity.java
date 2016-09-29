package com.example.test99;

/**
 * Created by TracyM on 2016/9/10.
 */
public class RecordEntity {
    protected String name;
    protected String number;
    protected int type;
    protected long date;
    protected long duration;
    protected int _new;

    @Override
    public String toString(){
        return "RecordEntity [toString()=" + name+"," + number+"," + type+"," + date+"," + duration+"," + name+"," + "]";
    }
}
