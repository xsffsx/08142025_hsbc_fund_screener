/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.constant.Constant;

import lombok.Setter;

public class HealthcheckSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private String roles;

    @Setter
    private AuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(Constant.URL_DASHBOARD).permitAll();
//                .authenticated().and().formLogin()
//            .successHandler(this.successHandler).and().httpBasic().disable().csrf().disable().cors();
    }

//

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser(this.username)
            .password(this.password)
            .roles(this.roles);
    }

}
