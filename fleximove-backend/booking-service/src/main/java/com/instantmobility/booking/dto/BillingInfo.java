package com.instantmobility.booking.dto;

import lombok.Data;
<<<<<<< HEAD

@Data
=======
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Getter
@NoArgsConstructor
>>>>>>> 4776a39 (add latest changes)
public class BillingInfo {
    private String billingModel;
    private double rate;
}



