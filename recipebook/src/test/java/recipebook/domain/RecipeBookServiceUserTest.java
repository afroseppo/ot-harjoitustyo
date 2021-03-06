/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.domain;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiitinha
 */
public class RecipeBookServiceUserTest {

    private RecipebookService recipebook;
    private FakeUserDao userDao;
    private FakeRecipeDao recipeDao;
    private User user1;

    @Before
    public void SetUp() {
        userDao = new FakeUserDao();
        recipeDao = new FakeRecipeDao();

        user1 = new User("Testi", "salasana");
        userDao.createUser(user1);

        recipebook = new RecipebookService(recipeDao, userDao);
    }

    @Test
    public void loginReturnsTrueIfUserExists() {
        assertTrue(recipebook.login("Testi", "salasana"));
    }

    @Test
    public void loginReturnsFalseIfUserDoesntExist() {
        assertFalse(recipebook.login("Testi2", "salasana"));
    }

    @Test
    public void getLoggedUserReturnsTheCorrectUser() {
        recipebook.login("Testi", "salasana");
        assertEquals(user1, recipebook.getLoggedUser());
    }

    @Test
    public void createUserReturnsTrueIfLegitNameAndPassword() {
        assertTrue(recipebook.createUser("test2", "salasana"));
    }

    @Test
    public void loggingOutSetsLoggedUserToNull() {
        recipebook.login("Testi", "salasana");
        assertEquals("Testi", recipebook.getLoggedUser().getName());
        recipebook.logout();
        assertEquals(null, recipebook.getLoggedUser());
    }
    
    @Test
    public void connectToDatabaseReturnsTrueIfSuccessful() {
        assertTrue(recipebook.connectToDatabase("./src/test/resources/database"));
    }

}
