package com.tianhong.app.tianhongapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by stonelv on 2017/4/28.
 */

public final class Product {
    @NonNull
    private final int pId;

    @NonNull
    private final String pName;

    @NonNull
    private final float pPrice;

    @Nullable
    private final String pDescription;

    @NonNull
    private boolean pClosed;

    public Product(@NonNull int id, @NonNull String name, @NonNull float price, @Nullable String desc, @NonNull boolean closed)
    {
        pId = id;
        pName = name;
        pPrice = price;
        pDescription = desc;
        pClosed = closed;
    }

    @NonNull
    public int getId() {
        return pId;
    }

    @NonNull
    public String getName() {
        return pName;
    }

    @NonNull
    public float getPrice() {
        return pPrice;
    }

    @Nullable
    public String getDescription() {
        return pDescription;
    }

    public boolean IsClosed() {return pClosed; }

    public void setClosed(boolean closed) {
        pClosed = closed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product pdt = (Product)obj;
        return pdt.pId == pId &&
                pdt.pName == pName &&
                pdt.pPrice == pPrice;
    }
}
