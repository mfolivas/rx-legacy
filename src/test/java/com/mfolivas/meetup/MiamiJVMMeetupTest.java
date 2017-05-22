package com.mfolivas.meetup;

import org.junit.Test;

/**
 * @author Marcelo Olivas
 */
public class MiamiJVMMeetupTest {

    @Test
    public void shouldSubscribeAParticularUserToAMeetupAnBeNotified() {
        MiamiJVMMeetup meetup = new MiamiJVMMeetup();
        meetup.registerUserAddedListener(new PrintUserNameAddedListener());
        meetup.registerUserAddedListener(new EmailUserOnNewTalkAddedListener("RxJava"));

        MeetupUser user = new MeetupUser("Marcelo", "Olivas");

        meetup.addUser(user);
    }
}
