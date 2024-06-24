package com.myScm.scm.controlers;

import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.Dto.SearchContactForm;
import com.myScm.scm.Dto.UpdateUserForm;
import com.myScm.scm.Dto.UserForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import com.myScm.scm.errorHandler.ResourceNotFoundException;
import com.myScm.scm.helper.Message;
import com.myScm.scm.helper.MessageType;
import com.myScm.scm.serviceImpl.ContactServiceImpl;
import com.myScm.scm.serviceImpl.SearchServiceImpl;
import com.myScm.scm.serviceImpl.UserServiceImple;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/scm2/user")
public class UserController {

    private final ContactServiceImpl contactServiceImpl;
    private final UserServiceImple userServiceImple;
    private final SearchServiceImpl searchServiceImpl;

    private Contact contact;
    private User user;
    @SuppressWarnings("used")
    private UserForm userForm;
    private Page<ContactForm> contactPages;

    public UserController(ContactServiceImpl contactServiceImpl, UserServiceImple userServiceImple,
            SearchServiceImpl searchServiceImpl) {
        this.contactServiceImpl = contactServiceImpl;
        this.userServiceImple = userServiceImple;
        this.searchServiceImpl = searchServiceImpl;
    }

    // Dashboard Page
    /**
     * @param authentication
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dashboard")
    public String godahboard(Authentication authentication, Model model) {

        this.user = userServiceImple.getUserFromLogin(authentication);
        this.userForm = this.userServiceImple.getUserFormByUser(this.user);
        model.addAttribute("user", this.userForm);
        model.addAttribute("fev", this.contactServiceImpl.getFavoriteContacts(this.user));
        return "loggedin/dashboard";
    }

    // GO to the Add Contact Page
    /**
     * @param model
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/add-contact")
    public String goAddContac(Model model, Authentication authentication) {

        this.userForm = this.userServiceImple
                .getUserFormByUser(userServiceImple.getUserFromLogin(authentication));
        model.addAttribute("user", this.userForm);

        model.addAttribute("contactForm", new ContactForm());
        return "loggedin/addContact";
    }

    // Proceed to the Contact Addition
    /**
     *
     * @param con
     * @param bindingResult
     * @param session
     * @param authentication
     * @param model
     * @return
     */
    @PostMapping("/doadd-contact")
    @PreAuthorize("hasRole('USER')")
    public String proceedAddContact(@Valid @ModelAttribute() ContactForm con, BindingResult bindingResult,
            HttpSession session, Authentication authentication, Model model) {

        try {

            this.user = this.userServiceImple.getUserFromLogin(authentication);
            model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
            if (bindingResult.hasErrors()) {
                return "loggedin/addContact";
            }

            this.contact = contactServiceImpl.saveContact(con, user);
            if (contact != null) {
                session.setAttribute("message", new Message("contact successfully added !!", MessageType.green));
            }

        } catch (Exception e) {

        }

        return "redirect:/scm2/user/add-contact";
    }

