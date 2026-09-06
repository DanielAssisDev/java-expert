package com.daniel.dev.repositories;

import com.daniel.dev.dto.ClientDTO;
import com.daniel.dev.dto.UserDTO;
import com.daniel.dev.entities.User;
import com.daniel.dev.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            	SELECT new com.daniel.dev.dto.UserDTO(u) FROM User u
            	WHERE UPPER(u.email) LIKE UPPER(CONCAT('%', :email, '%'))
            	""")
    Optional<UserDTO> searchUserAndRolesByEmailLike(String email);

    @Query(value = """
            SELECT new com.daniel.dev.dto.ClientDTO(u) FROM User u
            """)
    ClientDTO searchClient();

    @Query(nativeQuery = true, value = """
            SELECT u.email username, u.password, r.id roleId, r.authority
            			FROM tb_user u
            			INNER JOIN tb_user_role ur ON u.id = ur.user_id
            			INNER JOIN tb_role r ON r.id = ur.role_id
            			WHERE u.email = :email
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

    Optional<User> findByEmail(String email);
}

