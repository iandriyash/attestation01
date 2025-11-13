package attestation_finalProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Home", description = "Главная страница приложения")
@Controller
public class HomeController {

    @Operation(summary = "Главная страница",
            description = "Показывает главную страницу пиццерии после входа")
    @GetMapping("/")
    public String homePage() {
        // resources/templates/index.html
        return "index";
    }
}
