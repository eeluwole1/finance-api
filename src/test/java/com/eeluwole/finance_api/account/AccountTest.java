package com.eeluwole.finance_api.account;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Account.class).verify();
    }
}
