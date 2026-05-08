package com.eeluwole.finance_api.advanced;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AdvancedTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Advanced.class).verify();
    }
}
