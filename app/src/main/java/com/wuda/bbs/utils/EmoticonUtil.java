package com.wuda.bbs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.wuda.bbs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmoticonUtil {
    private static final Map<String, Integer> emoticons;
    private static final int ITEM_HEIGHT = 128;

    public static Integer getMipmapId(String name) {
        return emoticons.get(name);
    }

    public static Drawable getDrawable(Context context, String name) {
        Integer id = emoticons.get(name);
        if (id != null) {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getDrawable(id);
            int height = ITEM_HEIGHT;
            int width = (int) (drawable.getMinimumWidth() * (height * 1.0f / drawable.getMinimumHeight()));
            drawable.setBounds(0, 0, width, height);
            return drawable;
        }
        return null;
    }

    public static List<String> getNameList() {
        return new ArrayList<>(emoticons.keySet());
    }

    static {
        emoticons = new LinkedHashMap<>();
        emoticons.put("[em01]", R.mipmap.em01);
        emoticons.put("[em02]", R.mipmap.em02);
        emoticons.put("[em03]", R.mipmap.em03);
        emoticons.put("[em04]", R.mipmap.em04);
        emoticons.put("[em05]", R.mipmap.em05);
        emoticons.put("[em06]", R.mipmap.em06);
        emoticons.put("[em07]", R.mipmap.em07);
        emoticons.put("[em08]", R.mipmap.em08);
        emoticons.put("[em09]", R.mipmap.em09);
        emoticons.put("[em10]", R.mipmap.em10);
        emoticons.put("[em11]", R.mipmap.em11);
        emoticons.put("[em12]", R.mipmap.em12);
        emoticons.put("[em13]", R.mipmap.em13);
        emoticons.put("[em14]", R.mipmap.em14);
        emoticons.put("[em15]", R.mipmap.em15);
        emoticons.put("[em16]", R.mipmap.em16);
        emoticons.put("[em17]", R.mipmap.em17);
        emoticons.put("[em18]", R.mipmap.em18);
        emoticons.put("[em19]", R.mipmap.em19);
        emoticons.put("[em20]", R.mipmap.em20);
        emoticons.put("[em21]", R.mipmap.em21);
        emoticons.put("[em22]", R.mipmap.em22);
        emoticons.put("[em23]", R.mipmap.em23);
        emoticons.put("[em24]", R.mipmap.em24);
        emoticons.put("[em25]", R.mipmap.em25);
        emoticons.put("[em26]", R.mipmap.em26);
        emoticons.put("[em27]", R.mipmap.em27);
        emoticons.put("[em28]", R.mipmap.em28);
        emoticons.put("[em29]", R.mipmap.em29);
        emoticons.put("[em30]", R.mipmap.em30);
        emoticons.put("[em31]", R.mipmap.em31);
        emoticons.put("[em32]", R.mipmap.em32);
        emoticons.put("[em33]", R.mipmap.em33);
        emoticons.put("[em34]", R.mipmap.em34);
        emoticons.put("[em35]", R.mipmap.em35);
        emoticons.put("[em36]", R.mipmap.em36);
        emoticons.put("[em37]", R.mipmap.em37);
        emoticons.put("[em38]", R.mipmap.em38);
        emoticons.put("[em39]", R.mipmap.em39);
        emoticons.put("[em40]", R.mipmap.em40);
        emoticons.put("[em41]", R.mipmap.em41);
        emoticons.put("[em42]", R.mipmap.em42);
        emoticons.put("[em43]", R.mipmap.em43);
        emoticons.put("[em44]", R.mipmap.em44);
        emoticons.put("[em45]", R.mipmap.em45);
        emoticons.put("[em46]", R.mipmap.em46);
        emoticons.put("[em47]", R.mipmap.em47);
        emoticons.put("[em48]", R.mipmap.em48);
        emoticons.put("[em49]", R.mipmap.em49);
        emoticons.put("[em50]", R.mipmap.em50);
        emoticons.put("[em51]", R.mipmap.em51);
        emoticons.put("[em52]", R.mipmap.em52);
        emoticons.put("[em53]", R.mipmap.em53);
        emoticons.put("[em54]", R.mipmap.em54);
        emoticons.put("[em55]", R.mipmap.em55);
        emoticons.put("[em56]", R.mipmap.em56);
        emoticons.put("[em57]", R.mipmap.em57);
        emoticons.put("[em58]", R.mipmap.em58);
        emoticons.put("[em59]", R.mipmap.em59);
        emoticons.put("[em60]", R.mipmap.em60);
        emoticons.put("[em61]", R.mipmap.em61);
        emoticons.put("[em62]", R.mipmap.em62);
        emoticons.put("[em63]", R.mipmap.em63);
        emoticons.put("[em64]", R.mipmap.em64);
        emoticons.put("[em65]", R.mipmap.em65);
        emoticons.put("[em66]", R.mipmap.em66);
        emoticons.put("[em67]", R.mipmap.em67);
        emoticons.put("[em68]", R.mipmap.em68);
        emoticons.put("[em69]", R.mipmap.em69);
        emoticons.put("[em70]", R.mipmap.em70);
        emoticons.put("[em71]", R.mipmap.em71);
        emoticons.put("[em72]", R.mipmap.em72);
        emoticons.put("[em73]", R.mipmap.em73);
        emoticons.put("[em74]", R.mipmap.em74);
        emoticons.put("[em75]", R.mipmap.em75);
        emoticons.put("[em76]", R.mipmap.em76);
        emoticons.put("[em77]", R.mipmap.em77);
        emoticons.put("[em78]", R.mipmap.em78);
        emoticons.put("[em79]", R.mipmap.em79);
        emoticons.put("[em80]", R.mipmap.em80);
        emoticons.put("[em81]", R.mipmap.em81);
        emoticons.put("[em82]", R.mipmap.em82);
        emoticons.put("[em83]", R.mipmap.em83);
        emoticons.put("[em84]", R.mipmap.em84);
        emoticons.put("[em85]", R.mipmap.em85);
        emoticons.put("[em86]", R.mipmap.em86);
        emoticons.put("[em87]", R.mipmap.em87);
        emoticons.put("[em88]", R.mipmap.em88);
        emoticons.put("[em89]", R.mipmap.em89);
        emoticons.put("[em90]", R.mipmap.em90);
        emoticons.put("[em91]", R.mipmap.em91);
        emoticons.put("[em92]", R.mipmap.em92);
        emoticons.put("[em93]", R.mipmap.em93);
        emoticons.put("[em94]", R.mipmap.em94);
        emoticons.put("[em95]", R.mipmap.em95);
        emoticons.put("[em96]", R.mipmap.em96);
        emoticons.put("[em97]", R.mipmap.em97);
        emoticons.put("[em98]", R.mipmap.em98);
        emoticons.put("[em99]", R.mipmap.em99);
        emoticons.put("[em100]", R.mipmap.em100);
        emoticons.put("[em101]", R.mipmap.em101);
        emoticons.put("[em102]", R.mipmap.em102);
        emoticons.put("[em103]", R.mipmap.em103);
        emoticons.put("[em104]", R.mipmap.em104);
        emoticons.put("[em105]", R.mipmap.em105);
        emoticons.put("[em106]", R.mipmap.em106);
        emoticons.put("[em107]", R.mipmap.em107);
        emoticons.put("[em108]", R.mipmap.em108);
        emoticons.put("[em109]", R.mipmap.em109);
        emoticons.put("[em110]", R.mipmap.em110);
        emoticons.put("[em111]", R.mipmap.em111);
        emoticons.put("[em112]", R.mipmap.em112);
        emoticons.put("[em113]", R.mipmap.em113);
        emoticons.put("[em114]", R.mipmap.em114);
        emoticons.put("[em115]", R.mipmap.em115);
        emoticons.put("[em116]", R.mipmap.em116);
    }
}
