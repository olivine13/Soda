/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
@Entity
public class KeyWord {

    public final static int TYPE_HOT = 0X01;
    public final static int TYPE_HISTORY = 0x02;
    public final static int TYPE_ALL = 0x03;
    
    @Property
    @NotNull
    private String keyword;
    @Property
    private long count;
    @Property
    private long time;
    @Property
    @NotNull
    private int type;
    @Generated(hash = 1887911918)
    public KeyWord(@NotNull String keyword, long count, long time, int type) {
        this.keyword = keyword;
        this.count = count;
        this.time = time;
        this.type = type;
    }
    @Generated(hash = 617591908)
    public KeyWord() {
    }
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public long getCount() {
        return this.count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
