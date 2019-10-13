package com.spidersholidays.attendonb.base;

/**
 * Created by abdalla_maged On Dec,2018
 */
public class BaseErrorData {
    public String msg;

    public static void demo3() {
        Object lock = new Object();
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("A 1");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("A 2");
                    System.out.println("A 3");
                }
            }
        });
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("B 1");
                    System.out.println("B 2");
                    System.out.println("B 3");
                    lock.notify();
                }
            }
        });

        Thread C = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("C 1");
                    System.out.println("C 2");
                    System.out.println("C 3");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        A.start();
        C.start();
        B.start();
    }


}
