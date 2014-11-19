package it.unimib.disco.bimib.Atms;


import org.junit.Test;

public class AtmTest {

	public AtmTest() {
	}

	@Test
	public void test() {
		double[][] a = new double[7][7];
		for(int i = 0; i < 7; i ++)
			for(int j = 0; j < 7; j++)
				a[i][j] = 0.0;

		a[0][1] = 1.0;
		a[1][2] = 1.0;
		a[1][3] = 1.0;
		a[2][0] = 1.0;
		a[3][4] = 1.0;
		a[4][3] = 1.0;
		a[5][6] = 1.0;
		a[6][5] = 1.0;
		
		Atm atm = new Atm(a);
		
		System.out.println(atm.getTesNumber(0.4));
	}

}
