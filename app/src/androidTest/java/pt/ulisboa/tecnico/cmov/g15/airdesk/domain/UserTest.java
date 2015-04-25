package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import junit.framework.TestCase;

/**
 * Created by joao on 25-04-2015.
 */
public class UserTest extends TestCase {
    User user1;

    @Override
    protected void setUp() {
        user1 = new User("someEmail");
    }

    public void testEquals() {
        // two users are equal if their mails are equal
        User user2 = new User("someEmail");
        User user3 = new User("otherEmail");
        assert(user1.equals(user2));
        assertFalse(user1.equals(user3));
    }
}
