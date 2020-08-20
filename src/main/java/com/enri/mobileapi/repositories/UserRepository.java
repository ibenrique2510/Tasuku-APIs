package com.enri.mobileapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enri.mobileapi.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
