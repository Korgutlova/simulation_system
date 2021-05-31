package com.korgutlova.diplom.config;

import com.korgutlova.diplom.security.AuthProviderImpl;
import com.korgutlova.diplom.security.CustomizeLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.korgutlova.diplom.model.enums.roles.Role.*;

@Configuration
@EnableWebSecurity
@ComponentScan("com.korgutlova.diplom.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProviderImpl authProvider;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;

    @Autowired
    public SecurityConfig(AuthProviderImpl authProvider, AccessDeniedHandler accessDeniedHandler, CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler) {
        this.authProvider = authProvider;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customizeLogoutSuccessHandler = customizeLogoutSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/sign_up/**", "**/**").anonymous()
                .antMatchers("/chat/**", "/conversation/**", "/queue/**",
                        "/app/**", "/topic/**", "/api/**", "/home/**", "/project/**", "/static/**")
                .hasAnyRole(USER.toString(), ADMIN.toString(), BOT.toString(), ORGANIZER.toString());


        http.csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/process")
                .usernameParameter("login")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutSuccessHandler(customizeLogoutSuccessHandler)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }
}