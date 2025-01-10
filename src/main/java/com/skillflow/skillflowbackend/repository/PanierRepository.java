package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.Module;
import com.skillflow.skillflowbackend.model.Panier;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.StatusPanier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanierRepository extends CrudRepository<Panier, Long> {
    Panier findByUserAndStatusPanier(User user, StatusPanier statusPanier);
}
