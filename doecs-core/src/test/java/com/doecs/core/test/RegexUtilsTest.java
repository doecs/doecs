package com.doecs.core.test;

import com.doecs.core.utils.RegexUtils;
import com.doecs.core.utils.StringUtils;
import org.junit.Test;

public class RegexUtilsTest {
    @Test
    public void getMatch() {
        String s= RegexUtils.getMatch("^[1-9]\\d*", "123aa33");
        System.out.println(s);
    }

    @Test
    public void getFirstInt() {
        System.out.println(RegexUtils.getFirstInt("bb123aa33"));
    }

    @Test
    public void lowerCaseFirstLetter() {
        System.out.println(StringUtils.lowerCaseFirstLetter("Acdfasdfa1232_a士大夫"));
        System.out.println(StringUtils.upperCaseFirstLetter("scdfasdfa1232_a士大夫"));
    }
}
