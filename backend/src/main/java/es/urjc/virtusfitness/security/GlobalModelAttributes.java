package es.urjc.virtusfitness.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request, Authentication auth) {
        boolean authenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", authenticated);
        model.addAttribute("isAdmin", authenticated && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            model.addAttribute("csrfToken", csrf.getToken());
            model.addAttribute("csrfParameterName", csrf.getParameterName());
        } else {
            model.addAttribute("csrfToken", "");
            model.addAttribute("csrfParameterName", "_csrf");
        }
    }
}
