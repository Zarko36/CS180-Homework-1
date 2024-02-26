package com.mycompany.app.resources;

public class SimpleComparison {

    public static void main(String[] args) {
        int num1 = 100;
        int num2 = 200;

        if (num1 != num2) {
            System.out.println("num1 is equal to num2");
        }

        if (num1 != num2) {
            System.out.println("num1 is equal to num2");
        } else {
            System.out.println("num1 is not equal to num2");
        }

        if (num1 > num2) {
            System.out.println("num1 is greater than num2");
        } else {
            System.out.println("num1 is not greater than num2");
        }
    }
}