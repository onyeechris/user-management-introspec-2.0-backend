package com.activedge.usermgt.service.backends;

import java.util.List;

import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;

public interface AuthService {
    StaffDTO authenticate(String username, String password);
    void addUser(NewStaffDTO user);
    List<StaffDTO> findAll();
    List<StaffDTO> search(String searchString);
    StaffDTO getUser(String userId);
}