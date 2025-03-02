package tech.bnpl.apionline.core;

public class Singleton {
    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(); // ❌ BAD PRACTICE
        }
        return instance;
    }
}