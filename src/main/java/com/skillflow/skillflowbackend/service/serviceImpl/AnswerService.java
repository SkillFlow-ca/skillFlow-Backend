package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Answer;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.repository.AnswerRepository;
import com.skillflow.skillflowbackend.repository.QuestionRepository;
import com.skillflow.skillflowbackend.service.AnswerIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnswerService implements AnswerIService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SessionService sessionService;
    @Override
    public Answer addAnswerAndAssignToQuestion(Answer answer, long idQuestion) {
        Question question = questionRepository.findById(idQuestion).get();
        User user=this.sessionService.getUserBySession().get();
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setCreatedAt(Instant.now());
        answer.setIsDeleted(false);
        answerRepository.save(answer);
        return answer;
    }

    @Override
    public Answer updateAnswer(Answer answer, long idAnswer) {
        Answer answer1 = answerRepository.findById(idAnswer).get();
        User user=this.sessionService.getUserBySession().get();
        answer.setUser(user);
        answer1.setUpdatedAt(Instant.now());
        answer1.setContent(answer.getContent());
        return answerRepository.save(answer1);
    }

    @Override
    public Answer deleteAnswer(long idAnswer) {
        Answer answer= answerRepository.findById(idAnswer).get();
        answer.setIsDeleted(true);
        return answer;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(long idQuestion) {
        Question question = questionRepository.findById(idQuestion).get();
        return answerRepository.getAnswersByQuestionId(question.getIdQuestion());
    }
}
