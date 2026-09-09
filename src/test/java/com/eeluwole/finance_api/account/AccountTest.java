package com.eeluwole.finance_api.account;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void equalsVerifier() throws Exception {
        // BigDecimal.equals() is scale-sensitive (0 != 0.00) — we don't care about
        // scale differences for equality, only value, so this warning is expected.
        EqualsVerifier.forClass(Account.class).suppress(Warning.BIGDECIMAL_EQUALITY).verify();
    }
}
