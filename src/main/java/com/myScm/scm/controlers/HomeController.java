package com.myScm.scm.controlers;

import java.time.LocalTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.myScm.scm.Dto.UserForm;
import com.myScm.scm.entities.User;
import com.myScm.scm.errorHandler.DataDuplicacyException;
import com.myScm.scm.helper.AppConstants;
import com.myScm.scm.helper.Message;
import com.myScm.scm.helper.MessageType;
import com.myScm.scm.serviceImpl.UserServiceImple;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/scm2")
public class HomeController {

    private UserServiceImple serviceImple;
    private User user;

    public HomeController(UserServiceImple serviceImple) {
        this.serviceImple = serviceImple;
    }

    @GetMapping("/home")
    public String gohomepage(Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }

        return new String("home");
    }

    @GetMapping("/about")
    public String goaboutpage(Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }
        return new String("about");

    }

    @GetMapping("/service")
    public String goServicespage(Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }

        return new String("services");
    }

    @GetMapping("/contact")
    public String goContactpage(HttpSession session, Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }

        return new String("contact");
    }

    @GetMapping("/signin")
    public String goSigninPage(Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }
 
        return new String("signin");
    }

    @GetMapping("/signup")
    public String goSignUpPage(Authentication authentication, Model model) {

        if (authentication != null) {

            UserForm user = this.serviceImple
                    .getUserFormByUser(this.serviceImple.getUserByUsername(authentication.getName()));
            model.addAttribute("user", user);
        }

        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    @PostMapping("/do_signup")
    public String doSignUp(@Valid @ModelAttribute() UserForm user, BindingResult bindingResult, HttpSession session) {

        try {
            if (bindingResult.hasErrors()) {
                return "signup";
            }

            this.user = this.serviceImple.saveUser(user);
            if (this.user != null) {
                session.setAttribute("message", new Message("SignUp SuccessFull !!", MessageType.green));
            } else {
                throw new Exception();
            }
        } catch (DataIntegrityViolationException e) {

            throw new DataDuplicacyException(e.getMostSpecificCause().toString(), user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String("signin");
    }

    @GetMapping("/change")
    public String add() {

        return "loggedin/Setting/changeEmail";
    }

    @GetMapping("/sendVerifyLink")
    public String sendVerifyLink(@RequestParam(name = "email", defaultValue = "") String email, HttpSession session) {

        LocalTime localTime = LocalTime.now();

        session.setAttribute("time", localTime);

        if (this.serviceImple.sendVerifyLink(email)) {
            session.setAttribute("message", new Message("verification link is send to " + email, MessageType.blue));
            return "redirect:/scm2/signin";
        }

        session.setAttribute("message", new Message("check your mail and verify again " + email, MessageType.red));
        return "redirect:/scm2/signin";
    }

    @GetMapping("/verify/email/link/user/byuser/dkhefuen-eefefe")
    public String verifyEmail(@RequestParam(name = "email", defaultValue = "") String userName,
            HttpSession httpSession) {

        this.user = this.serviceImple.getUserByUsername(userName);

        LocalTime localTime1 = ((LocalTime) httpSession.getAttribute("time")).plusMinutes(5);
        LocalTime localTime2 = LocalTime.now();

        if (user != null && localTime2.isBefore(localTime1)) {
            if (serviceImple.verifyEmail(this.user)) {

                httpSession.setAttribute("message",
                        new Message("email verification is successfull", MessageType.green));
            }
        } else {

            if (localTime2.isAfter(localTime1)) {

                httpSession.setAttribute("message",
                        new Message("verification link expired!!", MessageType.red));
                return "redirect:/scm2/signin";
            }

            httpSession.setAttribute("message",
                    new Message("email verification failed!!", MessageType.red));
        }

        return "redirect:/scm2/signin";
    }

    @GetMapping("/forgot-pass")
    public String SendForgotPasswordLink(HttpSession session, @RequestParam(name = "email") String email) {

        if (this.serviceImple.forgotPass(email)) {
            session.setAttribute("message",
                    new Message("authentication link is send to registered mail", MessageType.blue));
        } else {

            session.setAttribute("message", new Message("please check your entered email", MessageType.red));
        }

        return "redirect:/scm2/signin";
    }

    @GetMapping("/linkClicked/{changeUrl}")
    public String goChangePassPage(Model model, @PathVariable("changeUrl") String constent,
            @RequestParam(name = "email") String email) {

        model.addAttribute("constent", constent);
        model.addAttribute("email", email);

        return "forgotPass";
    }

    @PostMapping("/change_Pass")
    public String getMethodName(HttpSession httpSession,
            @RequestParam(name = "email") String email, @RequestParam("constent") String constent,
            @RequestParam(name = "pass") String newPass1, @RequestParam(name = "pass1") String newPass2) {

        this.user = this.serviceImple.getUserByUsername(email);

        if (this.user != null && AppConstants.CHANGE_URL.equals(constent)) {

            httpSession.setAttribute("message", this.serviceImple.forgotChangePass(user, newPass1, newPass2));

        } else {
            httpSession.setAttribute("message",
                    new Message("INTERNAL_SERVER_ERROR please try again later some time!! ", MessageType.red));
        }

        return "redirect:/scm2/signin";

    }

}
