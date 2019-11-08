package example.webauthn;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static example.webauthn.security.WebAuthnConfigurer.webAuthn;

/**
 * @author Rob Winch
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.apply(webAuthn())
				.and()
			.formLogin().and()
			.authorizeRequests()
				.mvcMatchers("/secure").access("@mfa.require(authentication)")
				.anyRequest().authenticated();
	}
}
