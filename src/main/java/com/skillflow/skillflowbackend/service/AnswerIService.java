package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Answer;

import java.util.List;

public interface AnswerIService {
    public Answer addAnswerAndAssignToQuestion(Answer answer,long idQuestion);
    public Answer updateAnswer(Answer answer,long idAnswer);
    public Answer deleteAnswer(long idAnswer);
    public List<Answer> getAnswersByQuestionId(long idQuestion);
    public Answer updateAnswerToAddAvote(long idAnswer,String vote);

}
