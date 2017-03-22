package TrungHuynh;

//import weka.core.Attribute;
import weka.core.Instance;

public class Node {

	private String name;
	private int attributeIndex;
	private int rightNode = -1;
	private int leftNode = -1;
	private String condition2;
	private double condition1;
	private String label = null;
	private boolean isLeaf = false;
	private float Gain;
	
//	private String[] attribute= {"Refund","MaritalStatus","Income"};
	// public Node(String name) {
	// // super();
	// this.name = name;
	// }

	// public Node(String name, int rightNode, int leftNode, String condition1,
	// int condition2, String label) {
	// this.name = name;
	// this.rightNode = rightNode;
	// this.leftNode = leftNode;
	// this.condition1 = condition1;
	// this.condition2 = condition2;
	// this.label = label;
	// }

	public int GetNextNode(Instance ins) {

		if (isLeaf)
			return -1; // something goes wrong
		
//		Attribute att = new Attribute(name);
		
		if (condition2 == null) // numerical attribute
		{
			double condi1 = ins.value(attributeIndex);
			if (condi1 < condition1) {
				return leftNode;
			} else {
				return rightNode;
			}

		} else { // String attribute

			String condi2 = ins.stringValue(attributeIndex);
			if (condition2.contains(condi2)) {
				return leftNode;
			} else {
				return rightNode;
			}
		}

	}
	
//	public int GetNextNode1(String condi2, int condi1) {
//
//		if (isLeaf)
//			return -1; // something goes wrong
//		if (condition2 == null) // numerical attribute
//		{
//			if (condi1 < condition1) {
//				return leftNode;
//			} else {
//				return rightNode;
//			}
//
//		} else { // String attribute
//
//			if (condition2.contains(condi2)) {
//				return leftNode;
//			} else {
//				return rightNode;
//			}
//		}
//
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRightNode() {
		return rightNode;
	}

	public void setRightNode(int rightNode) {
		this.rightNode = rightNode;
	}

	public int getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(int leftNode) {
		this.leftNode = leftNode;
	}

	public String getCondition2() {
		return condition2;
	}

	public void setCondition2(String condition2) {
		this.condition2 = condition2;
	}

	public double getCondition1() {
		return condition1;
	}

	public void setCondition1(double condition1) {
		this.condition1 = condition1;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	
	
	public int getAttributeIndex() {
		return attributeIndex;
	}

	public void setAttributeIndex(int attributeIndex) {
		this.attributeIndex = attributeIndex;
	}
	
	

	public float getGain() {
		return Gain;
	}

	public void setGain(float gain) {
		Gain = gain;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", rightNode=" + rightNode + ", leftNode=" + leftNode + ", condition2="
				+ condition2 + ", condition1=" + condition1 + ", label=" + label + ", isLeaf=" + isLeaf + "]";
	}

}
