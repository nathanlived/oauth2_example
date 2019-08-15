package com.github.niecjing.oauth2.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

/**
 * @author Jing Zhi Bao
 */
@Configuration
public class MyWebResponseExceptionTranslate extends DefaultWebResponseExceptionTranslator {

    /**
     * 自定义登录或者鉴权失败时的返回信息
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);

        OAuth2Exception body = responseEntity.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());
        // do something with header or response
        if (400 == responseEntity.getStatusCode().value()) {
            if ("Bad credentials".equals(body.getMessage())) {
                return new ResponseEntity("您输入的用户名或密码错误", headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity(body, headers, responseEntity.getStatusCode());

    }

}
