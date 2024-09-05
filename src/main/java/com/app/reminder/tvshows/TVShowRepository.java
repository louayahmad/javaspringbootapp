package com.app.reminder.tvshows;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TVShowRepository extends JpaRepository<TVShow, String> {

}