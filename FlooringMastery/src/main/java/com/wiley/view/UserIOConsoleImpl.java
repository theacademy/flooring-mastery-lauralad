package com.wiley.view;

import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
    Scanner sc = new Scanner(System.in);

    @Override
    public void print(String message){
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        String stringInput = sc.nextLine();

        return stringInput;
    }

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        int intInput = Integer.parseInt(sc.nextLine());

        return intInput;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int intInput = 0;
        do {
            System.out.println(prompt);
            intInput = Integer.parseInt(sc.nextLine());
        }while(intInput < min || intInput > max);

        return intInput;
    }

    @Override
    public double readDouble(String prompt) {
        System.out.println(prompt);
        double doubleInput = Double.parseDouble(sc.nextLine());

        return doubleInput;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double doubleInput = 0.0;
        do {
            System.out.println(prompt);
            doubleInput = Double.parseDouble(sc.nextLine());
        }while(doubleInput < min || doubleInput > max);

        return doubleInput;
    }

    @Override
    public float readFloat(String prompt) {
        System.out.println(prompt);
        float floatInput = Float.parseFloat(sc.nextLine());

        return floatInput;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float floatInput = 0;
        do {
            System.out.println(prompt);
            floatInput = Float.parseFloat(sc.nextLine());
        }while(floatInput < min || floatInput > max);

        return floatInput;
    }

    @Override
    public long readLong(String prompt) {
        System.out.println(prompt);
        long longInput = Long.parseLong(sc.nextLine());

        return longInput;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long longInput = 0;
        do {
            System.out.println(prompt);
            longInput = Long.parseLong(sc.nextLine());

        } while (longInput < min || longInput > max);

        return longInput;
    }
}
