package com.complex.utils;

import java.util.Random;

public class RandomID {
    public static int getCPUID(){
        int max = 6;
        int min = 1;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getCPUNum5(){
        int max = 50;
        int min = 40;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getCPUNum4(){
        int max = 40;
        int min = 30;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getCPUNum3(){
        int max = 30;
        int min = 20;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getCPUNum2(){
        int max = 20;
        int min = 10;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getCPUNum(){
        int max = 10;
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int genID(){
        int max = 10;
        int min = 2;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static String genID2(){
        int max = 2000;
        int min = 100;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    public static String genIDWorker(){
        int max = 30000;
        int min = 20001;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

}
