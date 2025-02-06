package com.api.weather.repository;

import com.api.weather.DB_tabels.Cities;
import org.springframework.data.repository.CrudRepository;

public interface CitiesRepository extends CrudRepository<Cities,Long> {

    Cities findByTitle(String title);
}