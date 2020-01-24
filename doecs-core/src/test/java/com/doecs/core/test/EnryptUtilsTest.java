package com.doecs.core.test;

import com.doecs.core.utils.ByteUtils;
import com.doecs.core.utils.EncryptUtils;
import org.junit.Test;

import java.io.IOException;

public class EnryptUtilsTest {
    @Test
    public void test() {
        String s = EncryptUtils.safeUrlBase64Encode("{\"name\":\"NoticeDetails\",\"params\":{\"noticeId\":\"123\"}}");

        System.out.println(s);

        try {
            String ss = ByteUtils.bytesToStr(EncryptUtils.safeUrlBase64Decode(s), "UTF-8");
            System.out.println(ss);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
