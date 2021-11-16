package com.psi.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class TestRandom {

    @Test
    public void rwe() {
        for (int i = 2; i < 20; i++) {
            double w = Math.random() * 1000000;//0.001
            System.out.println("W_"+w);
            String s = (long) (Math.random() * 1000000) + "";
            System.out.println("S_"+s);
            String s2 = Math.random()  + "";
            String substring = s2.substring(s2.length() - 6);
            System.out.println("sub_"+substring);

            long v = (long) (Math.random() * 1000000);
            System.out.println("V_"+v);
        }
    }

    @Test
    public void ewe(){
        Random r=new Random();

    }

}
