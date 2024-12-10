package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.enume.QuestionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionIService {
    public Question addQuestion(Question question);
    public ResponseModel<Question> myQuestions(Pageable pageable);
    public Question updateQuestion(Question question,long idQuestion);
    public Question deleteQuestion(long idQuestion);
    public Question getQuestionById(long idQuestion);
    public int countQuesions();
    public ResponseModel<Question> getQuestionByContraintes(QuestionStatus questionStatus, String title, Integer views, Pageable pageable);
}
