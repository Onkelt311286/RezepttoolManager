package de.tkoehler.rezepttool.manager.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RecipeOverviewController {

	private ManagerService managerService;

	public RecipeOverviewController(ManagerService managerService) {
		this.managerService = managerService;
	}

	@PostMapping(value = "/recipeOverview", params = { "overview" })
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
	public String backToIndexPage(final ModelMap model, final HttpSession session) {
		return "redirect:/";
	}

	@PostMapping(value = "/recipeOverview", params = { "newRecipe" })
	public String forwardToCreateRecipePage(final ModelMap model, final HttpSession session) {
		return "createRecipe";
	}

	@RequestMapping(value = "/recipeOverview", params = { "showRecipe" })
	public String showRecipe(final HttpServletRequest req, ModelMap model) {
		log.info("showRecipe: " + req.getParameter("showRecipe"));
		return "recipeOverview";
	}

	@PostMapping(value = "/recipeOverview", params = { "editRecipe" })
	public String editRecipe(final HttpServletRequest req, ModelMap model) {
		log.info("editRecipe: " + req.getParameter("editRecipe"));
		return "recipeOverview";
	}

	@PostMapping(value = "/recipeOverview", params = { "deleteRecipe" })
	public String deleteRecipe(final HttpServletRequest req, ModelMap model) {
		log.info("deleteRecipe: " + req.getParameter("deleteRecipe"));
		return "recipeOverview";
	}
}
