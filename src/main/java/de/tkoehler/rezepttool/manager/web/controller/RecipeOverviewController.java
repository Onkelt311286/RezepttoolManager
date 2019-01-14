package de.tkoehler.rezepttool.manager.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.web.model.UrlWrapper;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RecipeOverviewController {
	
	private ManagerService managerService;
	
	public RecipeOverviewController(ManagerService managerService) {
		this.managerService = managerService;
	}

	@PostMapping(value = "/", params = { "overview" })
	public String initializeOverviewPage(final ModelMap model, final HttpSession session) {
		log.info("init recipeOverview.html");
		try {
			List<RecipeEntity> recipes = managerService.showRecipeList();
			log.info("Recipes: " + recipes.size());
			model.addAttribute("recipes", recipes);
		}
		catch (ManagerServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "recipeOverview";
	}
	
	@PostMapping(value = "/recipeOverview", params = { "back" })
	public String backToCreateRecipePage(final UrlWrapper urlWrapper, final ModelMap model, final HttpSession session) {
		return "redirect:/";
	}
}
