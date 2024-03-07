package dat3.recipe.api;

import dat3.recipe.dto.CategoryDto;
import dat3.recipe.service.CategoryService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /*@GetMapping
    public List<String> getAllCategories() {
        return categoryService.getAllCategories();
    }
*/

    //Cache for 1 minutes
    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = categoryService.getAllCategories();

        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic();

        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(categories);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryDto addCategory(@RequestBody CategoryDto request) {
        return categoryService.addCategory(request);
    }

}