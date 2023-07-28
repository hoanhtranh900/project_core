package com.osp.security;

import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmUser;
import com.osp.core.repository.AdmAuthoritiesRepository;
import com.osp.core.repository.AdmGroupRepository;
import com.osp.core.repository.AdmUserRepository;
import com.osp.core.utils.UtilsCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Service
public class CrmUserDetailsService implements UserDetailsService {

    @Autowired private AdmUserRepository userRepository;
    @Autowired private AdmGroupRepository groupRepository;
    @Autowired private AdmAuthoritiesRepository authoritiesRepository;
    @Autowired private MessageSource messageSource;
    @Value("${supper.type_user_logins}") private String typeUserLogins;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        AdmUser user = this.userRepository.findByUsernameAndTypeUserIn(username, Arrays.asList(typeUserLogins.split(","))).orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("error.login_fail", null, UtilsCommon.getLocale())));
        if (user == null) {
            throw new UsernameNotFoundException(messageSource.getMessage("error.login_fail", null, UtilsCommon.getLocale()));
        }
        List<String> list = this.authoritiesRepository.loadListAuthorityOfUserByUsername(user.getUsername(), ConstantString.STATUS.active, ConstantString.IS_DELETE.active);
        if (list != null && list.size() > 0) {
            for (String authority : list) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
            user.setGrantedAuths(authorities);
        }
        return user;
    }

}
