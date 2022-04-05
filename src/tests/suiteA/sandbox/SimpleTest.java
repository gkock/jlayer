package sandbox;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

@Test(groups = "ignore")
public class SimpleTest {
	
  @Test public void fail() {
	  System.out.println("SimpleTest.fail()");
	  assertEquals(1, 0);
  }
  @Test public void succeed() {
	  System.out.println("SimpleTest.succeed()");
	  assertEquals(1, 1);
  }
  
}
