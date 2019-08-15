package com.github.niecjing.oauth2.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.github.niecjing.oauth2.server.service.MyUserDetailsService;

/**
 * 配置授权服务
 *
 * @author Jing Zhi Bao
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    /**
     * 注入权限验证控制器 来支持 password grant type
     */
    @Autowired
    private AuthenticationManager                           authenticationManager;

    /**
     * 注入userDetailsService，开启refresh_token需要用到
     */
    @Autowired
    private MyUserDetailsService                            userDetailsService;

    /**
     * 数据源
     */
    @Autowired
    private DataSource                                      dataSource;

    /**
     * 设置保存token的方式，一共有五种，这里采用数据库的方式
     */
    @Autowired
    private TokenStore                                      tokenStore;

    @Autowired
    private WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 配置令牌端点(Token Endpoint)的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 配置oauth2服务跨域
        CorsConfigurationSource source = request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedOrigin(request.getHeader(HttpHeaders.ORIGIN));
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };

        security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients()
            .addTokenEndpointAuthenticationFilter(new CorsFilter(source));
    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
         * clientId：（必须的）用来标识客户的Id。
         * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
         * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
         * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
         * authorities：此客户端可以使用的权限（基于Spring Security authorities）
         */
        clients.jdbc(dataSource);

        // clients.inMemory()
        //         .withClient("id")
        //         .secret("abc")
        //         .scopes("a", "b")
        //         .authorizedGrantTypes("password", "client_credentials" , "authorization_code", "refresh_token")
                // .authorities("")
        ;

    }

    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 开启密码授权类型
        endpoints.authenticationManager(authenticationManager);
        // 配置token存储方式 当一个令牌是有效的时候，它可以被用来加载身份信息，里面包含了这个令牌的相关权限
        endpoints.tokenStore(tokenStore);
        // 自定义登录或者鉴权失败时的返回信息
        endpoints.exceptionTranslator(webResponseExceptionTranslator);
        // 要使用refresh_token的话，需要额外配置userDetailsService
        endpoints.userDetailsService(userDetailsService);

    }
}