    // Go view All Contact
    /**
     *
     * @param page
     * @param model
     * @param httpSession
     * @param authentication
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/view-contact")
    public String viewContact(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
            HttpSession httpSession,
            Authentication authentication) throws Exception {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
        model.addAttribute("searchForm", new SearchContactForm("view", 0));

        // if (page < 0) {
        // page++;
        // }
        this.contactPages = this.contactServiceImpl.getContactPages(page, user);

        if (this.contactPages.isEmpty() && page >= 1) {
        this.contactPages = this.contactServiceImpl.getContactPages(page - 1,
        this.user);
        }
        if (!this.contactPages.isEmpty()) {

        model.addAttribute("contacts", this.contactPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", this.contactPages.getTotalPages());
        model.addAttribute("elements", this.contactPages.getNumberOfElements());

        } else {
        httpSession.setAttribute("message", new Message("you can not have any contact", MessageType.yellow));
        }

        return "loggedin/viewContact";
    }

    // View Favorite Contacts
    /**
     *
     * @param model
     * @param page
     * @param httpSession
     * @param authentication
     * @return
     */
    @GetMapping("/fav-contact")
    @PreAuthorize("hasRole('USER')")
    public String fevContac(Model model, @RequestParam("page") int page, HttpSession httpSession,
            Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
        model.addAttribute("searchForm", new SearchContactForm("fev", 0));

        if (page < 0) {
            page++;
        }

        this.contactPages = contactServiceImpl.getFavoriteContactsList(page, this.user);

        if (this.contactPages.isEmpty() && page >= 1) {
            this.contactPages = contactServiceImpl.getFavoriteContactsList(page - 1, this.user);
        }

        model.addAttribute("fev", this.contactServiceImpl.getFavoriteContacts(this.user));

        if (this.contactPages.hasContent()) {
            model.addAttribute("fevcontacts", this.contactPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPage", this.contactPages.getTotalPages());
            model.addAttribute("elements", this.contactPages.getNumberOfElements());

        } else {
            httpSession.setAttribute("message",
                    new Message("you can not have any contact in favorite", MessageType.yellow));
        }

        return "loggedin/favorite";
    }

    // Search any contact
    /**
     * @param searchContactForm
     * @param authentication
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/search")
    public String searchContact(@ModelAttribute() SearchContactForm searchContactForm,
            Authentication authentication,
            Model model, HttpSession session) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
        model.addAttribute("searchForm", searchContactForm);

        this.contactPages = this.searchServiceImpl.globalSearchMethod(searchContactForm, user,
                searchContactForm.getFromRes());

        if (this.contactPages.hasContent()) {
            model.addAttribute("searchcontacts", this.contactPages);
            model.addAttribute("elements", this.contactPages.getNumberOfElements());

        } else {

            session.setAttribute("message", new Message("result not found", MessageType.red));
        }
        return "loggedin/searchResult";
    }

    // Go to the Profile Page
    /**
     *
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public String goProfilePage(Model model, Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.userForm = this.userServiceImple.getUserFormByUser(this.user);
        model.addAttribute("user", this.userForm);
        model.addAttribute("profile", userForm);
        model.addAttribute("fev", this.contactServiceImpl.getFavoriteContacts(user));

        return "loggedin/profile";
    }

    // Go to the update profile page
    /**
     *
     * @param model
     * @param id
     * @param authentication
     * @return
     */
    @PostMapping("/update-profile")
    @PreAuthorize("hasRole('USER')")
    public String goUpdateProfile(Model model, @RequestParam("user") String id, Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        this.userForm = this.userServiceImple.getUserFormByUser(user);

        UpdateUserForm updateUserForm = new UpdateUserForm();
        updateUserForm.setUserName(this.userForm.getUserName());
        updateUserForm.setUserNumber(this.userForm.getUserNumber());
        updateUserForm.setProfilePic(this.userForm.getProfilePic());

        if (this.user.getUserId().equals(id)) {
            model.addAttribute("updateUserForm", updateUserForm);
        }

        return "loggedin/updateProfile";
    }

    // Proceed Update User Data
    /**
     *
     * @param name
     * @param number
     * @param partFile
     * @param authentication
     * @param session
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/do-update")
    public String processUpdate(@Valid @ModelAttribute() UpdateUserForm updateUserForm,
            BindingResult bindingResult, Authentication authentication, HttpSession session, Model model) {

        try {

            this.user = this.userServiceImple.getUserFromLogin(authentication);
            this.userForm = this.userServiceImple.getUserFormByUser(this.user);
            updateUserForm.setProfilePic(this.user.getProfilePic());
            model.addAttribute("user", this.userForm);
            
        
            if (bindingResult.hasErrors()) {
                return "loggedin/updateProfile";
            }

            this.user = this.userServiceImple.updateUser(user, updateUserForm.getUserName(),
            updateUserForm.getUserNumber(),
            updateUserForm.getProfile());

            if (user != null) {

                return "redirect:/scm2/user/profile";
            }

            session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try again after", MessageType.red));

            return "redirect:/scm2/user/profile";

        } catch (ResourceNotFoundException exception) {
            throw new ResourceNotFoundException("we are not able to fatch the resources for this user!!");
        }

    }

    // deleteContact
    /**
     *
     * @param page
     * @param element
     * @param contact
     * @param fromRes
     * @param field
     * @param fieldVal
     * @param from
     * @param session
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/delete-contact")
    public String deleteContact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(name = "element", defaultValue = "2") int element,
            @RequestParam("contact") String contact,
            @RequestParam(value = "fromRes", defaultValue = "view") String fromRes,
            @RequestParam(name = "searchField", defaultValue = "byName") String field,
            @RequestParam(name = "fieldValue", defaultValue = "") String fieldVal,
            @RequestParam(name = "from", defaultValue = "view") String from,
            HttpSession session, Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(contact, this.user);

        if (this.contactServiceImpl.deleteContact(this.contact, this.user)) {
            session.setAttribute("message", new Message("contact deleted Successfull", MessageType.yellow));

            if (element == 1) {
                page -= 1;
            }

            // if Delete in the Search

            if (from.equalsIgnoreCase("search") && element > 1) {
                return "redirect:/scm2/user/search?searchField=" + field + "&fromRes=" + fromRes
                        + "&fieldValue=" + fieldVal;
            }

            return (fromRes.equalsIgnoreCase("view")) ? "redirect:/scm2/user/view-contact?page=" + page
                    : "redirect:/scm2/user/fav-contact?page=" + page;
        }

        session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try agan after", MessageType.red));

        return "redirect:/scm2/user/view-contact?page=" + page;
    }

    // Go to any Specific contact Detail
    /**
     *
     * @param id
     * @param page
     * @param element
     * @param fromRes
     * @param authentication
     * @param session
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/unique")
    public String goUnique(@RequestParam(value = "id") String id, @RequestParam("page") int page,
            @RequestParam(name = "element", defaultValue = "2") int element,
            @RequestParam(name = "fromRes", defaultValue = "view") String fromRes, Authentication authentication,
            HttpSession session, Model model) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(id, this.user);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        model.addAttribute("currentPage", page);
        // after update contact data
        if (session.getAttribute("fromRes") != null) {

            fromRes = session.getAttribute("fromRes").toString();
            element = Integer.parseInt(session.getAttribute("element").toString());
            session.removeAttribute("fromRes");
            session.removeAttribute("element");
        }

        model.addAttribute("element", element);
        model.addAttribute("fromRes", fromRes);

        if (contact != null && contactServiceImpl.contactIsExist(contact, user)) {

            model.addAttribute("contactDetail", this.contact);
            return "loggedin/contactDetail";
        }

        session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try again after!!", MessageType.red));

        return "redirect:/scm2/user/view-contact?page" + page;
    }

    // for update Contact Detail
    /**
     *
     * @param page
     * @param id
     * @param fromRes
     * @param element
     * @param authentication
     * @param model
     * @param httpSession
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/update-contactDetail")
    public String updateContact(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam("contact") String id,
            @RequestParam(name = "fromRes", defaultValue = "view") String fromRes,
            @RequestParam(name = "element", defaultValue = "2") String element, Authentication authentication,
            Model model,
            HttpSession httpSession) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(id, this.user);

        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
        if (this.contactServiceImpl.contactIsExist(this.contact, this.user)) {

            model.addAttribute("contactDetail", this.contactServiceImpl.getContactFormByContact(this.contact));
            model.addAttribute("currentPage", page);
            model.addAttribute("fromRes", fromRes);
            model.addAttribute("element", element);
            return "loggedin/updateContact";
        }

        httpSession.setAttribute("message", new Message("Only access Your own contacts", MessageType.red));

        return "redirect:/scm2/user/view-contact?page" + page;
    }

    // Proceed The contact Update Data
    /**
     * @param contactForm
     * @param bindingResult
     * @param page
     * @param httpSession
     * @param model
     * @param authentication
     * @param fromRes
     * @param element
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/proceed-contactDetail")
    public String proceedContactDetail(@Valid @ModelAttribute("contactDetail") ContactForm contactForm,
            BindingResult bindingResult, @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication, @RequestParam(name = "fromRes", defaultValue = "view") String fromRes,
            @RequestParam(name = "element") String element) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.user);
        if (bindingResult.hasErrors()) {
            return "loggedin/updateContact";
        }

        this.contact = this.contactServiceImpl.updateContact(contactForm, this.user);

        return "redirect:/scm2/user/unique?id=" + this.contact.getContactId() + "&page=" + page + "&element=" + element
                + "&fromRes=" + fromRes;
    }

    // Make contact to favorite or not
    /**
     * @param page
     * @param contact
     * @param fromRes
     * @param from
     * @param element
     * @param fieldVal
     * @param field
     * @param session
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fev")
    public String fevNofev(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam("contact") String contact,
            @RequestParam(name = "fromRes", defaultValue = "view") String fromRes,
            @RequestParam(name = "element", defaultValue = "") int element,
            @RequestParam(name = "searchField", defaultValue = "byName") String field,
            @RequestParam(name = "fieldValue", defaultValue = "") String fieldVal,
            @RequestParam(name = "from", defaultValue = "fev") String from,
            HttpSession session, Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);

        if (this.contactServiceImpl.makeFavoriteById(contact, this.user) != null) {

            if (element == 1 && fromRes.equalsIgnoreCase("fev")) {
                page -= 1;
            }

            if (from.equalsIgnoreCase("search") && (fromRes.equalsIgnoreCase("view") || element > 1)) {
                return "redirect:/scm2/user/search?searchField=" + field + "&fromRes=" + fromRes
                        + "&fieldValue=" + fieldVal;
            }

            // check request from fev or view
            return fromRes.equals("fev") ? "redirect:/scm2/user/fav-contact?page=" + page
                    : "redirect:/scm2/user/view-contact?page=" + page;

        }

        session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try agan after", MessageType.red));

        return "redirect:/scm2/user/view-contact?page=" + page;
    }

    // Make Favorite Any individual
    /**
     * @param id
     * @param page
     * @param element
     * @param fromRes
     * @param authentication
     * @param session
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/unique-fev")
    public String makeUniqueFev(@RequestParam("contact") String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "element", defaultValue = "2") int element,
            @RequestParam(name = "fromRes", defaultValue = "view") String fromRes,
            Authentication authentication,
            HttpSession session,
            Model model) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(id, user);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));
        session.setAttribute("element", element);
        session.setAttribute("fromRes", fromRes);

        if (this.contactServiceImpl.makeFavoriteById(id, this.user) != null) {

            model.addAttribute("contactDetail", this.contact);
            return "redirect:/scm2/user/unique?id=" + id + "&page=" + page + "&element=" + element + "&fromRes="
                    + fromRes;
        }

        session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try again after!!", MessageType.red));

        return "redirect:/scm2/user/unique?id=" + id + "&page=" + page + "&element=" + element + "&fromRes="
                + fromRes;
    }

    // Compose Mail From Unique
    /**
     *
     * @param page
     * @param contact
     * @param fromRes
     * @param element
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/compose")
    public String goComposeMailPage(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam("contact") String contact,
            @RequestParam(name = "fromRes", defaultValue = "view") String fromRes,
            @RequestParam(name = "element", defaultValue = "2") String element,
            Authentication authentication, Model model) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(contact, this.user);

        model.addAttribute("list", Arrays.asList(element, page, fromRes, contact, this.contact.getName()));
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        return "loggedin/composeMail";
    }

    /**
     * @param subject
     * @param message
     * @param session
     * @param file
     * @param page
     * @param contact
     * @param element
     * @param fromRes
     * @param model
     * @param authentication
     * @return
     */
    @PostMapping("/proceedMail")
    public String postMethodName(@RequestParam(name = "subject", defaultValue = "scm2") String subject,
            @RequestParam(name = "message", defaultValue = "message") String message, HttpSession session,
            @RequestParam(name = "file", defaultValue = "Hello") MultipartFile file,
            @RequestParam("page") int page, @RequestParam("contact") String contact,
            @RequestParam("element") int element, @RequestParam("fromRes") String fromRes, Model model,
            Authentication authentication) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        this.contact = this.contactServiceImpl.getContactByIdAndUser(contact, this.user);

        if (this.userServiceImple.sendMail(this.contact.getContactEmail(), this.user.getUserEmail(), subject, message,
                file, this.contact.getName(), this.user.getUserName())) {
            session.setAttribute("message", new Message("mail send successfully", MessageType.blue));
        } else {
            session.setAttribute("message", new Message("INTERNAL_SERVER_ERROR try again after", MessageType.red));

        }

        return "redirect:/scm2/user/unique?id=" + this.contact.getContactId() + "&page=" + page + "&element=" + element
                + "&fromRes="
                + fromRes;
    }

