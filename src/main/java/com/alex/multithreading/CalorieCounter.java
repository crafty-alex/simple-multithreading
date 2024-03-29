package com.alex.multithreading;

public class CalorieCounter {

    private int totalCalories = 0;

    public synchronized void consumeCalories(String food, int calories) {
        System.out.println("Attempting to consume: " + food);
        while (totalCalories + calories > 1000) {
            try {
                System.out.println("Too many calories! Waiting to exercise.");
                wait(); // Wait until calories are burned
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                return;
            }
        }
        totalCalories += calories;
        System.out.println("Consumed " + food + ", Total Calories: " + totalCalories);
    }

    public synchronized void burnCalories(String activity, int calories) {
        System.out.println("Starting to burn calories by " + activity);
        totalCalories -= calories;
        System.out.println("Burned calories with " + activity + ", Total Calories: " + totalCalories);
        notifyAll(); // Notify that calories have been burned
    }

}
