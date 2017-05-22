package com.mfolivas.meetup;

/**
 * @author Marcelo Olivas
 */
public class MeetupUser {
    private final String firstName;
    private final String lastName;

    public MeetupUser(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    @Override
    public String toString() {
        return "MeetupUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
