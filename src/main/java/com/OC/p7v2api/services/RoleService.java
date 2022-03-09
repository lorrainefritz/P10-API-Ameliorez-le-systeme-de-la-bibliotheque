package com.OC.p7v2api.services;

import com.OC.p7v2api.entities.Role;
import com.OC.p7v2api.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Log4j2
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> findAllRoles(){
        log.info("in BookService in findAllRoles method");
        return roleRepository.findAll();
    }

    public Role getARoleById(Integer id){
        log.info("in BookService in getARoleById method");
        return roleRepository.getById(id);
    }

    public Role saveARole(Role role){
        log.info("in BookService in saveARole method");
        return  roleRepository.save(role);
    }
    public void deleteARole(Role role){
        log.info("in BookService in deleteARole method");
         roleRepository.delete(role);
    }
}
