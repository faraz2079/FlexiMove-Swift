package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillingInfo {
    private String billingModel;
    private double rate;
}



