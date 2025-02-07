package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.model.Panier;

public interface PanierIService {
    public Panier addCourseToPanier(Long courseId,Panier panier);
    public Panier removeCourseFromPanier(Long courseId);
    public void clearPanier();
    public Panier getPanier();
    public Double getPaniertotal();

}
