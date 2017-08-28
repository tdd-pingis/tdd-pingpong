package pingis.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

  User normalUser;
  User protectedUser;

  @Before
  public void setUp() {
    generateSampleData();
  }

  private void generateSampleData() {
    normalUser = new User(1, "Matti", 1);
    protectedUser = new User();
  }

  @Test
  public void testUserName() {
    assertEquals("Matti", normalUser.getName());
  }

  @Test
  public void testUserPoints() {
    final double precision  = 0.001;
    assertEquals(1, normalUser.getPoints(), precision);
  }

  @Test
  public void testEqualsWithExactlySameAttributes() {
    User normalUserVariation = new User(1, "Matti", 1);
    assertEquals(normalUser, normalUserVariation);
    assertTrue(normalUser.hashCode() == normalUserVariation.hashCode());
  }

  @Test
  public void testEqualsWithSameIdDifferentName() {
    User normalUserVariation = new User(1, "VeliMatti", 1);
    assertEquals(normalUser, normalUserVariation);
  }

  @Test
  public void testHashCodeWithSameIdDifferentName() {
    User normalUserVariation = new User(1, "VeliMatti", 1);
    assertTrue(normalUser.hashCode() == normalUserVariation.hashCode());
  }

  @Test
  public void testEqualsWithDifferentUsers() {
    final int normalUserVariationId = 1234;
    User testUserVeliMatti = new User(normalUserVariationId, "VeliMatti", 2);
    assertNotEquals(normalUser, testUserVeliMatti);
  }

  @Test
  public void testHashCodeWithDifferentUsers() {
    final long normalUserVariationId = 1245;
    User normalUser2 = new User(normalUserVariationId, "VeliMatti", 2);
    assertTrue(normalUser.hashCode() != normalUser2.hashCode());
  }

  @Test
  public void testEqualsWithSameNameDifferentId() {
    final long normalUserVariationId = 3;
    User normalUserVariation = new User(normalUserVariationId, "Matti", 1);
    assertNotEquals(normalUser, normalUserVariation);
  }

  @Test
  public void testHashCodeWithSameNameDifferentId() {
    final long normalUserVariationId = 3;
    User normalUserVariation = new User(normalUserVariationId, "Matti", 1);
    assertTrue(normalUserVariation.hashCode() != normalUser.hashCode());
  }

}
