package com.asd.controller;

import com.asd.model.User;
import com.asd.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    /** Guard: must be logged-in ADMIN. Return a redirect string if blocked; otherwise null. */
    private String requireAdmin(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Object role = session.getAttribute("userRole");
        if (!(role instanceof String) || !"ADMIN".equals(role)) {
            return "redirect:/dashboard?error=forbidden";
        }
        return null;
    }

    /** GET /users — list all users (ADMIN only) */
    @GetMapping
    public String listUsers(Model model, HttpSession session,
                            @RequestParam(value = "msg", required = false) String msg) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        // Navbar context
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));

        // Current logged-in ID (to disable self-delete)
        model.addAttribute("currentUserId", session.getAttribute("userId"));

        // Optional flash message
        if (msg != null) {
            model.addAttribute("msg", msg);
        }

        // Add users list
        model.addAttribute("users", users.findAll());
        return "users"; // templates/users.html
    }

    /** GET /users/create — show create form */
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        model.addAttribute("user", new User());
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user-create";
    }

    /** POST /users/create — process form */
    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        if (user.getStatus() == null) user.setStatus(User.Status.ACTIVE);
        if (user.getRole() == null) user.setRole(User.Role.READ_ONLY);

        users.save(user);

        redirectAttributes.addAttribute("msg", "User created successfully!");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, HttpSession session) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        User user = users.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") int id,
                             @ModelAttribute("user") User updatedUser,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        User user = users.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
        user.setStatus(updatedUser.getStatus());

        users.save(user);
        redirectAttributes.addAttribute("msg", "User updated successfully!");
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        // Prevent self-delete
        Integer currentId = (Integer) session.getAttribute("userId");
        if (currentId != null && currentId == id) {
            redirectAttributes.addAttribute("msg", "⚠ You cannot delete your own account!");
            return "redirect:/users";
        }

        users.deleteById(id);
        redirectAttributes.addAttribute("msg", "User deleted successfully!");
        return "redirect:/users";
    }
}


