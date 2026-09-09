package com.eeluwole.finance_api.common;

import java.math.BigDecimal;

/**
 * Centralized application-wide constants. Keep magic numbers/strings that
 * carry business meaning here instead of scattering literals across
 * services — one place to find and change them.
 */
public final class AppConstants {

    private AppConstants() {
    }

    /** Maximum balance a single account may hold, enforced on deposit. */
    public static final BigDecimal MAX_ACCOUNT_BALANCE = new BigDecimal("1000000");

    /** Authorization header scheme this API expects Bearer tokens under. */
    public static final String JWT_HEADER_PREFIX = "Bearer ";
}
