package com.doecs.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidCheckUtils {
    public static boolean chkDateStr(String s, String fmt){
        boolean rs=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(s);
        } catch (ParseException e) {
            rs=false;
        }
        return rs;
    }

    public static boolean chkMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (mobile.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
    }
    /**
     * 检查身份证号码合法性
     * @param idCardNo
     * @return
     * @throws Exception
     */
    public static boolean chkIdCardNo(String idCardNo){
        if(StringUtils.isBlank(idCardNo)){
            return false;
        }
        int length = idCardNo.length();
        if(length == 15){
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(idCardNo);
            return m.matches();
        }else if(length == 18){
            String front_17 = idCardNo.substring(0, idCardNo.length() - 1);//号码前17位
            String verify = idCardNo.substring(17, 18);//校验位(最后一位)
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(front_17);
            if(!m.matches()){
                return false;
            }else{
                return checkIdCardNoVerify(front_17, verify);
            }
        }
        return false;
    }

    /**
     * 校验验证位合法性
     * @param front_17
     * @param verify
     * @return
     * @throws Exception
     */
    private static boolean checkIdCardNoVerify(String front_17,String verify){
        int[] wi = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1};
        String[] vi = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int s = 0;
        for(int i = 0; i<front_17.length(); i++){
            int ai = Integer.parseInt(front_17.charAt(i) + "");
            s += wi[i]*ai;
        }
        int y = s % 11;
        String v = vi[y];
        if(!(verify.toUpperCase().equals(v))){
            return false;
        }
        return true;
    }
}
