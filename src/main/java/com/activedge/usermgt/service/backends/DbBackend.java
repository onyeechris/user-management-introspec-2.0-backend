package com.activedge.usermgt.service.backends;

import java.util.List;

import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;

public class DbBackend implements AuthService {

    @Override
    public StaffDTO authenticate(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addUser(NewStaffDTO user) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<StaffDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<StaffDTO> search(String searchString) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StaffDTO getUser(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

}