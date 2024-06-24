package com.myScm.scm.configuration;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecutityConfiguration {

    @Autowired
    private Auth2SuccessHandler auth2SuccessHandler;

    @Bean
    public CustomUserDetailServices getCustomUserDetailServices() {

        return new CustomUserDetailServices();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(getCustomUserDetailServices(), getpasswordEncoder());
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder getpasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain getsecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(t -> t.disable())
                .authorizeHttpRequests(a -> a.requestMatchers("/scm2/user/**")
                .authenticated()
                .requestMatchers("/admin/**")
                .authenticated()
                .anyRequest()
                .permitAll())
                .formLogin(login -> login
                .loginPage("/scm2/signin").loginProcessingUrl("/dosignin")
                .defaultSuccessUrl("/scm2/user/dashboard")
                .passwordParameter("password")
                .usernameParameter("userEmail")
                // This is The faluir Handler
                .failureHandler(customAuthenticationFailureHandler())
                // // This are Success Handler
                // .successHandler(new AuthenticationSuccessHandler() {

                // @Override
                // public void onAuthenticationSuccess(HttpServletRequest request,
                // HttpServletResponse response, Authentication authentication)
                // throws IOException, ServletException {
                // throw new UnsupportedOperationException("Unimplemented method
                // 'onAuthenticationSuccess'");
                // }

                // })
                );

        httpSecurity.addFilterBefore(customLogoutFilter("/scm2/user/logout", "/scm2/signin?logout"), LogoutFilter.class);
        httpSecurity.addFilterBefore(customLogoutFilter("/scm2/user/change", "/scm2/signin?change"), LogoutFilter.class);

        httpSecurity.oauth2Login(oauth2
                -> oauth2.loginPage("/scm2/signin")
                        .successHandler(auth2SuccessHandler)
        );

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    private Filter customLogoutFilter(String logoutUrl, String logoutSuccessUrl) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        SimpleUrlLogoutSuccessHandler successHandler = new SimpleUrlLogoutSuccessHandler();
        successHandler.setDefaultTargetUrl(logoutSuccessUrl);

        LogoutFilter logoutFilter = new LogoutFilter(successHandler, logoutHandler);
        logoutFilter.setFilterProcessesUrl(logoutUrl);
        return logoutFilter;
    }

}
