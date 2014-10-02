package it.unimib.disco.bimib.Tes;

import it.unimib.disco.bimib.Exceptions.TesTreeException;

import org.junit.Test;

public class TesTreeTest {

	public TesTreeTest() {
	}

	@Test
	public void test() throws TesTreeException {
		TesTree current = new TesTree(0);
		TesTree given = new TesTree(0);
		
		 System.out.println("First tree created");
			current.addNodeManually(1, 1, 0);
			current.addNodeManually(2, 1, 0);
			current.addNodeManually(3, 2, 1);
			current.addNodeManually(4, 3, 3);
			current.addNodeManually(5, 2, 2);
			current.addNodeManually(6, 3, 5);
			current.addNodeManually(7, 2, 1);
			current.addNodeManually(8, 3, 7);
			current.addNodeManually(9, 3, 7);
			System.out.println("Second tree created");
			given.addNodeManually(1, 1, 0);
			given.addNodeManually(2, 1, 0);
			given.addNodeManually(3, 2, 1);
			given.addNodeManually(4, 2, 2);
			given.addNodeManually(5, 2, 2);
			given.addNodeManually(6, 3, 3);
			given.addNodeManually(7, 3, 4);
			given.addNodeManually(8, 3, 4);
			given.addNodeManually(9, 3, 5);
			System.out.println(TesTree.getNumberOfNodes(current.getRoot()));
			System.out.println(TesTree.getNumberOfNodes(given.getRoot()));
			System.out.println("Comparison " + current.tesTreeCompare(given));
			
	}

}
