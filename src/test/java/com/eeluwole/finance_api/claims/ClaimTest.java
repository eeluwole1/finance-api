package com.eeluwole.finance_api.claims;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ClaimTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Claim.class).verify();
    }
}
