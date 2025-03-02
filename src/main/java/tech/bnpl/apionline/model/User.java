package tech.bnpl.apionline.model;

public class User {
    public String name; // ❌ BAD PRACTICE
    public String email;

    public void createUser(String name, String email, String phone, String address, String country, String zip, String dob) {
        // ❌ Too many parameters
    }
}
