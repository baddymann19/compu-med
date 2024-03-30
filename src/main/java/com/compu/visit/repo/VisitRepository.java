package com.compu.visit.repo;

import com.compu.visit.model.Visit;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VisitRepository implements PanacheRepositoryBase<Visit, Long> {

}
