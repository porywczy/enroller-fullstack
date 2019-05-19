package com.company.enroller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private ParticipantProvider participantProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //wartości wstrzykniete z konfiguracji aplikacji za pomocą adnotacji @Value
    @Value("${security.secret}")
    private String secret;

    @Value("https://fullstack12345.herokuapp.com")
    private String issuer;

    @Value("${security.token_expiration_in_seconds}")
    private int tokenExpiration;

    //konfigurację autoryzacji
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //Skonfiguruj firewall tak, by nie pozwalał na korzystanie z aplikacji bez zalogowania się
                //(poza endpointem wystawiającym token oraz rejestrującym użytkownika)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/participants").permitAll()
                .antMatchers("/api/tokens").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                //zarejestruj stworzony filtr
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), secret, issuer, tokenExpiration), UsernamePasswordAuthenticationFilter.class)
                //Zarejestruj filtr w konfiguracji zabezpieczen
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //wstrzyknij wymagane zależności i skonfiguruj system autentykacji nadpisując odpowiednią metodę:
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(participantProvider).passwordEncoder(passwordEncoder);
    }
}
