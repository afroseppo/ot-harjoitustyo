/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.Domain;

import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiitinha
 */
public class RecipeBookServiceTest {
    
    private RecipeBookService recipebook;
    private FakeUserDao userDao;
    private FakeRecipeDao recipeDao;
    
    @Before
    public void SetUp() {
        userDao = new FakeUserDao();
        recipeDao = new FakeRecipeDao();
        
        User user1 = new User("Testi", "salasana");
        userDao.createUser(user1);
        
        recipebook = new RecipeBookService(recipeDao, userDao);
    }
    
    @Test
    public void creatingNewRecipeReturnsTrue() {
        assertTrue(recipebook.createNewRecipe("resepti"));
    }
    
}