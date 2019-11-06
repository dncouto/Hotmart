package br.com.desafioHotMart.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()	
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
		.logout()
			.logoutUrl("/logout")
		    .logoutSuccessUrl("/exit")
	        .clearAuthentication(true)
	        .deleteCookies("JSESSIONID", "auth_code")
			.invalidateHttpSession(true);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
        	.passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("daniel")
                .password("couto")
                .roles("USER")
            .and()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("admin")
                .password("admin")
                .authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
                .roles("MANAGER");
    }

}