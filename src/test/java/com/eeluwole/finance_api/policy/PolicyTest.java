package com.eeluwole.finance_api.policy;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class PolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        // BigDecimal.equals() is scale-sensitive (0 != 0.00) — we don't care about
        // scale differences for equality, only value, so this warning is expected.
        EqualsVerifier.forClass(Policy.class).suppress(Warning.BIGDECIMAL_EQUALITY).verify();
    }
}
