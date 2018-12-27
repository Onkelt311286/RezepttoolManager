package de.tkoehler.rezepttool.manager.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImportRecipeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/")
	public String startCreditApplicationProcess(Model model) {
		Recipe recipe = Recipe.builder()
				.url("www.chefkoch.de")
				.build();
		model.addAttribute("recipe", recipe);
		return "createRecipe";
	}
}
