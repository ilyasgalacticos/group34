package kz.iitu.csse.group34.controllers;

import kz.iitu.csse.group34.entities.Items;
import kz.iitu.csse.group34.entities.Users;
import kz.iitu.csse.group34.repositories.ItemsRepository;
import kz.iitu.csse.group34.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/")
    public String index(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page){

        int pageSize = 10;

        if(page<1){
            page = 1;
        }

        int totalItems = itemsRepository.countAllByDeletedAtNull();
        int tabSize = (totalItems+pageSize-1)/pageSize;

        Pageable pageable = PageRequest.of(page-1, pageSize);
        List<Items> items = itemsRepository.findAllByDeletedAtNull(pageable);
        model.addAttribute("itemler", items);
        model.addAttribute("tabSize", tabSize);
        return "index";
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String add(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "price") int price
    ){

        Items item = new Items(name, price);
        itemsRepository.save(item);

        return "redirect:/";
    }

    @GetMapping(path = "/details/{id}")
    public String details(ModelMap model, @PathVariable(name = "id") Long id){

        Optional<Items> item = itemsRepository.findByIdAndDeletedAtNull(id);
        model.addAttribute("item", item.orElse(new Items("No Name", 0)));

        return "details";
    }

    @PostMapping(path = "/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String delete(@RequestParam(name = "id") Long id){
        Items item = itemsRepository.findByIdAndDeletedAtNull(id).get();
        item.setDeletedAt(new Date());
        itemsRepository.save(item);
        return "redirect:/";
    }

    @GetMapping(path = "/login")
    public String loginPage(Model model){

        return "login";

    }

    @GetMapping(path = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){

        model.addAttribute("user", getUserData());
        return "profile";

    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String usersPage(Model model){

        model.addAttribute("user", getUserData());

        List<Users> users = userRepository.findAll();
        model.addAttribute("userList", users);

        return "users";

    }

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByEmail(secUser.getUsername());
        }
        return userData;
    }

}