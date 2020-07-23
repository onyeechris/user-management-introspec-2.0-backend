package com.activedge.usermgt;

import java.util.PriorityQueue;

public class Demo {
    public static void main(String[] args) {
        String str = "acca";

        String lowestString = null;

        PriorityQueue<String> pQueue = new PriorityQueue<String>();

        for(int j=0; j < str.length(); j++) {
            char[] strArr = str.toCharArray();
            for(int i=97; i < 123; i++) {
                if(i < str.charAt(j)) {
                    strArr[j] = (char) i;
                    // push new string
                    String newString = new String(strArr);
                    if(!isPalindrome(newString)) {
                        pQueue.add(newString);
                    }
                }
                if(str.charAt(j) <= i) break;
            }
        }

        if(pQueue.isEmpty()) {
            System.out.println("Smallest string is IMPOSSIBLE");
        } else {
            System.out.println("Smallest string is " + pQueue.poll());
        }

//        while(!pQueue.isEmpty()) {
//
//            System.out.println(pQueue.poll());
//        }

//        String str2 = "aaaabaaa";
//
//        int a = 'a'; // 97
//        int z = 'z'; // 122
//
//        char aa =  120;
//
//        System.out.println(a);
//        System.out.println(aa);
//        System.out.println(z);
//
//        System.out.println("str < str2: " + str.compareTo(str2));
//
//        System.out.printf("Is string[%s] a palindrome? %s", str, isPalindrome(str));
    }

    public static String varr() {
        String str = "acca";

        String newStr = "";

        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) < 97 || str.charAt(i) > 122 ) continue; // throw exception
        }

        return newStr;
    }


    public static boolean isPalindrome(String text) {
        String clean = text.replaceAll("\\s+", "").toLowerCase();
        int length = clean.length();
        int forward = 0;
        int backward = length - 1;
        while (backward > forward) {
            char forwardChar = clean.charAt(forward++);
            char backwardChar = clean.charAt(backward--);
            if (forwardChar != backwardChar)
                return false;
        }
        return true;
    }
}
