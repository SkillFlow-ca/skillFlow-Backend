package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Contact;
import com.skillflow.skillflowbackend.service.ContactIService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/contact/")
@Validated
public class ContactController {
    @Autowired
    private ContactIService contactIService;


    @PostMapping("save")
    public Contact saveContact(@Validated @RequestBody Contact contact) {
        return contactIService.saveContact(contact);
    }
    @GetMapping("all")
    public ResponseEntity<ResponseModel<Contact>> getContacts(Pageable pageable) {
        return ResponseEntity.ok(contactIService.getContacts(pageable));
    }
    @GetMapping("getContactById")
    public Contact getContactById(@RequestParam long id) {
        return contactIService.getContactById(id);
    }
    @PutMapping("reply")
    public Contact updateWithReplyContact(@RequestParam String subject, @RequestParam String message, @RequestParam long idContact) throws MessagingException {
        return contactIService.updateWithReplyContact(subject, message, idContact);
    }
}
