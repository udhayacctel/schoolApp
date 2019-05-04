package com.sstutor.security.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstutor.security.filter.ApiTokenAuthFilter;
import com.sstutor.security.filter.OTPLoginProcessingFilter;
import com.sstutor.security.filter.SignupProcessingFilter;
import com.sstutor.security.filter.MobileLoginProcessingFilter;
import com.sstutor.security.token.ApiTokenExtractor;
import com.sstutor.security.filter.RestAuthenticationEntryPoint;
import com.sstutor.security.config.SkipPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String JWT_TOKEN_HEADER_PARAM_ID = "X-Authorization_Id";
    
    public static final String ENTRY_POINT_ALL_API = "/**";
    public static final String ENTRY_POINT_LOGIN_OTP = "/login/otp";
    public static final String ENTRY_POINT_SIGNUP = "/signup";
    public static final String ENTRY_POINT_LOGIN_MOBILE = "/login/mobileno";
    
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired @Qualifier("OTPLoginSuccessHandler") private AuthenticationSuccessHandler otpLoginSuccessHandler;
    @Autowired private AuthenticationFailureHandler failureHandler;
    
    @Autowired private ApiTokenExtractor apiTokenExtractor;
        
    @Autowired private ObjectMapper objectMapper;
    
    @Autowired private DataSource dataSource;
    
    
    protected OTPLoginProcessingFilter buildOTPLoginProcessingFilter() throws Exception {
    	logger.debug("hello sercurityconfig - buildOTPLoginProcessingFilter");
    	OTPLoginProcessingFilter filter = new OTPLoginProcessingFilter(ENTRY_POINT_LOGIN_OTP, otpLoginSuccessHandler, failureHandler, objectMapper, dataSource);
        return filter;
    }
    
    protected SignupProcessingFilter buildSignupProcessingFilter() throws Exception {
    	logger.debug("hello sercurityconfig - buildSignupProcessingFilter");
    	SignupProcessingFilter filter = new SignupProcessingFilter(ENTRY_POINT_SIGNUP, failureHandler, objectMapper, dataSource);
        return filter;
    }
    
    protected MobileLoginProcessingFilter buildMobileLoginProcessingFilter() throws Exception {
    	logger.debug("hello sercurityconfig - buildMobileLoginProcessingFilter");
    	MobileLoginProcessingFilter filter = new MobileLoginProcessingFilter(ENTRY_POINT_LOGIN_MOBILE, failureHandler, objectMapper, dataSource);
        return filter;
    }    
    
    protected ApiTokenAuthFilter buildApiTokenAuthFilter() throws Exception {
    	logger.debug("hello sercurityconfig - buildApiTokenAuthFilter");
    	List<String> pathsToSkip = Arrays.asList(ENTRY_POINT_LOGIN_OTP, ENTRY_POINT_SIGNUP, ENTRY_POINT_LOGIN_MOBILE);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, ENTRY_POINT_ALL_API);
        ApiTokenAuthFilter filter 
            = new ApiTokenAuthFilter(failureHandler, apiTokenExtractor,  matcher, dataSource);
        
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
    	logger.debug("hello sercurityconfig - configure authmanagerbuilder");
    	//auth.authenticationProvider(apiTokenAuthProvider);
    	//auth.authenticationProvider(otpLoginAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	logger.debug("hello sercurityconfig - configure httpsecurity");
    	
        http
        .cors()
        .and()
        .csrf().disable() // We don't need CSRF for JWT based authentication
        .exceptionHandling()
        .authenticationEntryPoint(this.authenticationEntryPoint)
        
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
            .authorizeRequests()
                .antMatchers(ENTRY_POINT_LOGIN_OTP).permitAll() // Permit this without security
        .and()
        	.authorizeRequests()
                .antMatchers(ENTRY_POINT_SIGNUP).permitAll() // Permit this without security
        .and()
            .authorizeRequests()
                 .antMatchers(ENTRY_POINT_LOGIN_MOBILE).permitAll() // Permit this without security                
                    
                
        .and()
             .authorizeRequests()
                .antMatchers(ENTRY_POINT_ALL_API).authenticated() // Protected API End-points
                       
        .and()
            .addFilterBefore(buildMobileLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        	.addFilterBefore(buildSignupProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        	.addFilterBefore(buildOTPLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        	.addFilterBefore(buildApiTokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "X-Authorization", "X-Authorization_Id", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}