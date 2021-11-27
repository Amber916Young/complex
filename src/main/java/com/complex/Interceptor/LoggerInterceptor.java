package com.complex.Interceptor;
import com.complex.entity.OperationRecord;
import com.complex.entity.UserInfo;
import com.complex.service.TaskService;
import com.complex.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        String url = request.getRequestURI();
        String method = request.getMethod();
        String[] urlArray = url.split("/");
        if(urlArray.length>=2){
            if(url.contains("insert")||url.contains("upload")&&("POST".equalsIgnoreCase(method))){
                addOperation(request,"insert",url,0);
            }
            if(url.contains("del")||url.contains("deletes")&&("POST".equalsIgnoreCase(method))){
                addOperation(request,"deletes",url,0);
            }
            if(url.contains("assign")&&("POST".equalsIgnoreCase(method))){
                addOperation(request,"assign Job",url,0);
            }
            if(url.contains("login")&&url.contains("permission")){
                addOperation(request,"login",url,0);
            }
            if(url.contains("logout")&&("POST".equalsIgnoreCase(method))){
                addOperation(request,"logout",url,0);
            }
        }else{
            System.out.println("Invalid URL："+url);
            addOperation(request,"Invalid |URL",url,0);

        }

    }
    @Autowired
    TaskService taskService ;

    /**
     * 向操作日志中增加数据的方法 yang 2019
     * @param request
     * @param operate  操作
     * @param operateObject  操作对象
     */
    public void addOperation(HttpServletRequest request, String operateObject, String operate,int id){
        OperationRecord operationRecord = new OperationRecord();
        HttpSession session = request.getSession();
        UserInfo loginUser =(UserInfo)session.getAttribute("user");

        operationRecord.setBehavior(operateObject);
        operationRecord.setMethod(operate);
        operationRecord.setUser(loginUser.getUsername());
        Date ltime = new Date();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(ltime.getTime());
        operationRecord.setOperateTime(time);
        taskService.insertSelective(operationRecord);
        System.err.println("add logger");
    }


}