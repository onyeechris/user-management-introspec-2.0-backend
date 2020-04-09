package com.activedge.usermgt.security;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    /**
     *
     * @param req
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().print(jsonLoginResponse);

        CustomHttpTrace cTrace = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .status(401)
                .username(req.getRemoteUser())
                .sourceIp(req.getRemoteAddr())
                .path(req.getRequestURL().toString())
                .queryParams(req.getQueryString())
                .method(req.getMethod())
                .payload(jsonLoginResponse)
                .rawBody("Access Denied !")
                .build();

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, cTrace);
    }

    @Getter
    private class InvalidLoginResponse {
        private String username;
        private String password;

        InvalidLoginResponse() {
            this.username = "Invalid Username";
            this.password = "Invalid Password";
        }
    }
}
