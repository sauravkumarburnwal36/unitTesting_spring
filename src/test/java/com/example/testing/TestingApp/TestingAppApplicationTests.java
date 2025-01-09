package com.example.testing.TestingApp;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestingAppApplicationTests.class)
@Slf4j
class TestingAppApplicationTests {


	@BeforeAll
	static void setupOnce(){
		log.info("Setting up at once");
	}

	@AfterAll
	static void tearDownAll(){
		log.info("Tearing down after all test method");
	}

	@BeforeEach
	void setupBeforeEachTestMethod(){
		log.info("Setting up before each test method");
	}

	@AfterEach
	void tearDownAfterEachTestMethod(){
		log.info("Tearing down after each test method");
	}

	@Test
	@DisplayName("TestingMethodTwo")
	void testMethodTwo(){
		log.info("Test Method Two");
	}

	@Test
	void testTwoNumber(){
		int a=5;
		int b=3;
		int result=addTwoNumbers(a,b);
//		Assertions.assertEquals(8,result);
//		assertThat(result).isEqualTo(8).isCloseTo(12, Offset.offset(1));
		assertThat("Apple").isEqualTo("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);
	}

	private int addTwoNumbers(int a, int b) {
		return a+b;
	}

	@Test
	void testDivideTwoNumbers_whenDenominatorIsZero_ThenThrowArithmeticException(){
		int a=5;
		int b=0;
		Assertions.assertThatThrownBy(()->divideTwoNumbers(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");
	}
	double divideTwoNumbers(int a,int b){
		try{
			return a/b;
		}
		catch(ArithmeticException e){
			log.error("Arithmetic Exxception Occured:{}",e.getLocalizedMessage());
			throw new ArithmeticException("Tried to divide by zero");
		}
	}

}
