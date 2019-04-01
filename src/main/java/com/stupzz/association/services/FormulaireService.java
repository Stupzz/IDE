package com.stupzz.association.services;

import com.stupzz.association.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulaireService {
    @Autowired
    private TeamRepository teamRepository;

}
