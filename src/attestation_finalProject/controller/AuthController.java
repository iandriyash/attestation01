package attestation_finalProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth", description = "Аутентификация и вход")
@Controller
public class AuthController {

    @Operation(summary = "Страница логина", description = "Возвращает шаблон resources/templates/login.html")
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // resources/templates/login.html
    }
}
