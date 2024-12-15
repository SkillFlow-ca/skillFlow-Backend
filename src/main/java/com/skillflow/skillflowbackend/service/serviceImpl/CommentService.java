package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Answer;
import com.skillflow.skillflowbackend.model.CommentAnswer;
import com.skillflow.skillflowbackend.repository.AnswerRepository;
import com.skillflow.skillflowbackend.repository.CommentRepository;
import com.skillflow.skillflowbackend.service.CommentIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CommentService implements CommentIService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private SessionService sessionService;

    @Override
    public CommentAnswer addCommentAndAssingToAnswer(CommentAnswer commentAnswer, long idAnswer) {
        Answer answer=answerRepository.findById(idAnswer).get();
        commentAnswer.setUser(sessionService.getUserBySession().get());
        commentAnswer.setAnswer(answer);
        commentAnswer.setCreatedAt(Instant.now());
        commentAnswer.setIsDeleted(false);
        return commentRepository.save(commentAnswer);
    }
}
