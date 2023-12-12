package com.unitral.microdoc.interceptor;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        System.out.println("**********************************");
        System.out.println("Request rid:: "+request.getRequestId());
        System.out.println("Request :: "+request.getRequestURI());  //  /ping
        System.out.println("Request :: "+request.getServerName());  // localhost
        System.out.println("Request :: "+request.getDispatcherType());  //Request
        System.out.println("Request :: "+request.getScheme());
        System.out.println("Request pcid:: "+request.getServletConnection().getProtocolConnectionId());
        System.out.println("Request :: "+request.getServletConnection().isSecure());
        System.out.println("Request :: "+request.getServletContext().getMajorVersion());
        System.out.println("Request :: "+request.getServletContext().getServerInfo());
        System.out.println("Request :: "+request.getServletContext().getVirtualServerName());
        System.out.println("Request :: "+request.getServletContext().getSessionTimeout());
        System.out.println("Request :: "+request.getRemoteAddr());
        System.out.println("Request id :: "+request.getSession().getId());
        System.out.println("Request :: "+request.getSession().isNew());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(System.currentTimeMillis()-request.getSession().getCreationTime()));
        System.out.println("Request Creation Time:: "+ df.format( request.getSession().getCreationTime()));
        System.out.println("Request :: "+request.getSession().getMaxInactiveInterval());
        System.out.println("Request sesid:: "+request.getRequestedSessionId());







        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle :: "+response.toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("Response :: "+response.getContentType());
        System.out.println("Response :: "+response.getBufferSize());
        System.out.println("Response :: "+response.getCharacterEncoding());
        System.out.println("Response :: "+response.getLocale());
        System.out.println("Response :: "+response.getStatus());



    }

}
