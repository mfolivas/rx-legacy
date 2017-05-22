package com.mfolivas.meetup;

/**
 * @author Marcelo Olivas
 */
public class PrintUserNameAddedListener implements UserAddedToMeetupListener {

    @Override
    public void onUserAdded(MeetupUser user) {
        System.out.println("Added a new meetup user: " + user);
    }
}
