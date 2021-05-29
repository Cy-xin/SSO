package com.xin.sso.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xin.sso.entity.User;
import com.xin.sso.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author xkk
 * @date 2021/5/29/029 18:19:49
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private boolean needValidate = true;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@Param("name") String name, @Param("pwd") String pwd, @Param("verifyCode") String verifyCode,
                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String strResult = "";
        String times = "3000";
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");

        String validateCode = session.getAttribute("validateCode") != null ? session.getAttribute("validateCode").toString() : "";
        if (needValidate) {
            boolean checkCode = verifyCode != null && !verifyCode.equalsIgnoreCase(validateCode);
            if (verifyCode == null || checkCode) {
                strResult = "{\"result\":\"failure\",\"reason\":\"validate code error\"}";
                response.setContentType("text/text");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(strResult);
                return;
            }

            User user = userService.loginUser(name, pwd);
            if (user != null) {
                session.setAttribute("userid", user.getId());
                session.setAttribute("username", user.getUserName());
                session.setAttribute("userAlias", user.getUserAlias());
                session.setAttribute("sex", user.getSex());

                long time = System.currentTimeMillis();
                String timeString = user.getUserName() + "_" + uuid + "_" + time;

                if (session.getAttribute("ticket") == null || !session.getAttribute("ticket").toString().equalsIgnoreCase(timeString)) {
                    int expireSeconds = Integer.parseInt(times);
                    redisTemplate.opsForValue().set(timeString, user.getId(), expireSeconds);
                    session.setAttribute("ticket", timeString);
                }

                strResult += "{\"result\":\"success\",\"userInfo\":{\"username\":\"" + user.getUserName() + "\",\"useralias\":\"" + user.getUserAlias() + "\","
                        + "\"userId\":\"" + user.getId() + "\",\"userSex\":\"" + user.getSex() + "\"," + "\"userEmail\":\"" + (user.getUserEmail() != null ? user.getUserEmail() : "")
                        + "\",\"registerDate\":\"" + (user.getRegisterDate() != null ? user.getRegisterDate().toLocaleString() : "") + "\"},\"ticket\":\"" + timeString + "\"}";

                response.setContentType("text/text");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(strResult);
                Cookie cookie = new Cookie("sso", user.getUserName());
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                strResult = "{\"result\":\"failure\",\"reason\":\"user does not exist\"}";
                response.setContentType("text/text");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(strResult);
                request.getRequestDispatcher("index.html").forward(request, response);
            }
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String ticket = request.getParameter("ticket");
        session.removeAttribute("userid");
        session.removeAttribute("username");
        session.removeAttribute("userAlias");
        session.removeAttribute("sex");
        session.removeAttribute("ticket");
        if (ticket == null) {
            ticket = session.getAttribute("ticket").toString();
        }
        redisTemplate.delete(ticket);
        return "index";
    }

    private String getLocalIp(HttpServletRequest request) {
        String[] ADDR_HEADER = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "X-Real-IP" };
        String NUKNOWN = "unknown";
        String addr = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hsr = request;
            for (String header : ADDR_HEADER) {
                if (StringUtils.isBlank(addr) || NUKNOWN.equalsIgnoreCase(addr)) {
                    addr = hsr.getHeader(header);
                } else {
                    break;
                }
            }
        }
        if (StringUtils.isBlank(addr) || NUKNOWN.equalsIgnoreCase(addr)) {
            addr = request.getRemoteAddr();
        } else {
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按','分割
            if (addr != null) {
                int i = addr.indexOf(",");
                if (i > 0) {
                    addr = addr.substring(0, i);
                }
            }

        }
        return addr;
    }

}
