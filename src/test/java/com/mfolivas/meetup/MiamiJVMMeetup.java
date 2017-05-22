package com.mfolivas.meetup;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcelo Olivas
 */
public class MiamiJVMMeetup {
    private List<MeetupUser> users = new ArrayList<>();
    private List<UserAddedToMeetupListener> listeners = new ArrayList<>();

    public void addUser(MeetupUser user) {
        //Add user to the meetup list
        this.users.add(user);

        //Notify the list of registered users
        notifyUsersAddedListeners(user);
    }

    public void registerUserAddedListener(UserAddedToMeetupListener listener) {
        //Add the listener to the list of registered listeners
        this.listeners.add(listener);
    }

    public void unregisterUserAddedListener(UserAddedToMeetupListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyUsersAddedListeners(MeetupUser meetupUser) {
        this.listeners.forEach(listener -> listener.onUserAdded(meetupUser));
    }

}
