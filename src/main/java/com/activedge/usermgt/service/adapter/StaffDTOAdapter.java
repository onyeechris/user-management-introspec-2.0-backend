package com.activedge.usermgt.service.adapter;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class StaffDTOAdapter {

    public Optional<StaffDTO> transform(Optional<LdapUser> l_user) {
        if(l_user.isPresent()) {
            LdapUser l_usr = l_user.get();
            return Optional.of(new StaffDTO(l_usr.getFullname().split(" ")[0], l_usr.getFullname().split(" ")[1],l_usr.getUsername(),l_usr.getMail(), Type.USER));
        } else {
            return Optional.empty();
        }
    }

    public Page<StaffDTO> transform(Iterable<LdapUser> l_users) {
        Iterator<LdapUser> lu = l_users.iterator();
        List<StaffDTO> userList = new ArrayList<>();

        while(lu.hasNext()) {
            LdapUser x = lu.next();
            userList.add(new StaffDTO(x.getFullname().split(" ")[0], x.getFullname().split(" ")[1],x.getUsername(),x.getMail(), Type.USER));
        }
        return new PageImpl<>(userList);
    }
}
