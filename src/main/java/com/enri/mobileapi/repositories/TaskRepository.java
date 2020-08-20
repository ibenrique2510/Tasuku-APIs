package com.enri.mobileapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enri.mobileapi.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
