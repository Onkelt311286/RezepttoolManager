package de.tkoehler.rezepttool.manager.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OldRecipeOverviewController {

	private ManagerService managerService;

	public OldRecipeOverviewController(ManagerService managerService) {
		this.managerService = managerService;
	}

	@PostMapping(value = "/recipeOverview", params = { "overview" })
	public String initializeOverviewPage(final ModelMap model, final HttpSession session) {
		log.info("init recipeOverview.html");
		try {
			loadRecipes(model, session);
		}
		catch (ManagerServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "recipeOverview";
	}

	private void loadRecipes(final ModelMap model, final HttpSession session) throws ManagerServiceException {
		List<TinyRecipe> recipes = managerService.showRecipeList();
		log.info("Recipes: " + recipes.size());
		session.setAttribute("loaded", false);
		model.addAttribute("recipes", recipes);
		model.addAttribute("filter", "filter");
	}

	@PostMapping(value = "/recipeOverview", params = { "back" })
	public String backToIndexPage(final ModelMap model, final HttpSession session) {
		if ((boolean) session.getAttribute("loaded")) {
			try {
				loadRecipes(model, session);
			}
			catch (ManagerServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "recipeOverview";
		}
		else return "redirect:/";
	}

	@PostMapping(value = "/recipeOverview", params = { "filter" })
	public String filterRecipeOverview(final HttpServletRequest req, final String filter, final ModelMap model, final HttpSession session) {
		log.info("Filter: " + filter);
		log.info("FilterReq: " + req.getParameter("filter"));
		try {
			loadRecipes(model, session);
			managerService.filterRecipeList();
		}
		catch (ManagerServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "recipeOverview";
	}

	@PostMapping(value = "/recipeOverview", params = { "editRecipe" })
	public String editRecipe(final HttpServletRequest req, ModelMap model, HttpSession session) {
		log.info("editRecipe: " + req.getParameter("editRecipe"));
		String recipeId = req.getParameter("editRecipe");
		try {
			RecipeWebInput recipe = managerService.editRecipe(recipeId);
			model.addAttribute("recipe", recipe);
			session.setAttribute("loaded", true);
		}
		catch (ManagerServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "recipeOverview";
	}

	@PostMapping(value = "/recipeOverview", params = { "deleteRecipe" })
	public String deleteRecipe(final HttpServletRequest req, ModelMap model) {
		log.info("deleteRecipe: " + req.getParameter("deleteRecipe"));
		return "recipeOverview";
	}
}
