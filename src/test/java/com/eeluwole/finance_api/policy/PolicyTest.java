package com.eeluwole.finance_api.policy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class PolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Policy.class).verify();
    }
}
