package com.rehund.ecommerce.repository;

import com.rehund.ecommerce.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    void deleteByUserId(Long userId);

    @Query(value = """
            SELECT * FROM user_roles
            WHERE user_id = :userId
            """, nativeQuery = true)
    List<UserRole> findRoleByUserId(@Param("userId") Long userId);
}
