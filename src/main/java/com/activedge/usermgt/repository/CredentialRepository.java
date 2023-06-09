package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.Staff;
import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    private LdapRepository ldapRepository;
    @Autowired
    private StaffRepository staffRepository;

    private Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>() {{
//        Iterable<LdapUser> allLdapUsers = ldapRepository.findAll();
//        if(!allLdapUsers.equals(null)) {
//            allLdapUsers.forEach(a -> put(a.getUsername(), null));
//        }
//        Iterable<Staff> allStaff = staffRepository.findAll();
//        if(!allStaff.equals(null)){
//            allStaff.forEach(staff -> put(staff.getUsername(),null));
//        }
    }};

    @Override
    public String getSecretKey(String username) {
        return usersKeys.get(username).getSecretKey();
    }

    @Override
    public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
        usersKeys.put(username, new UserTOTP(username,secretKey,validationCode,scratchCodes));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UserTOTP {
        private String username;
        private String secretKey;
        private int validationCode;
        private List<Integer> scratchCodes;
    }
}

