package com.dambrz.projectmanagementsystemapi.repository;

import com.dambrz.projectmanagementsystemapi.model.Role;
import com.dambrz.projectmanagementsystemapi.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(ERole name);
}
