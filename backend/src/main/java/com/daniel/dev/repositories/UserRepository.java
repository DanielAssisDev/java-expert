package com.daniel.dev.repositories;

import com.daniel.dev.entities.User;
import com.daniel.dev.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = """
            SELECT u.email username, u.password, r.id roleId, r.authority
            			FROM tb_user u
            			INNER JOIN tb_user_role ur ON u.id = ur.user_id
            			INNER JOIN tb_role r ON r.id = ur.role_id
            			WHERE u.email = :email
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

    User findByEmail(String email);
}