    // Go to the feedback
    /**
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/feedback")
    @PreAuthorize("hasRole('USER')")
    public String gochangeFeedback(Authentication authentication, Model model) {
        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        return "loggedin/feedback";
    }

    // Proceed The Feedback
    /**
     * @param email
     * @param mobile
     * @param query
     * @param authentication
     * @param model
     * @return
     */
    @PostMapping("/sendfeedback")
    public String proceedFeedback(@RequestParam("email") String email, @RequestParam("mobile") String mobile,
            @RequestParam("query") String query, Authentication authentication, Model model, HttpSession httpSession) {

        this.userForm = this.userServiceImple
                .getUserFormByUser(this.userServiceImple.getUserFromLogin(authentication));

        model.addAttribute("user", this.userForm);

        boolean isSend = this.userServiceImple.sendFeedback(query, email, mobile, query);

        if (isSend) {
            httpSession.setAttribute("message", new Message("feedback send successfully", MessageType.blue));
        } else {

            httpSession.setAttribute("message",
                    new Message("INTERNAL_SERVER_ERROR try again after", MessageType.red));
        }

        return "redirect:/scm2/user/feedback";
    }

    // Go to the Change Email
    /**
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/changeMail")
    @PreAuthorize("hasRole('USER')")
    public String gochangeMail(Authentication authentication, Model model) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        return "loggedin/setting/ChangeEmail";
    }

    // Go to the Change password
    /**
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/changePass")
    @PreAuthorize("hasRole('USER')")
    public String gochangePass(Authentication authentication, Model model) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);
        model.addAttribute("user", this.userServiceImple.getUserFormByUser(this.user));

        return "loggedin/setting/ChangePass";
    }

    // Proceed Change Password
    /**
     * @param currentPass
     * @param newPass
     * @param repass
     * @param authentication
     * @param model
     * @param httpSession
     * @return
     */
    @PostMapping("/proceed-ChangePass")
    @PreAuthorize("hasRole('USER')")
    public String croceddChangePass(@RequestParam("currentPass") String currentPass,
            @RequestParam("newPass") String newPass, @RequestParam("re-newPass") String repass,
            Authentication authentication,
            Model model, HttpSession httpSession) {

        this.user = this.userServiceImple.getUserFromLogin(authentication);

        httpSession.setAttribute("message", this.userServiceImple.changePass(user, currentPass, newPass, repass));

        return "redirect:/scm2/user/changePass";
    }

