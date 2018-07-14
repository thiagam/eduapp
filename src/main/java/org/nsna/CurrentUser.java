package org.nsna;

import org.nsna.domain.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4511690576618377952L;
	private User user;

    public CurrentUser(User user) {
        super(user.getUserName(), user.getPasswordHash(),
        		true, //enabled
        		user.isAccountNonExpired(), //accountNonExpired
        		true, // credentialsNonExpired
        		true, // accountNonLocked
        		AuthorityUtils.createAuthorityList(user.getUserRole()));
    	//System.out.println(user.getUserRole());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getRole() {
    	System.out.println(user.getUserRole());
        return user.getUserRole();
    }

}
