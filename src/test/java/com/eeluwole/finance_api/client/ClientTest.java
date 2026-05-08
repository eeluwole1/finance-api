package com.eeluwole.finance_api.client;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Client.class).verify();
    }
}
