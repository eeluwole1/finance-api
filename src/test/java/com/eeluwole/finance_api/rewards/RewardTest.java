package com.eeluwole.finance_api.rewards;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class RewardTest {

    @Test
    void equalsVerifier() throws Exception {
        EqualsVerifier.forClass(Reward.class).verify();
    }
}
