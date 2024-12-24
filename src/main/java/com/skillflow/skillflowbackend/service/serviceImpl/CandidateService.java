package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.Candidate;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.enume.CandidateStatus;
import com.skillflow.skillflowbackend.repository.CandidateRepository;
import com.skillflow.skillflowbackend.repository.JobRepository;
import com.skillflow.skillflowbackend.service.CandidateIService;
import com.skillflow.skillflowbackend.utility.EmailUtility;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CandidateService implements CandidateIService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private EmailUtility emailUtility;
    @Override
    public Candidate addCandidateASSignToJob(Candidate candidate, long idJob) throws MessagingException {
        Job job=jobRepository.findById(idJob).get();
        candidate.setJob(job);
        candidate.setCreatedAt(Instant.now());
        candidate.setTrackCode(generateRandomCode());
        candidate.setStatus(CandidateStatus.ApplicationSubmitted);
        emailUtility.sendApplicationConfirmationEmail(candidate.getEmail(), candidate.getFirstName(), candidate.getLastName(),candidate.getTrackCode(),candidate.getJob().getTitle());
        return candidateRepository.save(candidate);
    }
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public Candidate updateCandidateToAddResume(long idCandidate, MultipartFile resume) throws IOException {
        Optional<Candidate> candidateOptional = candidateRepository.findById(idCandidate);

        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setResume(resume.getBytes());
            return candidateRepository.save(candidate); // Save and return updated candidate
        } else {
            throw new NoSuchElementException("Candidate not found with the given ID.");
        }
    }

    @Override
    public void deleteCandidate(long idCandidate) {
        candidateRepository.deleteById(idCandidate);
    }

    @Override
    public List<Candidate> getCandidatesByJob(long idJob) {
        Job job = jobRepository.findById(idJob).get();
        List<Candidate> candidates = job.getCandidateList();
        return candidates;
    }

    @Override
    public Candidate getCandidateByTrackCode(String trackCode) {
        Candidate candidate = candidateRepository.findByTrackCode(trackCode);
        return candidate;
    }


}
