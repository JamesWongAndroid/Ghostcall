package com.kickbackapps.ghostcall.ApiObjects.Numbers;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ynott on 7/23/15.
 */
public class NumbersDataParser {

    @Expose
    private List<NumbersData> numberData = new ArrayList();

    /**
     *
     * @return
     * The numberData
     */
    public List<NumbersData> getNumberData() {
        return numberData;
    }

    /**
     *
     * @param numberData
     * The numberData
     */
    public void setNumberData(List<NumbersData> numberData) {
        this.numberData = numberData;
    }

}
