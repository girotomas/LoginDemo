package com.tomasgiro.datademo.logindemo;

import android.util.Log;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Pattern pattern = Pattern.compile(".*<(.*?)>.*");
        System.out.println(pattern.pattern());
        Matcher matcher = pattern.matcher("adsfafd <sdgd dfg>");
       // String queryName = matcher.matches();
        matcher.find();
        System.out.println("hi: queryName= "+matcher.group(1));
        assertEquals(4, 2 + 2);
    }
}