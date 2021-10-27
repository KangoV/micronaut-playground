package io.belldj.pg.mn;

import io.micronaut.runtime.Micronaut;

public class Application {






    public static void main(String[] args) {


        String name = "darren1";
        if (name.equals("darren")) {
            System.out.println("yay");
        } else {
            System.out.println("boo");
        }

        for (int i=1; i<=10; i++) {
            System.out.println("loop: " + i);
        }



        //Micronaut.run(Application.class, args);
    }
}
