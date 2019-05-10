package com.nemesiss.chuodaidi.Android.Utils;

import android.net.Uri;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.BuildConfig;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class AppUtil {
    public static final String RESOURCE = "android.resource://";
    public static String packageName = BuildConfig.APPLICATION_ID;

    public static Uri ParseResourceIdToUri(int resId)
    {
        return Uri.parse(RESOURCE + packageName + "/" + resId);
    }

    public static int Dp2Px(int dp)
    {
        return (int)(ChuoDaidiActivity.getScale() * dp + 0.5f);
    }

    public static List<Integer> ConvertCardToDrawable(List<Card> cards) throws NoSuchFieldException, IllegalAccessException {
        List<Integer> drawableList = new ArrayList<>();
        for (Card c : cards) {
            Card.Pattern pattern = c.getPattern();
            String val = c.getPoint().toLowerCase();
            String patternName = pattern.name().toLowerCase();

            Field field = R.drawable.class.getField("poke_card_" + val + "_" + patternName);
            boolean IsStatic = Modifier.isStatic(field.getModifiers());
            if(IsStatic) {
                int resVal  = field.getInt(null);
                drawableList.add(resVal);
            }
        }
        return drawableList;
    }
}
