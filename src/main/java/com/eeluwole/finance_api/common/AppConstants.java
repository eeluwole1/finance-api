package com.eeluwole.finance_api.common;

/**
 * Centralized application-wide constants. Keep magic numbers/strings that
 * carry business meaning here instead of scattering literals across
 * services — one place to find and change them.
 */
public final class AppConstants {

    private AppConstants() {
    }

    /** Maximum balance a single account may hold, enforced on deposit. */
    public static final double MAX_ACCOUNT_BALANCE = 1_000_000;

    /** Authorization header scheme this API expects Bearer tokens under. */
    public static final String JWT_HEADER_PREFIX = "Bearer ";
}
