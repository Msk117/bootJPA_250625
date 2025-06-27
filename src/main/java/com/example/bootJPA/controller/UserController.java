package com.example.bootJPA.controller;

import com.example.bootJPA.dto.UserDTO;
import com.example.bootJPA.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/*")
@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        log.info(">>>> userDTO >> {}", userDTO);
        // password 암호화
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        String email = userService.register(userDTO);
        return (email != null) ? "/index" : "/user/join";
    }

    @GetMapping("/login")
    public String login( HttpServletRequest request,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "errmsg", required = false) String errmsg,
                         Model model){

        String email =(String)request.getSession().getAttribute("email");
        String errmsg =(String)request.getSession().getAttribute("errmsg");
        if(errmsg != null){
            log.info(">>>> controller errmsg >> {}", errmsg);
            model.addAttribute("email", email);
            model.addAttribute("errmsg", errmsg);
        }

        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("errmsg");

        return "/user/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                      RedirectAttributes redirectAttributes){
        log.info(">>>> userController >> email >> {}", request.getAttribute("email"));
        log.info(">>>> userController >> errmsg >> {}", request.getAttribute("errmsg"));
        redirectAttributes.addFlashAttribute("email", request.getAttribute("email"));
        redirectAttributes.addFlashAttribute("errmsg", request.getAttribute("errmsg"));

        return "redirect:/user/login";
    }

    @GetMapping("/list")
    public void list(Model model){
        model.addAttribute("userList",
                userService.getList());
    }

    @GetMapping("/modify")
    public void modify(Model model, Principal principal){
        model.addAttribute("userDTO",
                userService.selectEmail(principal.getName()));
    }
    @PostMapping("/modify")
    public String modify(UserDTO userDTO){
        if (!userDTO.getPwd().isEmpty()){
            userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        }
        String email = userService.modify(userDTO);

        return "redirect:/user/logout";
    }
    @GetMapping("/remove")
    public String remove(Principal principal){
        String email = userService.remove(principal.getName());
        return "redirect:/user/logout";
    }




}
