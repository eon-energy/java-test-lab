package com.technokratos.service.sub;

import com.technokratos.entity.internal.SolutionStatus;

public interface SolutionStatusService {
    SolutionStatus findByCode(String code);
}
