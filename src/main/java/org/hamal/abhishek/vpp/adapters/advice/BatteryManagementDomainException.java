package org.hamal.abhishek.vpp.adapters.advice;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BatteryManagementDomainException extends IllegalArgumentException {

    public BatteryManagementDomainException(String message) {
        super(message);
    }

    public BatteryManagementDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
