package edu.asu.diging.gilesecosystem.nepomuk.core.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

import edu.asu.diging.gilesecosystem.util.users.IAdminUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IAdminUserDetailsService userDetailsService;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
        // Spring Security ignores request to static resources such as CSS or JS
        // files.
        .ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HeadersConfigurer<HttpSecurity> config = http.antMatcher("**").csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
            
            @Override
            public boolean matches(HttpServletRequest arg0) {
                // don't require CSRF for REST calls
                if (arg0.getRequestURI().indexOf("/rest/") > -1) {
                    return false;
                }
                logger.debug("Request method: " + arg0.getMethod());
                if (arg0.getMethod().equals("GET")) {
                    return false;
                }
                return true;
            }
        }).and().headers().frameOptions().sameOrigin();
        
        
        // Configures form login
        config.and().formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/?error=bad_credentials")
                // Configures the logout function
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/403")
                // Configures url based authorization
                .and()
                .authorizeRequests()
                // Anyone can access the urls
                .antMatchers("/", "/resources/**",
                        "/rest/**", "/logout").permitAll()
                // The rest of the our application is protected.
                .antMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("ADMIN");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(
                passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}