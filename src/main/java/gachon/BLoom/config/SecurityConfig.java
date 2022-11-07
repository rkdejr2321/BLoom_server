package gachon.BLoom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall defaultHttpFireWall() {
        return new DefaultHttpFirewall();
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
        web.httpFirewall(defaultHttpFireWall());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf()
                .disable()

                .exceptionHandling()

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .authorizeRequests()

                // 접근제한 풀어주기
                // 해당 uri는 토큰이 없는 상태로 요청이 들어오기 때문에 허용
                .antMatchers("/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/member/signup").permitAll()
                .anyRequest().authenticated()

                .and()
                .logout().logoutSuccessUrl("/");

    }
}

