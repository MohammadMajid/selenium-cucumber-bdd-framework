package com.seleniumcucumberbddframework.utlis;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class RandomTestData extends WebElementUtils {

    public Random rnd = new Random();

    final private static Integer NAME = 5;

    public String randomName(){
        return "B" + RandomStringUtils.randomAlphabetic(NAME);
    }

    public String FirstNameWithA(){
        return "A" + RandomStringUtils.randomAlphabetic(NAME);
    }

}
