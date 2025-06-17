package com.technokratos.service.sub.impl;

import com.technokratos.entity.internal.SolutionStatus;
import com.technokratos.exception.solutionStatusServiceException.SolutionStatusNotFoundException;
import com.technokratos.repository.SolutionStatusRepository;
import com.technokratos.service.sub.SolutionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionStatusServiceImpl implements SolutionStatusService {
    private final SolutionStatusRepository solutionRepository;


    @Override
    public SolutionStatus findByCode(String code) {
        return solutionRepository.findByCode(code)
                .orElseThrow(SolutionStatusNotFoundException::new);
    }
}
