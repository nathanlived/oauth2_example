package com.github.niecjing.oauth2.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Jing Zhi Bao
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    private static final String RESOURCE_ID = "resource-server-rest-api";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('app')";
    private static final String HI_PATTERN = "/hi/**";

    /**
     * 这里设置需要token验证的url
     * 这些url可以在WebSecurityConfigurerAdapter中排除掉，
     * 对于相同的url，如果二者都配置了验证
     * 则优先进入ResourceServerConfigurerAdapter,进行token验证。而不会进行
     * WebSecurityConfigurerAdapter 的 basic auth或表单认证。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers().antMatchers(HI_PATTERN).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, HI_PATTERN).access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE)
        ;

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }

}
