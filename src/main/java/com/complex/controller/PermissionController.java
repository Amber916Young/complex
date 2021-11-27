package com.complex.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.complex.entity.UserInfo;
import com.complex.service.PermissionService;
import com.complex.utils.AJAXReturn;
import com.complex.utils.IPUtil;
import com.complex.utils.Page;
import com.complex.utils.TimeParse;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;
    private String Delcode="root";

    private String authUriSet ="authUriSet";

    TimeParse timeParse = new TimeParse();

    private void updateUserlogin(HttpServletRequest request, UserInfo user) throws Exception{
        String ip = IPUtil.getIpAddress(request);
        String ipaddress = "";
        String loginTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
        user.setLoginIp(ip);
        user.setLoginPosition(ipaddress);
        user.setLoginTime(loginTime);
        permissionService.updateUserInfo(user);
    }


    @SneakyThrows
    @ResponseBody
    @GetMapping("user/query")
    public Object CPULOGLoad(Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        page.setRows(limit);
        page.setKeyWord(keyword);
        List<HashMap> lists = permissionService.pageQueryUserData(page);
        int totals=permissionService.pageQueryUserCount(page);
        return  AJAXReturn.success("successfully",lists,totals);
    }
    @ResponseBody
    @PostMapping(value = "user/deletes")
    public Object userDeletes(@RequestBody String jsonData)  throws UnsupportedEncodingException {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=","");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        JSONArray ids = json.getJSONArray("ids");
        String code = json.getString("code");
        if (code.equals(Delcode)) {
            for (int i = 0; i < ids.size(); i++) {
                int id =  (int)ids.get(i);
                permissionService.deleteUsersByid(id);
            }
            res = "successfully!";
            return AJAXReturn.success(res);
        } else {
            res = "Code is wrong，delete fail!";
            return AJAXReturn.error(res);
        }
    }

    @ResponseBody
    @RequestMapping("/web/logout")
    public Object logout(HttpSession session,HttpServletRequest request){
        try {
            Map<String, HttpSession> userMap = (Map<String, HttpSession>) request.getServletContext().getAttribute("userMap");
            UserInfo userlogin = (UserInfo) session.getAttribute("user");
            if (userlogin != null) {
                String username =userlogin.getUsername();
                Iterator<Map.Entry<String, HttpSession> > it = userMap.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, HttpSession> entry=it.next();
                    String key=entry.getKey();
                    if(key.equals(username)){
                        it.remove();
                    }
                }
            }
            session.invalidate();
            return AJAXReturn.success("Logout successful");
        }catch (Exception e){
            return AJAXReturn.warn(e.getMessage());
        }
    }
    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/web/login", produces = "application/json; charset=utf-8")
    public Object UserLogin(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response,
                            HttpSession session){
        try {
            jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
            JSONObject json = JSONObject.parseObject(jsonData);
            Map<String, HttpSession> userMap = (Map<String, HttpSession>) request.getServletContext().getAttribute("userMap");
            if (userMap == null) {
                userMap = new HashMap<String, HttpSession>();
            }
            String pass =json.getString("password");
            String username =json.getString("username");
            UserInfo userlogin = (UserInfo) session.getAttribute("user");
            if (userlogin != null) {
                return AJAXReturn.warn("You have logged in and cannot log in again");
            }
            if (userMap.get(username) == null) {
                HashMap param = new HashMap();
                param.put("username",username);
                param.put("password",pass);
                UserInfo cuser = permissionService.authenticate(param);
                if (cuser == null) {
                    return AJAXReturn.warn("Wrong user name or password, please try again");
                } else {
                    updateUserlogin(request,cuser);
                    userMap.put(cuser.getUsername(), session);
                    request.getServletContext().setAttribute("userMap", userMap);
                    request.changeSessionId();
                    session.setAttribute("user", cuser);
                    int numberOfSessions = userMap.size();
                    session.setAttribute("numberOfSessions", numberOfSessions);
//                    String asscess_token = UserToken.token(username,pass);
                    return AJAXReturn.success("Login successful");
                }
            }else {
                HashMap param = new HashMap();
                param.put("username",username);
                param.put("password",pass);
                UserInfo cuser = permissionService.authenticate(param);
                if (cuser == null) {
                    return AJAXReturn.warn("Wrong user name or password, please try again");
                } else {
                    if(session.getAttribute("user")==null){
                        session.setAttribute("user", cuser);
                    }
                    int numberOfSessions = userMap.size();
                    if(session.getAttribute("numberOfSessions")==null){
                        session.setAttribute("numberOfSessions", numberOfSessions);
                    }
                    updateUserlogin(request,cuser);
//                    String asscess_token = UserToken.token(username,pass);
                    return AJAXReturn.success("Login successful");

                }
            }

        }catch (Exception e){
            return AJAXReturn.error(e.getMessage());
        }
    }
}
