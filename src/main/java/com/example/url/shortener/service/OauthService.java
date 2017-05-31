package com.example.url.shortener.service;

import com.example.url.shortener.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.DefaultAuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by natal on 31-May-17.
 */
@Service
public class OauthService {

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    public void getToken() {
        Map<String, String> authorizationParameters = new HashMap<>();
        authorizationParameters.put("scope", "read");
        authorizationParameters.put("username", "1");
        authorizationParameters.put("password", "123");
        authorizationParameters.put("client_id", "my-trusted-client");
        authorizationParameters.put("grant", "password");

        DefaultAuthorizationRequest authorizationRequest = new DefaultAuthorizationRequest(authorizationParameters);
        authorizationRequest.setApproved(true);

//        Set<GrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_UNTRUSTED_CLIENT"));
        Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        authorizationRequest.setAuthorities(authorities);


//        HashSet<String> resourceIds = new HashSet<String>();
//        resourceIds.add("mobile-public");
//        authorizationRequest.setResourceIds(resourceIds);

        Account account = new Account();
        account.setId("1");
        account.setPassword("123");

        // Create principal and auth token
        User userPrincipal = new User(account.getId(), account.getPassword(), true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities) ;

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

//        CustomTokenStore tokenStore = new CustomTokenStore();
//
//        // Token Enhancer
//        CustomTokenEnhancer tokenEnhancer = new CustomTokenEnhancer(user.getUserID());
//
//        TokenServices tokenServices = new CustomTokenServices();
//        tokenServices.setTokenEnhancer(tokenEnhancer);
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setTokenStore(tokenStore);
//
//        OAuth2AccessToken accessToken = tokenServices.createAccessTokenForUser(authenticationRequest, user);
//        OAuth2AccessToken  token = defaultTokenServices.createAccessToken(auth);
    }

    public String getToken2() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
//        resourceDetails.setClientSecret(TestOAuthConstants.CLIENT_SECRET);
        resourceDetails.setClientId("my-trusted-client");
//        resourceDetails.setAccessTokenUri(TestOAuthConstants.TOKEN_REQUEST_URL);
        resourceDetails.setScope(Arrays.asList("read"));
        resourceDetails.setGrantType("password");

        OAuth2RestTemplate oAuthRestTemplate = new OAuth2RestTemplate(resourceDetails);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );

        OAuth2AccessToken token = oAuthRestTemplate.getAccessToken();
//        System.out.println(oAuthRestTemplate.getResource());
//        System.out.println(oAuthRestTemplate.getOAuth2ClientContext());
//        System.out.println(token);
        return token.getValue();
    }
}
