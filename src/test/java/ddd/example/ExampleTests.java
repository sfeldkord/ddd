package ddd.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ddd.example.atm.AtmTest;
import ddd.example.shared.DollarsTest;
import ddd.example.shared.EurosTest;
import ddd.example.snackmachine.SnackMachineTest;

@RunWith(Suite.class)
@SuiteClasses(value = {
		EurosTest.class,
		DollarsTest.class,
		SnackMachineTest.class,
		AtmTest.class
})
public class ExampleTests {

}
