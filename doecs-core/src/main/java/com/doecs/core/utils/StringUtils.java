package com.doecs.core.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;
import sun.misc.BASE64Decoder;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Jie Xu
 * @email jayxu678@qq.com
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String getRandomString(int i) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < i; j++) {
			sb.append(String.valueOf(RandomUtils.nextInt(0, 10)));
		}
		return sb.toString();
	}

	/*
	 * 返回指定长度随机数，在前面补0
	 */
	public static String genRandomStr(int strLen) {

		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLen);

		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);

		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLen + 1);
	}

	public static String stringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String joinMapList(List<Map<String, Object>> mapList, String separator){
		List<String>  list = ListUtils.MapList2StrList(mapList);
		return org.apache.commons.lang3.StringUtils.join(list.toArray(), separator);
	}

	public static String underline2Camel(String line){
		return underline2Camel(line, true);
	}
	/**
	 * 下划线转驼峰法
	 * ex. UNDER_LINE_1_D_imp -> underLine1DImp
	 * @param line 源字符串
	 * @return 转换后的字符串
	 */
	public static String underline2Camel(String line, boolean smallCamel){
		if(line==null||"".equals(line)){
			return "";
		}
		if(line.indexOf("_")<0){
			return line;
		}
	 	// 大小驼峰,是否为小驼峰
//		boolean smallCamel = true;
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
			int index=word.lastIndexOf('_');
			if(index>0){
				sb.append(word.substring(1, index).toLowerCase());
			}else{
				sb.append(word.substring(1).toLowerCase());
			}
		}
		if(sb.length() == 0){
			return line;
		}else{
			return sb.toString();
		}
	}
	/**
	 * 驼峰法转下划线
	 * ex. UnderLine2MePC -> under_line2_me_p_c
	 * @param line 源字符串
	 * @return 转换后的字符串
	 */
	public static String camel2Underline(String line){
		if(line==null || "".equals(line)){
			return "";
		}
		// 不处理带有空格、括号、 as 、_，如："count(*) as cnt"
		if(line.contains(" ") || line.contains(" as ") || line.contains("(") || line.contains("_")){
			return line;
		}

		// 如果有点号则先分割，嵌套遍历，再连接起来返回
		if(line.indexOf(".")>0){
			String[] sArr = line.split("\\.");
			String sArrNew[] = new String[sArr.length];
			for (int i=0; i<sArr.length; i++) {
				sArrNew[i] = camel2Underline(sArr[i]);
			}
			return org.apache.commons.lang3.StringUtils.join(sArrNew,".");
		}

		line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(word.toUpperCase());
			sb.append(matcher.end()==line.length()?"":"_");
		}
		String s = sb.toString();
		if(s.length() == 0){
			return line;
		} else{
			return s.toLowerCase();
		}
	}


	private static final String ENCODING = "UTF-8";
	/**
	 * URLBase64加密
	 */
	public static String encode(String data) throws UnsupportedEncodingException{
		byte[] encodedByte = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
		return new String(encodedByte, ENCODING);
	}
	/**
	 * URLBase64解密
	 */
	public static String decode(String data) throws UnsupportedEncodingException {
		byte[] decodedByte = Base64.decodeBase64(data.getBytes(ENCODING));
		return new String(decodedByte, ENCODING);
	}

	public static int strToInt(String s, int defaultVal){
		try{
			return Integer.parseInt(s);
		}catch (Exception e){
			return defaultVal;
		}
	}

	public void test() throws UnsupportedEncodingException {
//		LogUtils.getLogger().debug(underline2Camel("UNDER_LINE_1_D_imp"));
//		LogUtils.getLogger().debug(camel2Underline("UnderLine2MePC"));
		LogUtils.getLogger().debug(camel2Underline("pId"));
//		String[] sArr = "informationSchema.tables".split("\\.");
//		for (int i=0; i< sArr.length; i++){
//			LogUtils.getLogger().debug(sArr[i]);
//		}


		String data = "找一个好姑娘做老婆是我的梦 想！";
		System.out.println("原文-->"+data);
		String encodedStr = encode(data);
		System.out.println("加密后-->"+encodedStr);
		String decodedStr = decode(encodedStr);
		System.out.println("解密后-->"+decodedStr);
		System.out.println(data.equals(decodedStr));
	}

	/**
	 * 一次性判断多个或单个对象为空。
	 * @param objects
	 * @author zhou-baicheng
	 * @return 只要有一个元素为Blank，则返回true
	 */
	public static boolean isBlank(Object...objects){
		Boolean result = false ;
		for (Object object : objects) {
			if(null == object || "".equals(object.toString().trim())
					|| "null".equals(object.toString().trim())){
				result = true ;
				break ;
			}
		}
		return result ;
	}

	public static String getRandom(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val.toLowerCase();
	}

	/**
	 * 获取随机的数值。
	 * @param length	长度
	 * @return
	 */
	public static String getRandomNum(Integer length){
		String result = "";
		Random rand = new Random();
		int n = 20;
		if(null != length && length > 0){
			n = length;
		}
		boolean[]  bool = new boolean[n];
		int randInt = 0;
		for(int i = 0; i < length ; i++) {
			do {
				randInt  = rand.nextInt(n);

			}while(bool[randInt]);

			bool[randInt] = true;
			result += randInt;
		}
		return result;
	}
	/**
	 * 一次性判断多个或单个对象不为空。
	 * @param objects
	 * @author zhou-baicheng
	 * @return 只要有一个元素不为Blank，则返回true
	 */
	public static boolean isNotBlank(Object...objects){
		return !isBlank(objects);
	}
	public static boolean isBlank(String...objects){
		Object[] object = objects ;
		return isBlank(object);
	}
	public static boolean isNotBlank(String...objects){
		Object[] object = objects ;
		return !isBlank(object);
	}
	public static boolean isBlank(String str){
		Object object = str ;
		return isBlank(object);
	}
	public static boolean isNotBlank(String str){
		Object object = str ;
		return !isBlank(object);
	}
	/**
	 * 判断一个字符串在数组中存在几个
	 * @param baseStr
	 * @param strings
	 * @return
	 */
	public static int indexOf(String baseStr,String[] strings){

		if(null == baseStr || baseStr.length() == 0 || null == strings)
			return 0;

		int i = 0;
		for (String string : strings) {
			boolean result = baseStr.equals(string);
			i = result ? ++i : i;
		}
		return i ;
	}
