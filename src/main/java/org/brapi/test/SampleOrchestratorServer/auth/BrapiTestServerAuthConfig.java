package org.brapi.test.SampleOrchestratorServer.auth;

import org.springframework.context.annotation.Configuration;

// @Configuration
// @EnableWebSecurity 
// @EnableGlobalMethodSecurity(prePostEnabled=true)
public class BrapiTestServerAuthConfig { //extends WebSecurityConfigurerAdapter{
	
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable().authorizeRequests()
//                .anyRequest()
//                .permitAll().and() //TODO secure this
//                //.authenticated().and()
//                .addFilter(new BrapiTestServerJWTAuthFilter(authenticationManager()))
//                // this disables session creation on Spring Security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
}
