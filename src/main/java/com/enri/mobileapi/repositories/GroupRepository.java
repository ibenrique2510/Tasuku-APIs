package com.enri.mobileapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enri.mobileapi.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{

}