package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    public List<Module> findModuleByCourse_IdCourse(Long courseId);
}
