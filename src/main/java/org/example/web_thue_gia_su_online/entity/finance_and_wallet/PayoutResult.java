package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

public record PayoutResult(boolean successful, String providerReference, String failureReason) {}
