package dat3.recipe.api;

import dat3.recipe.dto.RecipeDto;
import dat3.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAllRecipes(@RequestParam(required = false) String category) {
        if(category != null) {
            System.out.println("Category: " + category);
        }
        return recipeService.getAllRecipes(category);
    }

    @GetMapping(path ="/{id}")
    public RecipeDto getRecipeById(@PathVariable int id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    public RecipeDto addRecipe(@RequestBody RecipeDto request, Principal p) {
        String userName = p.getName();
        return recipeService.addRecipe(request,userName );
    }

    @PutMapping(path = "/{id}")
    public RecipeDto editRecipe(@RequestBody RecipeDto request,@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        RecipeDto existingRecipe = recipeService.getRecipeById(id);
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                existingRecipe.getOwner().equals(userDetails.getUsername())) {
            return recipeService.editRecipe(request,id);
        } else {
            throw new AccessDeniedException("Access is denied");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteRecipe(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        RecipeDto existingRecipe = recipeService.getRecipeById(id);
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                existingRecipe.getOwner().equals(userDetails.getUsername())) {
            return recipeService.deleteRecipe(id);
        } else {
            throw new AccessDeniedException("Access is denied");
        }
    }
}