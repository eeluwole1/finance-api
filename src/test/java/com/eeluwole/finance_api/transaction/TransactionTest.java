package com.eeluwole.finance_api.transaction;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Transaction.class).verify();
    }
}
