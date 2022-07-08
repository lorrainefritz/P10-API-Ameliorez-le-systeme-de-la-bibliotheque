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
        log.info("in RoleService in findAllRoles method");
        return roleRepository.findAll();
    }

    public Role getARoleById(Integer id) throws Exception {
        log.info("in RoleService in getARoleById method");
        if (id==null){
            log.info("in RoleService in getARoleById method where id is null");
            throw new Exception("Id can't be null");
        }
        return roleRepository.getById(id);
    }

    public Role saveARole(Role role) throws Exception {
        log.info("in BookService in saveARole method");
        if (role==null){
            log.info("in RoleService in saveArole method where stock is null");
            throw new Exception("Role can't be null");
        }
        return  roleRepository.save(role);
    }
    public void deleteARole(Role role) throws Exception {
        log.info("in BookService in deleteARole method");
        if (role==null){
            log.info("in RoleService in deleteArole method where stock is null");
            throw new Exception("Role can't be null");
        }
        roleRepository.delete(role);
    }
}
