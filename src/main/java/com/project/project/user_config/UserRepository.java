package com.project.project.user_config;

import com.project.project.user_config.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String>,
                                         JpaRepository<User, String> {
}
