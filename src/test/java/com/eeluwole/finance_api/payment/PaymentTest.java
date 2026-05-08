package com.eeluwole.finance_api.payment;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Payment.class).verify();
    }
}
