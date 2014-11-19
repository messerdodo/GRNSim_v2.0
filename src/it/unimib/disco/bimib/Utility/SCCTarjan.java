package it.unimib.disco.bimib.Utility;

import java.util.*;

//optimized version of http://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm
public class SCCTarjan {

	ArrayList<ArrayList<Integer>> graph;
	boolean[] visited;
	Stack<Integer> stack;
	int time;
	int[] lowlink;
	ArrayList<ArrayList<Integer>> components;

	public ArrayList<ArrayList<Integer>> scc(ArrayList<ArrayList<Integer>> graph) {
		int n = graph.size();
		this.graph = graph;
		visited = new boolean[n];
		stack = new Stack<Integer>();
		time = 0;
		lowlink = new int[n];
		components = new ArrayList<ArrayList<Integer>>();

		for (int u = 0; u < n; u++)
			if (!visited[u])
				dfs(u);

		return components;
	}

	void dfs(int u) {
		lowlink[u] = time++;
		visited[u] = true;
		stack.add(u);
		boolean isComponentRoot = true;

		for (int v : graph.get(u)) {
			if (!visited[v])
				dfs(v);
			if (lowlink[u] > lowlink[v]) {
				lowlink[u] = lowlink[v];
				isComponentRoot = false;
			}
		}

		if (isComponentRoot) {
			ArrayList<Integer> component = new ArrayList<Integer>();
			while (true) {
				int x = stack.pop();
				component.add(x);
				lowlink[x] = Integer.MAX_VALUE;
				if (x == u)
					break;
			}
			components.add(component);
		}
	}
	


	// Usage example
	public static void main(String[] args) {
		ArrayList<ArrayList<Integer>> g = new ArrayList<ArrayList<Integer>>(3);
		for (int i = 0; i < 3; i++)
			g.add(new ArrayList<Integer>());

		g.get(2).add(0);
		g.get(2).add(1);
		g.get(0).add(1);
		g.get(1).add(0);
		g.get(1).add(2);
		

		ArrayList<ArrayList<Integer>> components = new SCCTarjan().scc(g);
		System.out.println(components);
	}
}