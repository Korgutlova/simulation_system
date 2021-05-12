package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.service.api.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping("/signup")
    public String getSignUp(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String getSignUp(@ModelAttribute("signUpForm") @Valid SignUpForm signUpForm, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && userService.findByLogin(signUpForm.getLogin()) == null) {
            userService.register(signUpForm);
            return "redirect:/login";
        }
        return "signup";
    }
}