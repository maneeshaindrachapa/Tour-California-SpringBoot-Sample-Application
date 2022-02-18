package com.explore.california.repository;

import com.explore.california.model.Tour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
@Repository
public interface TourRepository extends CrudRepository<Tour, Integer> {

}
