package cz.cvut.fit.krakovoj;

import static org.junit.Assert.*;

import org.junit.Test;

public class KnapsackTest {

	@Test
	public void testGetShift() {
		Knapsack knapsack = new Knapsack();
		knapsack.addItem(4,1);
		assertSame(knapsack.getShift(1.0),2);
		assertSame(knapsack.getShift(2.0),3);
		assertSame(knapsack.getShift(3.0),3); // 3.584
		knapsack.addItem(6,4);
		assertSame(knapsack.getShift(1.0),1); //1.5849
		assertSame(knapsack.getShift(0.257),0);
		assertSame(knapsack.getShift(0.0),0);
	}
	
	@Test
	public void testItemMaxCost(){
		Knapsack knapsack = new Knapsack();
		knapsack.addItem(4,1);
		knapsack.addItem(5,8);
		knapsack.addItem(2,13);
		knapsack.addItem(3,3);
		assertSame(knapsack.getItemsMaxCost(),5);
	}
	
	@Test
	public void testLogb(){
		assertEquals(Knapsack.logb(2, 3.0),1.5849,0.0001);
	}
	
	@Test
	public void testshiftItemsCost(){
		Knapsack knapsack = new Knapsack();
		knapsack.addItem(15,1);
		knapsack.shiftItemsCost(2);
		assertEquals(knapsack.getItemCost(0),3);
	}

}
