package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Enrollment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.user.idUser = :idUser")
    public List<Enrollment> getEnrollmentsByUserId(@Param("idUser") Long idUser);
}
