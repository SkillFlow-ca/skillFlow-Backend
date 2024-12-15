package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.Question;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.QuestionStatus;
import com.skillflow.skillflowbackend.repository.QuestionRepository;
import com.skillflow.skillflowbackend.service.QuestionIService;
import com.skillflow.skillflowbackend.specifications.QuestionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService implements QuestionIService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SessionService sessionService;
    @Override
    public Question addQuestion(Question question) {
        User user=this.sessionService.getUserBySession().get();
        question.setUser(user);
        question.setQuestionStatus(QuestionStatus.OPEN);
        question.setViews(0);
        question.setIsDeleted(false);
        question.setCreatedAt(Instant.now());
        Question quest = questionRepository.save(question);
        return quest;
    }

    @Override
    public ResponseModel<Question> myQuestions(Pageable pageable) {
        User user=this.sessionService.getUserBySession().get();
        Page<Question> questions = questionRepository.getMyQuestions(user.getIdUser(), pageable);
        return buildResponse(questions);
    }

    @Override
    public Question updateQuestion(Question question, long idQuestion) {
        Question question1 = questionRepository.findById(idQuestion).get();
        User user=this.sessionService.getUserBySession().get();
        question1.setUser(user);
        question1.setTitle(question.getTitle());
        question1.setProblemDetails(question.getProblemDetails());
        question1.setWhatUTried(question.getWhatUTried());
        question1.setTags(question.getTags());
        question1.setUpdatedAt(Instant.now());
        question1.setAnswerList(question.getAnswerList());
        question1.setUpdatedAt(Instant.now());
        return questionRepository.save(question1);
    }

    @Override
    public Question deleteQuestion(long idQuestion) {
        Question question = questionRepository.findById(idQuestion).get();
        question.setIsDeleted(true);
        return questionRepository.save(question);
    }

    @Override
    public Question getQuestionById(long idQuestion) {
        return questionRepository.findById(idQuestion).get();
    }

    @Override
    public int countQuesions() {
        return (int) questionRepository.countNotDeleted();
    }

    @Override
    public ResponseModel<Question> getQuestionByContraintes(QuestionStatus questionStatus,String title,Integer views, Pageable pageable) {
        final Specification<Question> specification= QuestionSpecification.searchQuestionByManyConditions(questionStatus,title,views);
        Page<Question> questions = questionRepository.findAll(specification, pageable);
        return buildResponse(questions);
    }

    @Override
    public Question getMyQuestionById(long idQuestion) {
        User user = this.sessionService.getUserBySession().get();
        return questionRepository.findById(idQuestion)
                .filter(question -> question.getUser().getIdUser() == user.getIdUser())
                .orElse(null);
    }

    @Override
    public Question updateStatusQuestion(long idQuestion, QuestionStatus questionStatus) {
        Question question = questionRepository.findById(idQuestion).orElseThrow(() -> new RuntimeException("Question not found"));
        question.setQuestionStatus(questionStatus);
        question.setUpdatedAt(Instant.now());
        return questionRepository.save(question);
    }
    private ResponseModel<Question> buildResponse(Page<Question> question) {
        List<Question> listQuestions = question.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Question>builder()
                .pageNo(question.getNumber())
                .pageSize(question.getSize())
                .totalElements(question.getTotalElements())
                .totalPages(question.getTotalPages())
                .data(listQuestions)
                .isLastPage(question.isLast())
                .build();
    }
}