    // Proceed Change Mail
    /**
     *
     * @param currentMail
     * @param newMail
     * @return
     */

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/proceed-changeMail")
    public String chnageMail(@RequestParam("currentMail") String currentMail,
            @RequestParam("newMail") String newMail) {

        return "redirect:/scm2/user/changeMail";
    }

    // Send change Email Verify link

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/proceed-changeMail")
    public String sendVerifyLink(@RequestParam(name = "newMail", defaultValue = "") String newmail,
            @RequestParam(name = "currentMail") String oldMail, HttpSession session) {

        LocalTime localTime = LocalTime.now();

        session.setAttribute("time", localTime);

        if (this.userServiceImple.sendChangeVerifyLink(newmail, oldMail)) {
            session.setAttribute("message", new Message("verification link is send to " + newmail, MessageType.blue));
            return "redirect:/scm2/user/changeMail";
        }

        session.setAttribute("message", new Message("check your mail and verify again " + newmail, MessageType.red));
        return "redirect:/scm2/user/changeMail";
    }

    // Change Email Verify Link

    @GetMapping("/change/verify/email/link/user/byuser")
    public String verifyChangeEmail(@RequestParam(name = "email", defaultValue = "") String newEmail,
            @RequestParam(name = "cMail", defaultValue = "") String oldMail,
            HttpSession httpSession) {

        this.user = this.userServiceImple.getUserByUsername(oldMail);

        LocalTime localTime1 = ((LocalTime) httpSession.getAttribute("time")).plusMinutes(5);
        LocalTime localTime2 = LocalTime.now();

        if (user != null && localTime2.isBefore(localTime1)) {
            if (userServiceImple.changeEmail(this.user, newEmail)) {

                httpSession.setAttribute("message",
                        new Message("email verification is successfull", MessageType.green));
            }
        } else {

            if (localTime2.isAfter(localTime1)) {

                httpSession.setAttribute("message",
                        new Message("verification link expired!!", MessageType.red));
                return "redirect:/scm2/user/changeMail";
            }

            httpSession.setAttribute("message",
                    new Message("email verification failed!!", MessageType.red));

            return "redirect:/scm2/user/changeMail";
        }

        return "redirect:/scm2/user/change";
    }

}
