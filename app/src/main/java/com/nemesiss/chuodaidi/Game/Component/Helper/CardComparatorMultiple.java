package com.nemesiss.chuodaidi.Game.Component.Helper;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardComparatorMultiple {
    public static boolean Compare(List<Card> left,List<Card> right)
    {
        int length = left.size();
        int o1PointSize1 = CardHelper.GetPointSize(left.get(0).getPoint());
        int o2PointSize1 = CardHelper.GetPointSize(right.get(0).getPoint());
        switch (length) {
            case 2:{

                if(o1PointSize1==o2PointSize1)
                {
                    int o11Size = left.get(0).getPattern().size;
                    int o12Size=left.get(1).getPattern().size;
                    int maxo1;
                    if(o11Size>o12Size)
                    {
                        maxo1=o11Size;
                    }
                    else
                        maxo1=o12Size;
                    int o21Size=right.get(0).getPattern().size;
                    int o22Size=right.get(1).getPattern().size;
                    int maxo2;
                    if(o21Size>o22Size)
                    {
                        maxo2=o21Size;
                    }
                    else
                        maxo2=o22Size;
                    if(maxo1>maxo2) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
              if(o1PointSize1>o2PointSize1)
              {
                  return true;
              }
              else
                  return  false;
            }
            case 3:{
                return o1PointSize1 > o2PointSize1;
            }
            case 5:{
                int o1PointSize2 = CardHelper.GetPointSize(left.get(1).getPoint());
                int o2PointSize2 = CardHelper.GetPointSize(right.get(1).getPoint());
                int o1PointSize3 = CardHelper.GetPointSize(left.get(2).getPoint());
                int o2PointSize3 = CardHelper.GetPointSize(right.get(2).getPoint());
                int o1PointSize4 = CardHelper.GetPointSize(left.get(3).getPoint());
                int o2PointSize4 = CardHelper.GetPointSize(right.get(3).getPoint());
                int o1PointSize5 = CardHelper.GetPointSize(left.get(4).getPoint());
                int o2PointSize5 = CardHelper.GetPointSize(right.get(4).getPoint());
                int o11Size=left.get(0).getPattern().size;
                int o12Size=left.get(1).getPattern().size;
                int o13Size=left.get(2).getPattern().size;
                int o14Size=left.get(3).getPattern().size;
                int o15Size=left.get(4).getPattern().size;
                int o21Size=right.get(0).getPattern().size;
                int o22Size=right.get(1).getPattern().size;
                int o23Size=right.get(2).getPattern().size;
                int o24Size=right.get(3).getPattern().size;
                int o25Size=right.get(4).getPattern().size;
                //判断5张牌属于哪种类型
                //两组5张牌内部排序大小，调用单张牌排大小的函数


                //同花顺最大

                //四带一次之

                //三带对

                //同花五

                //杂顺最小

                break;
            }
        }
        return true;
    }
}
