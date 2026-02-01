package com.SoftwareOrdersUberEats.userService.service;


import com.SoftwareOrdersUberEats.userService.interfaces.IMappedDiagnostic;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import static com.SoftwareOrdersUberEats.userService.constant.TracerConstants.CORRELATION_KEY;

@NoArgsConstructor
@Service
public class MappedDiagnosticService implements IMappedDiagnostic {

    public void setIdCorrelation(String correlationId){
        try {
            MDC.put(CORRELATION_KEY, correlationId);
        } finally {
            MDC.remove(CORRELATION_KEY);
        }
    }

    public String getIdCorrelation(){
        return MDC.get(CORRELATION_KEY);
    }
}