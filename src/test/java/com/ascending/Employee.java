/*
 *  Copyright 2019, Liwei Wang <daveywang@live.com>.
 *  All rights reserved.
 *  Author: Liwei Wang
 *  Date: 10/2019
 */

package com.ascending;

import java.lang.Comparable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.io.*;

public class Employee implements Comparable<Object>, Comparator<Employee>, Serializable {
    private static final long serialVersionUID = 1234567890L;
    private int id;
    private transient int ssn;
    //private int ssn;
    private String firstName;
    private String lastName;
    private String address;



    public Employee(int id, int ssn, String firstName, String lastName) {
        this.id = id;
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int compare(Employee e1, Employee e2) {
        return e1.id - e2.id;
    }

    public int compareTo(Object o) {
        Employee e = (Employee)o;
        return this.id - e.id;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        firstName = encrypt(firstName);
        lastName = encrypt(lastName);
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        firstName = deEncrypt(firstName);
        lastName = deEncrypt(lastName);
    }

    public String toString() {
        return "[Person: " + id + ", SSN: " + ssn + ", firstName: " + firstName + ", lastName: " + lastName + "]";
    }

    private String encrypt(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        return encryptedText;
    }

    private String deEncrypt(String encrypedText) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(encrypedText);
        return new String(decodedByteArray);
    }

    public static void main(String[] args) {
        Employee e1 = new Employee(1, 11111, "David", "Wang");
        Employee e2 = new Employee(2, 22222, "Ryo", "Hang");
        Employee e3 = null;
        Employee e4 = null;

        System.out.println("e1= " + e1 + "\ne2= " + e2);

        //Serialization

        try {
            FileOutputStream fileOut = new FileOutputStream("employee.ser");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(e1);
            objectOut.writeObject(e2);
            objectOut.close();
            fileOut.close();
            System.out.println("\nThe object e1 and e2 are serialized.");
        }
        catch(IOException ioex) {
            ioex.printStackTrace();
        }

        //Deserialization
        try {
            FileInputStream fileIn = new FileInputStream("employee.ser");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            e3 = (Employee)objectIn.readObject();
            e4 = (Employee)objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("\nThe object e3 and e4 are created from serialized objects.");
            System.out.println("e3= " + e3 + "\ne4= " + e4);
        }
        catch(IOException ioex) {
            ioex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}