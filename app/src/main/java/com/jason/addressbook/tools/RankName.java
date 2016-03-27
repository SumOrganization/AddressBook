package com.jason.addressbook.tools;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jason on 2016/3/12.
 */
public class RankName {
    public RankName(List<String> contactName){
        mcontactName = contactName;
    }private List<String> mcontactName;
    private String [] mRankedName;
    Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
    public List<String> getRankedName(List<String> name){
        mRankedName =  (String[])mcontactName.toArray(new String[mcontactName.size()]);
        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(mRankedName,cmp);
        List<String> RankedName = Arrays.asList(mRankedName);

        return RankedName;
    }
}
