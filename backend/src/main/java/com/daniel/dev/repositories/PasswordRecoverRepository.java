package com.daniel.dev.repositories;

import com.daniel.dev.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
}
