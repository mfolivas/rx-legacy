package com.mfolivas.meetup;

/**
 * @author Marcelo Olivas
 */
public class EmailUserOnNewTalkAddedListener implements UserAddedToMeetupListener {

    private String talk;

    public EmailUserOnNewTalkAddedListener(String talk) {
        this.talk = talk;
    }

    @Override
    public void onUserAdded(MeetupUser user) {
        System.out.println("Sent e-maiil to: " + user + " about the talk on " + talk);
    }
}