//	/**
//	 * 判断一个字符串是否为JSONObject,是返回JSONObject,不是返回null
//	 * @param args
//	 * @return
//	 */
//	public static net.sf.json.JSONObject isJSONObject(String args) {
//		net.sf.json.JSONObject result = null ;
//		if(isBlank(args)){
//			return result ;
//		}
//		try {
//			return net.sf.json.JSONObject.fromObject(args.trim());
//		} catch (Exception e) {
//			return result ;
//		}
//	}
//	/**
//	 * 判断一个字符串是否为JSONArray,是返回JSONArray,不是返回null
//	 * @param args
//	 * @return
//	 */
//	public static net.sf.json.JSONArray isJSONArray(Object args) {
//		JSONArray result = new JSONArray();
//		if(isBlank(args)){
//			return null ;
//		}
//		if(args instanceof  net.sf.json.JSONArray){
//
//			net.sf.json.JSONArray arr = (net.sf.json.JSONArray)args;
//			for (Object json : arr) {
//				if(json != null && json instanceof net.sf.json.JSONObject){
//					result.add(json);
//					continue;
//				}else{
//					result.add(JSONObject.fromObject(json));
//				}
//			}
//			return result;
//		}else{
//			return null ;
//		}
//
//	}
	public static String trimToEmpty(Object str){
		return (isBlank(str) ? "" : str.toString().trim());
	}

	/**
	 * 将 Strig  进行 BASE64 编码
	 * @param str [要编码的字符串]
	 * @param bf  [true|false,true:去掉结尾补充的'=',false:不做处理]
	 * @return
	 */
	public static String getBASE64(String str,boolean...bf) {
		if (StringUtils.isBlank(str)) return null;
		String base64 = new sun.misc.BASE64Encoder().encode(str.getBytes()) ;
		//去掉 '='
		if(isBlank(bf) && bf[0]){
			base64 = base64.replaceAll("=", "");
		}
		return base64;
	}

	/** 将 BASE64 编码的字符串 s 进行解码**/
	public static String getStrByBASE64(String s) {
		if (isBlank(s)) return "";
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 把Map转换成get请求参数类型,如 {"name"=20,"age"=30} 转换后变成 name=20&age=30
	 * @param map
	 * @return
	 */
	public static String mapToGet(Map<? extends Object,? extends Object> map){
		String result = "" ;
		if(map == null || map.size() ==0){
			return result ;
		}
		Set<? extends Object> keys = map.keySet();
		for (Object key : keys ) {
			result += ((String)key + "=" + (String)map.get(key) + "&");
		}

		return isBlank(result) ? result : result.substring(0,result.length() - 1);
	}
	/**
	 * 把一串参数字符串,转换成Map 如"?a=3&b=4" 转换为Map{a=3,b=4}
	 * @param args
	 * @return
	 */
	public static Map<String, ? extends Object> getToMap(String args){
		if(isBlank(args)){
			return null ;
		}
		args = args.trim();
		//如果是?开头,把?去掉
		if(args.startsWith("?")){
			args = args.substring(1,args.length());
		}
		String[] argsArray = args.split("&");

		Map<String,Object> result = new HashMap<String,Object>();
		for (String ag : argsArray) {
			if(!isBlank(ag) && ag.indexOf("=")>0){

				String[] keyValue = ag.split("=");
				//如果value或者key值里包含 "="号,以第一个"="号为主 ,如  name=0=3  转换后,{"name":"0=3"}, 如果不满足需求,请勿修改,自行解决.

				String key = keyValue[0];
				String value = "" ;
				for (int i = 1; i < keyValue.length; i++) {
					value += keyValue[i]  + "=";
				}
				value = value.length() > 0 ? value.substring(0,value.length()-1) : value ;
				result.put(key,value);

			}
		}

		return result ;
	}

	/**
	 * 转换成Unicode
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		String as[] = new String[str.length()];
		String s1 = "";
		for (int i = 0; i < str.length(); i++) {
			int v = str.charAt(i);
			if(v >=19968 && v <= 171941){
				as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
				s1 = s1 + "\\u" + as[i];
			}else{
				s1 = s1 + str.charAt(i);
			}
		}
		return s1;
	}
	/**
	 * 合并数据
	 * @param v
	 * @return
	 */
	public static String merge(Object...v){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			sb.append(v[i]);
		}
		return sb.toString() ;
	}
	/**
	 * 字符串转urlcode
	 * @param value
	 * @return
	 */
	public static String strToUrlcode(String value){
		try {
			value = java.net.URLEncoder.encode(value,"utf-8");
			return value ;
		} catch (UnsupportedEncodingException e) {
			LogUtils.getLogger().error("字符串转换为URLCode失败,value:" + value,e);
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * urlcode转字符串
	 * @param value
	 * @return
	 */
	public static String urlcodeToStr(String value){
		try {
			value = java.net.URLDecoder.decode(value,"utf-8");
			return value ;
		} catch (UnsupportedEncodingException e) {
            LogUtils.getLogger().error("URLCode转换为字符串失败;value:" + value,e);
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 判断字符串是否包含汉字
	 * @param txt
	 * @return
	 */
	public static Boolean containsCN(String txt){
		if(isBlank(txt)){
			return false;
		}
		for (int i = 0; i < txt.length(); i++) {

			String bb = txt.substring(i, i + 1);

			boolean cc = Pattern.matches("[\u4E00-\u9FA5]", bb);
			if(cc)
				return cc ;
		}
		return false;
	}
	/**
	 * 去掉HTML代码
	 * @param news
	 * @return
	 */
	public static String removeHtml(String news) {
		String s = news.replaceAll("amp;", "").replaceAll("<","<").replaceAll(">", ">");

		Pattern pattern = Pattern.compile("<(span)?\\sstyle.*?style>|(span)?\\sstyle=.*?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(s);
		String str = matcher.replaceAll("");

		Pattern pattern2 = Pattern.compile("(<[^>]+>)",Pattern.DOTALL);
		Matcher matcher2 = pattern2.matcher(str);
		String strhttp = matcher2.replaceAll(" ");


		String regEx = "(((http|https|ftp)(\\s)*((\\:)|：))(\\s)*(//|//)(\\s)*)?"
				+ "([\\sa-zA-Z0-9(\\.|．)(\\s)*\\-]+((\\:)|(:)[\\sa-zA-Z0-9(\\.|．)&%\\$\\-]+)*@(\\s)*)?"
				+ "("
				+ "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])"
				+ "|([\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*)*[\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*[\\sa-zA-Z]*"
				+ ")"
				+ "((\\s)*(\\:)|(：)(\\s)*[0-9]+)?"
				+ "(/(\\s)*[^/][\\sa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
		Pattern p1 = Pattern.compile(regEx,Pattern.DOTALL);
		Matcher matchhttp = p1.matcher(strhttp);
		String strnew = matchhttp.replaceAll("").replaceAll("(if[\\s]*\\(|else|elseif[\\s]*\\().*?;", " ");


		Pattern patterncomma = Pattern.compile("(&[^;]+;)",Pattern.DOTALL);
		Matcher matchercomma = patterncomma.matcher(strnew);
		String strout = matchercomma.replaceAll(" ");
		String answer = strout.replaceAll("[\\pP‘’“”]", " ")
				.replaceAll("\r", " ").replaceAll("\n", " ")
				.replaceAll("\\s", " ").replaceAll("　", "");


		return answer;
	}
	/**
	 * 把数组的空数据去掉
	 * @param array
	 * @return
	 */
	public static List<String> array2Empty(String[] array){
		List<String> list = new ArrayList<String>();
		for (String string : array) {
			if(StringUtils.isNotBlank(string)){
				list.add(string);
			}
		}
		return list;
	}
	/**
	 * 把数组转换成set
	 * @param array
	 * @return
	 */
	public static Set<?> array2Set(Object[] array) {
		Set<Object> set = new TreeSet<Object>();
		for (Object id : array) {
			if(null != id){
				set.add(id);
			}
		}
		return set;
	}
	/**
	 * serializable toString
	 * @param serializable
	 * @return
	 */
	public static String toString(Serializable serializable) {
		if(null == serializable){
			return null;
		}
		try {
			return (String)serializable;
		} catch (Exception e) {
			return serializable.toString();
		}
	}

	public static String genUuid(){
		UUID uuid=UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr=str.replace("-", "");
		return uuidStr;
	}

	public static String genRandomNumStr(int len){
		StringBuilder code = new StringBuilder(6);
		Random r = new Random();
		for (int i = 0; i < len; i++) {
			code.append((char)('0'+r.nextInt(10)));
		}
		return code.toString();
	}

	public static String upperCaseFirstLetter(String s){
	    if (StringUtils.isBlank(s)){
	        return s;
        }
        char[] chars = s.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    public static String lowerCaseFirstLetter(String s){
        if (StringUtils.isBlank(s)){
            return s;
        }
        char[] chars = s.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] = (char)(chars[0] + 32);
        }
        return new String(chars);
    }
}
