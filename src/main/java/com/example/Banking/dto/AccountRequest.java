package com.example.Banking.dto;

import java.math.BigDecimal;

public class AccountRequest {
    private Long customerId;
    private BigDecimal initialDeposit;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public BigDecimal getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(BigDecimal initialDeposit) { this.initialDeposit = initialDeposit; }
}
