package com.devandre.mediumclone.security.provider;

import java.util.Date;

/**
 * @author Andre on 21/02/2024
 * @project medium-clone
 */
public interface UserTokenProvider {

    String getToken(String userId);

    Date getExpirationTime(String userId);
    
}
