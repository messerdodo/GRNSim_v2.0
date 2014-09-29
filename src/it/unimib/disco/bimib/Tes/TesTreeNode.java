/**
 * 
  * This class is a Tes tree node. It is used as a brick for the Tes Tree 
 * 
 * @author Andrea Paroni (a.paroni@campus.unimib.it)
 * @author Giorgia Previtali (g.previtali6@campus.unimib.it)
 * @group BIMIB @ Disco (Department of Information Technology, Systems and Communication) of Milan University - Bicocca
 * @year 2013
 * 
 */
package it.unimib.disco.bimib.Tes;

//System imports
import java.util.ArrayList;
//GRNSim imports
import it.unimib.disco.bimib.Exceptions.TesTreeException;

public class TesTreeNode {

	private int nodeId;
	private Tes tes;
	private TesTreeNode parent;
	private ArrayList<TesTreeNode> children;


	/**
	 * Generic constructor
	 * @param tes: The Tes
	 * @param nodeId: Node identifier
	 */
	public TesTreeNode(Tes tes, int nodeId) {
		this.tes = tes;
		this.parent = null;
		this.children = new ArrayList<TesTreeNode>();
		this.setNodeId(nodeId);
	}

	/**
	 * nodeId getter
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * nodeId setter
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Tes getter
	 * @return the Tes
	 */
	public Tes getTes() {
		return tes;
	}

	/**
	 * Tes setter.
	 * @param Tes the Tes to set. It must not be null
	 */
	public void setTes(Tes tes) {
		if(tes != null)
			this.tes = tes;
	}

	/**
	 * This method returns the parent of the given node
	 * @return the parent node
	 */
	public TesTreeNode getParent() {
		return parent;
	}

	/**
	 * parent setter
	 * @param parent the parent to set
	 */
	public void setParent(TesTreeNode parent) {
		this.parent = parent;
	}


	/**
	 * Children getter
	 * @return all the children as an array list
	 */
	public ArrayList<TesTreeNode> getChildren() {
		return children;
	}

	/**
	 * Children getter
	 * @return all the children as an array
	 */
	public TesTreeNode[] getChildrenAsArray() {
		TesTreeNode[] childrenArray = new TesTreeNode[this.children.size()];
		for(int i = 0; i < this.children.size(); i++)
			childrenArray[i] = this.children.get(i);
		return childrenArray;
	}
	
	/**
	 * children setter
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<TesTreeNode> children) {
		this.children = children;
	}

	/**
	 * This method adds a child to this node
	 */
	public void addChild(TesTreeNode tesNode){
		if(tesNode != null){
			this.children.add(tesNode);
			tesNode.setParent(this);
		}
	}

	/**
	 * This method returns the child at the specified index
	 * @param index
	 * @return child
	 * @throws Exception
	 */
	public TesTreeNode getChild(int index) throws TesTreeException{
		if(index < 0 || index >= this.children.size()){
			throw new TesTreeException("the index of the child isn't correct");
		}
		return this.children.get(index);
	}

}
