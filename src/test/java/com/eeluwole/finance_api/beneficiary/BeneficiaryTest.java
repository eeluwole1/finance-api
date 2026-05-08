package com.eeluwole.finance_api.beneficiary;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class BeneficiaryTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Beneficiary.class).verify();
    }
}
