package com.doecs.core.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteUtils {
    /**
     * 构造新字节时需要与的值表
     * @author jayxu
     */
    private static final byte[] BUILD_BYTE_TABLE = new byte[]{(byte) 128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ByteUtils() {
    }

    /**
     * short转换到字节数组
     *
     * @param number 需要转换的数据。
     * @return 转换后的字节数组。
     */
    public static byte[] shortToBytes(short number) {
        byte[] b = new byte[2];
        for (int i = 1; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }

    /**
     * short转byte[]
     *
     * @param b
     * @param s
     * @param index
     */
    public static void bytesToShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index + 0] = (byte) (s >> 0);
    }

    /**
     * 字节到short转换
     *
     * @param b short的字节数组
     * @return short数值。
     */
    public static short bytesToShort(byte[] b) {
        return (short) ((((b[0] & 0xff) << 8) | b[1] & 0xff));
    }

    /**
     * 整型转换到字节数组
     *
     * @param number 整形数据。
     * @return 整形数据的字节数组。
     */
    public static byte[] intToBytes(int number) {
        byte[] b = new byte[4];
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }
    /**
     * int转byte
     * @param x
     * @return
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }
    /**
     * 字节数组到整型转换
     *
     * @param b 整形数据的字节数组。
     * @return 字节数组转换成的整形数据。
     */
    public static int bytesToInt(byte[] b) {
        if(b.length == 2){
            return bytesToShort(b);
        } else if(b.length == 1){
            return byteToInt(b[0]);
        } else {
            return ((((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff)));
        }
    }
    /**
     * byte转int
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        //Java的byte是有符号，通过 &0xFF转为无符号
        return b & 0xFF;
    }
    /**
     * long转换到字节数组
     *
     * @param number 长整形数据。
     * @return 长整形转换成的字节数组。
     */
    public static byte[] longToBytes(long number) {
        byte[] b = new byte[8];
        for (int i = 7; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }

    /**
     * 字节数组到整型的转换
     *
     * @param b 长整形字节数组。
     * @return 长整形数据。
     */
    public static long bytesToLong(byte[] b) {
        return ((((long) b[0] & 0xff) << 56) | (((long) b[1] & 0xff) << 48) | (((long) b[2] & 0xff) << 40) | (((long) b[3] & 0xff) << 32) | (((long) b[4] & 0xff) << 24)
                | (((long) b[5] & 0xff) << 16) | (((long) b[6] & 0xff) << 8) | ((long) b[7] & 0xff));
        //or

//        buffer.put(b, 0, b.length);
//        buffer.flip();//need flip
//        return buffer.getLong();
    }

    /**
     * byte数组转为集合
     * @param arr 需要转转换的数据。
     * @return 消息结构体。
     */
    public static List<Byte> bytesToList(byte[] arr){
        List<Byte> list = new ArrayList<Byte>();
        int len = arr.length;
        for(int i = 0;i<len;i++){
            list.add(arr[i]);
        }
        return list;
    }
    /**
     * Byte集合转为byte数组
     * @param  list 需要转转换的数据。
     * @return 消息结构体。
     */
    public static byte[] byteListToByteArr(List<Byte> list){
        byte[] b = new byte[list.size()];
        int len = b.length;
        for(int i = 0;i<len;i++){
            b[i]=list.get(i);
        }
        return b;
    }

    /**
     * double转换到字节数组
     *
     * @param d 双精度浮点。
     * @return 双精度浮点的字节数组。
     */
    public static byte[] doubleToBytes(double d) {
        byte[] bytes = new byte[8];
        long l = Double.doubleToLongBits(d);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Long.valueOf(l).byteValue();
            l = l >> 8;
        }
        return bytes;
    }

    /**
     * 字节数组到double转换
     *
     * @param b 双精度浮点字节数组。
     * @return 双精度浮点数据。
     */
    public static double bytesToDouble(byte[] b) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;

        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;

        l |= ((long) b[7] << 56);

        return Double.longBitsToDouble(l);
    }

    /**
     * float转换到字节数组
     *
     * @param d 浮点型数据。
     * @return 浮点型数据转换后的字节数组。
     */
    public static byte[] floatToBytes(float d) {
        byte[] bytes = new byte[4];
        int l = Float.floatToIntBits(d);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Integer.valueOf(l).byteValue();
            l = l >> 8;
        }
        return bytes;
    }

    /**
     * 字节数组到float的转换
     *
     * @param b 浮点型数据字节数组。
     * @return 浮点型数据。
     */
    public static float bytesToFloat(byte[] b) {
        int l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;

        return Float.intBitsToFloat(l);
    }

    /**
     * 字节转十六进制字符小写
     * @param b
     * @return
     */
    public static String byteToHexStr(byte b){
        return Integer.toString( ( b & 0xff ) + 0x100, 16).substring(1);
    }

    /**
     * 字符串到字节数组转换
     *
     * @param s       字符串。
     * @param charset 字符编码
     * @return 字符串按相应字符编码编码后的字节数组。
     */
    public static byte[] strToBytes(String s, Charset charset) {
        return s.getBytes(charset);
    }
    /**
     * 字符串到位数组转换
     *
     * @param s       字符串。
     * @param charset 字符编码
     * @return 字符串按相应字符编码编码后的字节数组。
     */
    public static boolean[] strToBits(String s, Charset charset) {
        return bytesToBits(strToBytes(s, charset));
    }

    /**
     * 字节数组带字符串的转换
     *
     * @param b       字符串按指定编码转换的字节数组。
     * @param charset 字符编码。
     * @return 字符串。
     */
    public static String bytesToStr(byte[] b, Charset charset) {
        return new String(b, charset);
    }

    /**
     * 对象转换成字节数组。
     *
     * @param obj 字节数组。
     * @return 对象实例相应的序列化后的字节数组。
     * @throws IOException
     */
    public static byte[] objectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buff);
        out.writeObject(obj);
        try {
            return buff.toByteArray();
        } finally {
            out.close();
        }
    }

    /**
     * 序死化字节数组转换成实际对象。
     *
     * @param b 字节数组。
     * @return 对象。
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object bytesToObject(byte[] b) throws IOException, ClassNotFoundException {
        ByteArrayInputStream buff = new ByteArrayInputStream(b);
        ObjectInputStream in = new ObjectInputStream(buff);
        Object obj = in.readObject();
        try {
            return obj;
        } finally {
            in.close();
        }
    }

    /**
     * 比较两个字节的每一个bit位是否相等.
     *
     * @param a 比较的字节.
     * @param b 比较的字节
     * @return ture 两个字节每一位都相等,false有至少一位不相等.
     */
    public static boolean byteCompare(byte a, byte b) {
        return Arrays.equals(byteToBits(a), byteToBits(b));
    }

    /**
     * 比较两个bit数组的每一个bit位是否相等.
     *
     * @param a 比较的bit数组
     * @param b 比较的bit数组
     * @return ture 每一位都相等,false有至少一位不相等.
     */
    public static boolean bitsCompare(boolean[] a, boolean[] b) {
        return Arrays.equals(a, b);
    }

    /**
     * 位数组高位高位补齐0
     * @param arr
     * @param len
     * @return
     */
    public static boolean[] bitsPadding(boolean[] arr, int len){
        boolean[] newArr = new boolean[len];
        for(int i=0; i<len; i++){
            if (i<len-arr.length){
                newArr[i] = false;
            }else{
                newArr[i] = arr[i-len-arr.length];
            }
        }
        return newArr;
    }

    /**
     * 比较两个数组中的每一个字节,两个字节必须二进制字节码每一位都相同才表示两个 byte相同.
     *
     * @param a 比较的字节数组.
     * @param b 被比较的字节数.
     * @return ture每一个元素的每一位两个数组都是相等的, false至少有一位不相等.
     */
    public static boolean bytesCompare(byte[] a, byte[] b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }

        int length = a.length;
        if (b.length != length) {
            return false;
        }

        for (int count = 0; count < a.length; count++) {
            if (!byteCompare(a[count], b[count])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回某个字节的bit组成的字符串.
     *
     * @param b 字节.
     * @return Bit位组成的字符串.
     */
    public static String getBitStrFromByte(byte b) {
        StringBuilder buff = new StringBuilder();
        boolean[] array = byteToBits(b);
        for (int i = 0; i < array.length; i++) {
            buff.append(array[i] ? 1 : 0);
        }
        return buff.toString();
    }

    /**
     * 计算出给定byte中的每一位,并以一个布尔数组返回. true表示为1,false表示为0.
     *
     * @param b 字节.
     * @return 指定字节的每一位bit组成的数组.
     */
    public static boolean[] byteToBits(byte b) {
        boolean[] buff = new boolean[8];
        int index = 0;
        for (int i = 7; i >= 0; i--) {
            buff[index++] = ((b >>> i) & 1) == 1;
        }
        return buff;
    }

    /**
     * 计算出给定byte数组中的每一位,并以一个布尔数组返回. true表示为1,false表示为0.
     *
     * @param byteArr 字节.
     * @return 指定字节的每一位bit组成的数组.
     */
    public static boolean[] bytesToBits(byte[] byteArr) {
        boolean[] buff = new boolean[byteArr.length * 8];
        for (int i=0; i < byteArr.length; i++) {
            boolean[] singleBuff = byteToBits(byteArr[i]);
            System.arraycopy(singleBuff, 0, buff, i*8, 8);
        }
        return buff;
    }

    /**
     * bytes按BCD格式转为date
     * @param bytes
     * @param fmt yyMMddHHmmss, yyyyMMddHHmmss, yyMMdd, HHmmss, HHmm
     * @return
     */
    public static Date bytesBcdToDate(byte[] bytes, String fmt){
        try {
            String s = bytesToHexStr(bytes, "");
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.parse(s);
        } catch (Exception e) {
            LogUtils.getLogger().error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * date按BCD格式转为bytes
     * @param date
     * @param fmt
     * @return
     */
    public static byte[] dateToBytesBcd(Date date, String fmt){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            String s = sdf.format(date);
            return hexStrToBytes(s);
        } catch (Exception e) {
            LogUtils.getLogger().error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 返回指定字节中指定bit位,true为1,false为0. 指定的位从0-7,超出将抛出数据越界异常.
     *
     * @param b     需要判断的字节.
     * @param index 字节中指定位.
     * @return 指定位的值.
     */
    public static boolean getBitFromByte(byte b, int index) {
        return byteToBits(b)[index];
    }
    /**
     * 返回指定字节中指定bit位,true为1,false为0. 指定的位从0-7,超出将抛出数据越界异常.
     *
     * @param b     需要判断的字节.
     * @param index 字节中指定位.
     * @return 指定位的值.
     */
    public static boolean getBitFromBytes(byte[] b, int index) {
        return bytesToBits(b)[index];
    }

    /**
     * 返回指定字节中指定bit位,true为1,false为0. 指定的位从0-7,超出将抛出数据越界异常.
     *
     * @param byteArr     需要判断的字节.
     * @param start 字节中指定开始位.
     * @param start 字节中指定截取长度.
     * @return 指定位的值.
     */
    public static boolean[] getBitsFromBytes(byte[] byteArr, int start, int len) {
        boolean[] bitArr = bytesToBits(byteArr);
        boolean[] rsArr = new boolean[len];
        System.arraycopy(bitArr, start, rsArr, 0, len);
        return rsArr;
    }

    /**
     * 返回指定字节中指定bit位,true为1,false为0. 指定的位从0-7,超出将抛出数据越界异常.
     *
     * @param bitArr     需要判断的字节.
     * @param start 字节中指定开始位.
     * @param start 字节中指定截取长度.
     * @return 指定位的值.
     */
    public static boolean[] getBitsFromBits(boolean[] bitArr, int start, int len) {
        boolean[] rsArr = new boolean[len];
        System.arraycopy(bitArr, start, rsArr, 0, len);
        return rsArr;
    }

    /**
     * 根据布尔数组表示的二进制构造一个新的字节.
     *
     * @param values 布尔数组,其中true表示为1,false表示为0.
     * @return 构造的新字节.
     */
    public static byte bitsToByte(boolean[] values) {
        byte b = 0;
        for (int i = 0; i < 8; i++) {
            if (values[i]) {
                b |= BUILD_BYTE_TABLE[i];
            }
        }
        return b;
    }

    /**
     * 将指定字节中的某个bit位替换成指定的值,true代表1,false代表0.
     *
     * @param b        需要被替换的字节.
     * @param index    位的序号,从0开始.超过7将抛出越界异常.
     * @param newValue 新的值.
     * @return 替换好某个位值的新字节.
     */
    public static byte replaceBitValueInByte(byte b, int index, boolean newValue) {
        boolean[] bitValues = byteToBits(b);
        bitValues[index] = newValue;
        return bitsToByte(bitValues);
    }

    /**
     * 将指定的IP地址转换成字节表示方式. IP数组的每一个数字都不能大于255,否则将抛出IllegalArgumentException异常.
     *
     * @param address IP地址数组.
     * @return IP地址字节表示方式.
     */
    public static byte[] ipAddressStrToBytes(String address) {
        if (address == null || address.length() < 0 || address.length() > 15) {
            throw new IllegalArgumentException("Invalid IP address.");
        }

        final int ipSize = 4;// 最大IP位数
        final char ipSpace = '.';// IP数字的分隔符
        int[] ipNums = new int[ipSize];
        StringBuilder number = new StringBuilder();// 当前操作的数字
        StringBuilder buff = new StringBuilder(address);
        int point = 0;// 当前操作的数字下标,最大到3.
        char currentChar;
        for (int i = 0; i < buff.length(); i++) {
            currentChar = buff.charAt(i);
            if (ipSpace == currentChar) {
                // 当前位置等于最大于序号后,还有字符没有处理表示这是一个错误的IP.
                if (point == ipSize - 1 && buff.length() - (i + 1) > 0) {
                    throw new IllegalArgumentException("Invalid IP address.");
                }
                ipNums[point++] = Integer.parseInt(number.toString());
                number.delete(0, number.length());
            } else {
                number.append(currentChar);
            }
        }
        ipNums[point] = Integer.parseInt(number.toString());

        byte[] ipBuff = new byte[ipSize];
        int pointNum = 0;
        for (int i = 0; i < 4; i++) {
            pointNum = Math.abs(ipNums[i]);
            if (pointNum > 255) {
                throw new IllegalArgumentException("Invalid IP address.");
            }
            ipBuff[i] = (byte) (pointNum & 0xff);
        }

        return ipBuff;
    }


    /**
     * 从byte[]中抽取新的byte[]
     * @param data - 元数据
     * @param start - 开始位置
     * @param len - 结束位置
     * @return 新byte[]
     */
    public static byte[] getBytesFromBytes(byte[]data, int start , int len){
        int end = start + len;
        byte[] ret=new byte[end-start];
        for(int i=0;(start+i)<end;i++){
            ret[i]=data[start+i];
        }
        return ret;
    }

    /**
     * bitArr转ByteArr(不足8的整数倍，高位用0补齐)
     * @param bits
     * @return
     */
    public static byte[] bitsToBytes(boolean[] bits) {
        int byteLen = bits.length / 8;
        int rest = bits.length % 8 == 0 ? 0 : (8 - bits.length % 8);
        int bitsNewLen = bits.length;
        if (rest>0){
            byteLen++;
            // 补齐到8的整数倍
            bitsNewLen = bits.length + rest;
        }
        boolean[] bitsNew = new boolean[bitsNewLen];
        for(int i=0; i < bitsNewLen; i++){
            if(i < rest){
                bitsNew[i] = false;  // 不足8的整数位，高位补0
            }else{
                bitsNew[i] = bits[i-rest];
            }
        }

        byte[] toReturn = new byte[byteLen];
        for (int entry = 0; entry < toReturn.length; entry++) {
            for (int bit = 0; bit < 8; bit++) {
                if (bitsNew[entry * 8 + bit]) {
                    toReturn[entry] |= (128 >> bit);
                }
            }
        }

        return toReturn;
    }

    /**
     * 将二进制数组转输出0101格式字符
     * @param bits
     * @return
     */
    public static String bitsToZeroOneStr(boolean[] bits) {
        if(bits == null || bits.length == 0){
            return null;
        }
        String[] arr = new String[bits.length];
        for (int i=0; i<arr.length; i++){
            if(bits[i]){
                arr[i] = "1";
            }else{
                arr[i] = "0";
            }
        }
        return StringUtils.join(arr);
    }

    public static boolean[] bitStrToBits(String s){
        if(StringUtils.isBlank(s)){
            return null;
        }
        // 去除前缀0b
        if(s.indexOf("0b") == 0) {
            s = s.replace("0b", "");
        }
        Pattern pattern = Pattern.compile("[0-1]+"); // 只能包含0，1
        Matcher rs = pattern.matcher(s);
        if( !rs.matches() ){
            return null;
        }
        String[] sArr = new String[s.length()];

        for(int i = 0; i < s.length(); i++){
            sArr[i] = s.substring(i, i+1);
//            System.out.println(sArr[i]);
        }

        boolean[] bits = new boolean[sArr.length];
        for (int i=0; i<sArr.length; i++){
            if("0".equals(sArr[i])){
                bits[i] = false;
            } else{
                bits[i] = true;
            }
        }
        return bits;
    }

    /**
     * 流转换为byte[]
     * @param inStream
     * @return
     */
    public static byte[] inputStreamToBytes(InputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            byte[] data = null;
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            return data;
        }catch (IOException e) {
            return null;
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
    }
    /**
     * byte[]转inputstream
     * @param b
     * @return
     */
    public static InputStream bytesToInputStream(byte[] b){
        return new ByteArrayInputStream(b);
    }

    /**
     * byte数组转换为Stirng
     * @param s1 -数组
     * @param encode -字符集
     * @param err -转换错误时返回该文字
     * @return
     */
    public static String bytesToStr(byte[] s1, String encode, String err){
        try {
            return new String(s1,encode);
        } catch (UnsupportedEncodingException e) {
            return err==null?null:err;
        }
    }
    /**
     * byte数组转换为Stirng
     * @param s1-数组
     * @param encode-字符集
     * @return
     */
    public static String bytesToStr(byte[] s1, String encode){
        return bytesToStr(s1,encode,null);
    }

    /**
     * 16进制字符创转int
     * @param hexString
     * @return
     */
    public static int hexStrToInt(String hexString){
        return Integer.parseInt(hexString,16);
    }

    /**
     * 十进制转二进制
     * @param i
     * @return
     */
    public static String intToBitStr(int i){
        return Integer.toBinaryString(i);
    }

    /**
     * 将16进制字符串转换为byte[]，自动去除空格和0x
     * @param str
     * @return
     */
    public static byte[] hexStrToBytes(String str) {
        str = str.replaceAll("[\\s\\r\\n]", "").replace("0x", "");
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
//        if(str == null || str.trim().equals("")) {
//            return new byte[0];
//        }
//
//        if(str.length()>2 && "0x".equals(str.substring(0,2))){
//            str = str.substring(2,str.length());
//        }
//
//        if(str.length()<4){
//            DecimalFormat df = new DecimalFormat("00");
//            str = df.format(Integer.parseInt(str));
//        }
//        byte[] bytes = new byte[str.length() / 2];
//        for(int i = 0; i < str.length() / 2; i++) {
//            String subStr = str.substring(i * 2, i * 2 + 2);
//            bytes[i] = (byte) Integer.parseInt(subStr, 16);
//        }
//
//        return bytes;
    }
    /**
     * byte数组转16进制hex字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexStrOld(byte[] bytes) {
        if (bytes == null){
            return null;
        }
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }
    /**
     * 字节数组转16进制字符串,无首字符0x和分隔符
     * @param b 字节数组
     * @return
     */
    public static String bytesToHexStr(byte[] b){
        return bytesToHexStr(b, "", "");
    }

    /**
     * 字节数组转16进制字符串,可指定分隔符
     * @param b 字节数组
     * @param splitStr 字节字符分隔符
     * @return
     */
    public static String bytesToHexStr(byte[] b, String splitStr){
        return bytesToHexStr(b, splitStr, "");
    }

    /**
     * 字节数组转16进制字符串,可指定分隔符和首字符
     * @param b 字节数组
     * @param splitStr 字节字符分隔符
     * @param firstStr 首字符（如：0x）
     * @return
     */
    public static String bytesToHexStr(byte[] b, String splitStr, String firstStr){
        String result="";
        for (int i=0; i < b.length; i++) {
            result += byteToHexStr(b[i]) + splitStr;
        }
        return firstStr + result.trim();
    }

    /**
     * 根据长度生成值均为0的字节数组
     */
    public static byte[] genZeroBytes(int len) {
        byte[] arr = new byte[len];
        for (int i=0; i<len; i++) {
            arr[i] = 0x00;
        }
        return arr;
    }

    /**
     * 根据长度生成值均为0的位数组
     */
    public static boolean[] genZeroBits(int len) {
        boolean[] arr = new boolean[len];
        for (int i=0; i<len; i++) {
            arr[i] = false;
        }
        return arr;
    }
    /**
     * 位数组高位高位补齐0
     * @param arr
     * @param len
     * @return
     */
    public static byte[] bytesPadding(byte[] arr, int len){
        byte[] newArr = new byte[len];
        for(int i=0; i<len; i++){
            if (i<len-arr.length){
                newArr[i] = 0x00;
            }else{
                newArr[i] = arr[i-len-arr.length];
            }
        }
        return newArr;
    }
    /**
     * 合并多个byte数组
     * @param arrs
     * @return
     */
    public static byte[] bytesMerge(byte[] ... arrs) {
        // 计算长度
        int len = 0;

        for (byte[] arr : arrs) {
            // 为null则忽略
            if (arr != null) {
                len += arr.length;
            }
        }
        byte[] newArr = new byte[len];

        int idx = 0;
        for (byte[] arr : arrs) {
            // 为null则忽略
            if (arr != null) {
                for (byte b : arr) {
                    newArr[idx] = b;
                    idx++;
                }
            }
        }
        return newArr;
    }

    /**
     * 合并多个bit数组
     * @param arrs
     * @return
     */
    public static boolean[] bitsMerge(boolean[] ... arrs) {
        // 计算长度
        int len = 0;

        for (boolean[] arr : arrs) {
            if (arr != null){
                len += arr.length;
            }
        }
        boolean[] newArr = new boolean[len];

        int idx = 0;
        for (boolean[] arr : arrs) {
            if (arr != null) {
                for (boolean b : arr) {
                    newArr[idx] = b;
                    idx++;
                }
            }
        }
        return newArr;
    }
}

