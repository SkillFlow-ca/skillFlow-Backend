package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact,Long> {
    @Query("SELECT c FROM Contact c order by c.createdAt desc")
    Page<Contact> findAll(Pageable pageable);
}
