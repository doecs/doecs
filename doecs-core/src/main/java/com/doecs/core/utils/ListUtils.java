package com.doecs.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListUtils {
    public static List<String> MapList2StrList(List<Map<String, Object>> mapList){
        List<String>  list = new ArrayList<String>();
        for(Map map: mapList){
            // handle it only when fields count is 1
            if (map.size() == 1){
                list.addAll(map.values());
            }
        }
        return list;
    }

    public static String[] StrlistToArray(List<String> list){
        String[] arr = list.toArray(new String[list.size()]);
        return arr;
    }

    /**
     * 根据指定key和条件取得list中满足条件的第一条记录
     * @param list
     * @param condiKey
     * @param coniVal
     * @return
     */
//    public static Object findFirstMap(List list, String condiKey, String coniVal){
//        Object rs = null;
//        for (Object o : list) {
//            String keyVal = ConvertUtils.convert(((beanClass)o).get(condiKey));
//            if (coniVal.equals(keyVal)) {
//                rs = o;
//                break;
//            }
//        }
//        return rs;
//    }
}