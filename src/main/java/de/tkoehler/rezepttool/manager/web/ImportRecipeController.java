package de.tkoehler.rezepttool.manager.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImportRecipeController {

	private RecipeRepository recipeRepository;
	private ImporterService importerService;

	public ImportRecipeController(RecipeRepository recipeRepository, ImporterService importerService) {
		this.recipeRepository = recipeRepository;
		this.importerService = importerService;
	}

	@GetMapping("/")
	public String index() {
		recipeRepository.findAll();
		log.info("Loading Index.html");
		return "index";
	}

	@PostMapping("/")
	public String startCreditApplicationProcess(ModelMap model) {
		log.info("Loading createRecipe.html");
		Recipe recipe = Recipe.builder()
				.url("https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html")
				.build();
		model.addAttribute("recipe", recipe);
		return "createRecipe";
	}

	@RequestMapping(value = "/createRecipe", params = { "load" })
	public String testSubmit(final Recipe recipe, final BindingResult bindingResult, final ModelMap model) {
		log.info("Running testSubmit");
		if (bindingResult.hasErrors()) { return "index"; }
		log.info(recipe.getUrl());
		try {
			Recipe loadedRecipe = importerService.loadRecipe(recipe.getUrl());

			model.addAttribute("recipe", loadedRecipe);
			log.info(loadedRecipe.toString());

			for (RecipeIngredient ingred : loadedRecipe.getIngredients()) {
				log.info(ingred.toString());
			}

		}
		catch (ImporterServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "createRecipe";
	}

	@PostMapping("/loading")
	public String startLoading(ModelMap model) {
		log.info("Loading loading.html");
		return "loading";
	}

	@RequestMapping(value = "/createRecipe", params = { "save" })
	public String saveRecipe(final Recipe recipe, final BindingResult bindingResult, final ModelMap model) {
		
		
		
		log.info("saving");
		
		log.info(recipe.toString());
		
		for (RecipeIngredient ingred : recipe.getIngredients()) {
			log.info(ingred.toString());
		}
		
		return "loading";
	}

	@RequestMapping(value = "/createRecipe", params = { "addIngredient" })
	public String addIngredient(final Recipe recipe, final BindingResult bindingResult) {
		recipe.addRecipeIngredient(new RecipeIngredient());
		return "createRecipe";
	}

	@RequestMapping(value = "/createRecipe", params = { "removeIngredient" })
	public String removeIngredient(final Recipe recipe, final BindingResult bindingResult, final HttpServletRequest req) {
		final int rowId = Integer.valueOf(req.getParameter("removeIngredient"));
		recipe.getIngredients().remove(rowId);
		return "createRecipe";
	}

	@RequestMapping(value = "/createRecipe", params = { "addCategory" })
	public String addCategroy(final Recipe recipe, final BindingResult bindingResult) {
		recipe.getCategories().add("");
		return "createRecipe";
	}

	@RequestMapping(value = "/createRecipe", params = { "removeCategory" })
	public String removeCategory(final Recipe recipe, final BindingResult bindingResult, final HttpServletRequest req) {
		final int rowId = Integer.valueOf(req.getParameter("removeCategory"));
		recipe.getCategories().remove(rowId);
		return "createRecipe";
	}
}
