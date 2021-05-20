package com.bogdanbrl.recipe.services;

import com.bogdanbrl.recipe.commands.IngredientCommand;
import com.bogdanbrl.recipe.converters.IngredientCommandToIngredient;
import com.bogdanbrl.recipe.converters.IngredientToIngredientCommand;
import com.bogdanbrl.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.bogdanbrl.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.bogdanbrl.recipe.domain.Ingredient;
import com.bogdanbrl.recipe.domain.Recipe;
import com.bogdanbrl.recipe.repositories.RecipeRepository;
import com.bogdanbrl.recipe.repositories.reactive.RecipeReactiveRepository;
import com.bogdanbrl.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeReactiveRepository, recipeRepository, unitOfMeasureReactiveRepository);
    }

    @Test
    void findByRecipeIdAndId() throws Exception {
    }

    @Test
    void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        Mockito.when(recipeRepository.findById(ArgumentMatchers.anyString())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3").block();

        //when
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }


    @Test
    void testSaveRecipeCommand() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");

        Mockito.when(recipeRepository.findById(ArgumentMatchers.anyString())).thenReturn(recipeOptional);
        Mockito.when(recipeRepository.save(ArgumentMatchers.any())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
        Mockito.verify(recipeRepository, Mockito.times(1)).save(ArgumentMatchers.any(Recipe.class));

    }

    @Test
    void testDeleteById() throws Exception {
        //given
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        Mockito.when(recipeRepository.findById(ArgumentMatchers.anyString())).thenReturn(recipeOptional);

        //when
        ingredientService.deleteById("1", "3");

        //then
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
        Mockito.verify(recipeRepository, Mockito.times(1)).save(ArgumentMatchers.any(Recipe.class));
    }
}
