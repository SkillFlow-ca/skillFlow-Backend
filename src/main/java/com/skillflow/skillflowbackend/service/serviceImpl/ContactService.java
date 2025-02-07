package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Contact;
import com.skillflow.skillflowbackend.repository.ContactRepository;
import com.skillflow.skillflowbackend.service.ContactIService;
import com.skillflow.skillflowbackend.utility.EmailUtility;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService implements ContactIService {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailUtility emailUtility;
    @Override
    public Contact saveContact(Contact contact) {
        contact.setCreatedAt(Instant.now());
        return contactRepository.save(contact);
    }

    @Override
    public Contact getContactById(long id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseModel<Contact> getContacts(Pageable pageable) {
        Page<Contact> contact = contactRepository.findAll(pageable);
        return buildResponse(contact);
    }

    @Override
    public Contact updateWithReplyContact(String subject,String message, long idContact) throws MessagingException {
        Contact contact1 = contactRepository.findById(idContact).orElse(null);
        contact1.setAnswered(true);
        contactRepository.save(contact1);
        emailUtility.sendSimpleEmail(contact1.getEmail(),subject,message);
        return contactRepository.save(contact1);
    }

    private ResponseModel<Contact> buildResponse(Page<Contact> contact) {
        List<Contact> listContact = contact.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<Contact>builder()
                .pageNo(contact.getNumber())
                .pageSize(contact.getSize())
                .totalElements(contact.getTotalElements())
                .totalPages(contact.getTotalPages())
                .data(listContact)
                .isLastPage(contact.isLast())
                .build();
    }

}
