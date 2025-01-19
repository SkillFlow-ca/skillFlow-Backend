package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Contact;
import com.skillflow.skillflowbackend.model.Job;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;

public interface ContactIService {
    public Contact saveContact(Contact contact);
    public Contact getContactById(long id);
    ResponseModel<Contact> getContacts(Pageable pageable);
    public Contact updateWithReplyContact(String subject,String message,long idContact) throws MessagingException;
}
