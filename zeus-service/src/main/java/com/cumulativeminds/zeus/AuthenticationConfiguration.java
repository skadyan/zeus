package com.cumulativeminds.zeus;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity
public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Inject
    private AuthenticationProperties authenticationProperties;

    public AuthenticationConfiguration() {
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        AuthenticationProperties p = authenticationProperties;

        AuthenticationProvider authenticationProvider = authenticationProvider();
        if (authenticationProvider != null) {
            auth.authenticationProvider(authenticationProvider);
        }

        auth
                .ldapAuthentication()
                .userDnPatterns(p.getUserDnPatterns())
                .groupSearchBase(p.getGroupSearchBase())
                // .userSearchBase(p.getUserSearchBase())
                // .userSearchFilter(p.getUserSearchFilter())
                // .groupSearchFilter(p.getGroupSearchFilter())
                // .groupRoleAttribute(p.getGroupRoleAttribute())
                .contextSource()
                .url(p.getUrl())
                .ldif(p.getLdif())
                .root(p.getRoot());
    }

    public AuthenticationProvider authenticationProvider() {
        if (authenticationProperties.getUrl() == null) {
            return null;
        }

        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                authenticationProperties.getDomain(),
                authenticationProperties.getUrl());
        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setSearchFilter(authenticationProperties.getUserSearchFilter());

        return provider;
    }
}
