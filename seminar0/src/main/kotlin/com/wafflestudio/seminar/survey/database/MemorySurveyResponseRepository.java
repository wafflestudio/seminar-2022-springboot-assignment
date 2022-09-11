package com.wafflestudio.seminar.survey.database;

import com.wafflestudio.seminar.survey.domain.SurveyResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MemorySurveyResponseRepository implements SurveyResponseRepository {
    private final MemoryDB db;
    
    public MemorySurveyResponseRepository(MemoryDB db) {
        this.db = db;
    }

    @NotNull
    @Override
    public List<SurveyResponse> findAll() {
        return this.db.getSurveyResponses();
    }

    @NotNull
    @Override
    public Optional<SurveyResponse> findById(long id) {
        return this.db.getSurveyResponses().stream()
                .filter(surveyResponse -> Objects.equals(surveyResponse.getId(), id))
                .findFirst();
    }
}
