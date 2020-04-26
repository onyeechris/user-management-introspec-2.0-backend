package com.activedge.usermgt.service.adapter;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.dto.StaffDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StaffDTOAdapter {

    public Optional<StaffDTO> transform(Optional<LdapUser> l_user) {
        if(l_user.isPresent()) {
            LdapUser l_usr = l_user.get();
            return Optional.of(new StaffDTO(l_usr.getFullname().split(" ")[0], l_usr.getFullname().split(" ")[1],l_usr.getMail(),l_usr.getUserid()));
        } else {
            return Optional.empty();
        }
    }

    public Page<StaffDTO> transform(Page<LdapUser> l_users) {
        return new PageImpl<>(l_users.stream()
                .map(x -> new StaffDTO(x.getFullname().split(" ")[0], x.getFullname().split(" ")[1],x.getMail(),x.getUserid()))
                .collect(Collectors.toList()));
    }
}
