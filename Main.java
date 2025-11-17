package Project;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ================
        // CREATE VEHICLE
        // ================
        Vehicle vehicle = new Vehicle();
        System.out.println("\n--- VEHICLE REGISTRATION ---");
        vehicle.inputVehicleDetails(); 


        // ================
        // CREATE PACKAGE
        // ================
        Package pkg = new Package();
        System.out.println("\n--- PACKAGE CREATION ---");
        pkg.readPackageDetails();       


        // ================
        // ASSIGN PACKAGE
        // ================
        vehicle.assignPackageWithValidation(pkg, input);


    }

    // Logo 
    public static void displayMS()
    {

        System.out.println("                                       --------------------------------------------------------  ");
        System.out.println("                                       **********     ***** ***    *** *****      *****                 ");
        System.out.println("                                       ********* *    *****  **    **  *****     **       ");
        System.out.println("                                       ***      * *   *****    ** **   *****       ***       ");
        System.out.println("                                       ***       * *  *****      *     *****         *** ");
        System.out.println("                                       ***       * *  *****            *****           **");
        System.out.println("                                       ********** *   *****            *****          **");
        System.out.println("                                       ***********   *****            *****     ****");
        System.out.println("                                       -------------------------------------------------------- ");
        System.out.println("                                                    ----Delivery Management System---- ");
        System.out.println();
    }
}
