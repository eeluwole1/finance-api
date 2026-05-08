package com.eeluwole.finance_api.auth;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(User.class).verify();
    }
}
