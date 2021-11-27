package com.complex.Interceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            return true;
        } else {
            System.out.println("You haven't logged in");
            response.sendRedirect("/index/login");
            return false;
        }
    }

}