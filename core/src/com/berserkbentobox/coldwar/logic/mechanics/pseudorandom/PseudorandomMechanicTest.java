package com.berserkbentobox.coldwar.logic.mechanics.pseudorandom;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettings;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicState;

public class PseudorandomMechanicTest {

	PseudorandomMechanic r;

	@Before
	public void setUp() throws Exception {
		PseudorandomMechanic.Settings settings = new PseudorandomMechanic.Settings(
				GameSettings.newBuilder()
					.setPseudorandomSettings(
							PseudorandomMechanicSettings.newBuilder()
								.setInitSeed(0)
								.build())
					.build());
		this.r = new PseudorandomMechanic(null, settings, GameState.newBuilder()
				.setPseudorandomState(
						PseudorandomMechanicState.newBuilder().build())
				.build());
	}
	
	@Test
	public void testHowIRollForNoPossibilities() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		Object result = r.howIRoll(possibilities);
		assertEquals(null, result);
	}

	@Test
	public void testHowIRollForOnePossibilityWithOneSum() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 1);
		Object result = r.howIRoll(possibilities);
		assertEquals(1, result);
	}

	@Test
	public void testHowIRollForOnePossibility() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 5);
		Object result = r.howIRoll(possibilities);
		assertEquals(1, result);
	}

	@Test
	public void testHowIRollForOnePossibilityWithZeroSum() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 0);
		Object result = r.howIRoll(possibilities);
		assertEquals(null, result);
	}
		
	@Test
	public void testHowIRollForOnePossibilityWithNegativeSum() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, -1);
		Object result = r.howIRoll(possibilities);
		assertEquals(null, result);
	}

	@Test
	public void testHowIRollForMultiplePossibilitiesWithZeroSum() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 1);
		possibilities.put(2, -1);
		Object result = r.howIRoll(possibilities);
		assertEquals(1, result);
	}
	
	@Test
	public void testHowIRollForMultiplePossibilitiesWithNegativeSum() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 0);
		possibilities.put(2, -1);
		Object result = r.howIRoll(possibilities);
		assertEquals(null, result);
	}
	
	@Test
	public void testHowIRollForMultiplePossibilities() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 1);
		possibilities.put(2, 1);
		Object result = r.howIRoll(possibilities);
		assertEquals(2, result);  // determined based on the seed of zero.
	}

	@Test
	public void testHowIRollForLotsOfPossibilities() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(1, 1);
		possibilities.put(2, 1);
		possibilities.put(3, 1);
		possibilities.put(4, 1);
		possibilities.put(5, 1);
		possibilities.put(6, 1);
		possibilities.put(7, 20);
		possibilities.put(8, -300);
		Object result = r.howIRoll(possibilities);
		assertEquals(7, result);  // determined based on the seed of zero.
	}
	
	@Test
	public void testHowIRollDistribution() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(0, 1);
		possibilities.put(1, 1);
		int sum = 0;
		for (int i=0; i<10000; i++) {
			sum += (Integer)r.howIRoll(possibilities);
		}
		assertEquals(5000, sum, 50);
	}

	@Test
	public void testHowIRollDistributionTwo() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(0, 1);
		possibilities.put(1, 2);
		int sum = 0;
		for (int i=0; i<10000; i++) {
			sum += (Integer)r.howIRoll(possibilities);
		}
		assertEquals(6666, sum, 50);
	}
	
	@Test
	public void testHowIRollDistributionThree() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(0, 99);
		possibilities.put(1, 1);
		int sum = 0;
		for (int i=0; i<10000; i++) {
			sum += (Integer)r.howIRoll(possibilities);
		}
		assertEquals(100, sum, 50);
	}

	@Test
	public void testHowIRollDistributionFour() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(0, 1);
		possibilities.put(1, 1);
		possibilities.put(2, 1);
		possibilities.put(3, 1);
		possibilities.put(4, 1);
		possibilities.put(5, 1);
		possibilities.put(6, 1);
		possibilities.put(7, 1);
		possibilities.put(8, 1);
		possibilities.put(9, 1);
		int count = 0;
		for (int i=0; i<10000; i++) {
			if ((Integer)r.howIRoll(possibilities) == 0) {
				count++;
			}
		}
		assertEquals(1000, count, 50);
	}
	
	@Test
	public void testHowIRollDistributionFive() {
		HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
		possibilities.put(0, 1);
		possibilities.put(1, 1);
		possibilities.put(2, 1);
		possibilities.put(3, 1);
		possibilities.put(4, 1);
		possibilities.put(5, 1);
		possibilities.put(6, 1);
		possibilities.put(7, 1);
		possibilities.put(8, 1);
		possibilities.put(9, 1);
		possibilities.put(10, -20);
		possibilities.put(11, -20);
		possibilities.put(12, 0);
		int count = 0;
		for (int i=0; i<10000; i++) {
			if ((Integer)r.howIRoll(possibilities) == 0) {
				count++;
			}
		}
		assertEquals(1000, count, 50);
	}
	

	@Test
	public void testHowIRollPerformance() {
		long howIRollTime;
		long rollTime;
		{
			HashMap<Object, Integer> possibilities = new HashMap<Object, Integer>();
			long startTime = System.currentTimeMillis();
			for (int i=0; i<10000; i++) {
				possibilities.put(i, i);
			}
			for (int i=0; i<10000; i++) {
				r.howIRoll(possibilities);
			}
			long finishTime = System.currentTimeMillis();
			howIRollTime = finishTime - startTime;
			System.out.println("howIRoll took: "+(howIRollTime)+ " ms");
		}

		{
			LinkedHashMap<Object, Integer> possibilities = new LinkedHashMap<Object, Integer>();
			long startTime = System.currentTimeMillis();
			for (int i=0; i<10000; i++) {
				possibilities.put(i, i);
			}
			for (int i=0; i<10000; i++) {
				r.roll(possibilities);
			}
			long finishTime = System.currentTimeMillis();
			rollTime = finishTime - startTime;
			System.out.println("roll took: "+(rollTime)+ " ms");
		}
		
		assertTrue(rollTime > howIRollTime);
	}
}
