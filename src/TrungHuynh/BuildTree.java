package TrungHuynh;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class BuildTree {

	public final float threshold = 0.8f;
	private List<Node> tree;
	private Random ran = new Random();
	private final String labelY = "Yes";
	private final String labelN = "No";

	private Slipt finBestSlipt(Instances da, List<Slipt> usedAttribute) {

		int cheatLabel = da.numAttributes() - 1;
		int nextAtrribute = -1;
		double minGini = 2;
		double gini = 2;
		double condi1 = -1;
		String condi2 = null;
		for (int i = 0; i < da.numAttributes() - 1; i++) { // get attribute, and
															// avoid already
															// used ones

			boolean isKeep = false;
			for (int y = 0; y < usedAttribute.size(); y++) {
				if (i == usedAttribute.get(y).getIndex()) {
					isKeep = true;
					break;
				}
			}

			if (isKeep)
				continue; //

			if (i == 2) // numerical attribute
			{
				double[] arr = new double[da.numInstances()];
				for (int y = 0; y < da.numInstances(); y++) {
					arr[y] = da.get(y).value(i);
				} // get all data form instance;

				Arrays.sort(arr);

				double minGiniNumer = 0;
				double avg = Double.MAX_VALUE;
				for (int x = 0; x < arr.length - 1; x++) {
					if (arr[x] == arr[(x + 1)])
						continue;

					avg = (arr[x] + arr[(x + 1)]) / 2;

					double leftNode = 0, rightNode = 0;
					double cheatingLeft = 0, cheatingRight = 0;

					for (int y = 0; y < da.numInstances(); y++) {

						if (da.get(y).value(i) >= avg) {
							++rightNode;
							if (da.get(y).stringValue(cheatLabel).compareTo("Yes") == 0) {
								++cheatingRight;
							}

						} else {

							++leftNode;
							if (da.get(y).stringValue(cheatLabel).compareTo("Yes") == 0) {
								++cheatingLeft;
							}

						}

					}

					minGiniNumer = getGini(leftNode, rightNode, cheatingLeft, cheatingRight);

				}

				if (minGiniNumer < gini) {
					gini = minGiniNumer;
					condi1 = avg;
				}

			} else if (i == 0) { // {Yes, No}

				double leftNode = 0, rightNode = 0;
				double cheatingLeft = 0, cheatingRight = 0;

				for (int y = 0; y < da.numInstances(); y++) {
					if (da.get(y).stringValue(i).compareTo("Yes") == 0) {
						++leftNode;
						if (da.get(y).stringValue(cheatLabel).compareTo("Yes") == 0) {
							++cheatingLeft;
						}

					} else {
						++rightNode;
						if (da.get(y).stringValue(cheatLabel).compareTo("Yes") == 0) {
							++cheatingRight;
						}
					}
				}

				gini = getGini(leftNode, rightNode, cheatingLeft, cheatingRight);
				condi2 = "Yes";

			} else if (i == 1) // {Single, Married, Divorced}
			{
				double single = 0, marr = 0, divor = 0, singleYes = 0, marrYes = 0, divorYes = 0;
				String val;
				String valCheating;
				for (int y = 0; y < da.numInstances(); y++) {
					val = da.get(y).stringValue(i);
					valCheating = da.get(y).stringValue(cheatLabel);
					if (val.compareTo("Single") == 0) {
						++single;

						if (valCheating.compareTo("Yes") == 0) {
							++singleYes;
						}

					} else if (val.compareTo("Married") == 0) {
						++marr;
						if (valCheating.compareTo("Yes") == 0) {
							++marrYes;
						}
					} else if (val.compareTo("Divorced") == 0) { // Divorced
						++divor;
						if (valCheating.compareTo("Yes") == 0) {
							++divorYes;
						}
					}

				}
				// single marry,Divorced
				double firstGini = getGini(single, (marr + divor), singleYes, (marrYes + divorYes));
				// single,marry Divorced
				double secondGini = getGini((single + marr), divor, (singleYes + marrYes), divorYes);
				// single,Divorced marry
				double thirdGini = getGini((single + divor), marr, (singleYes + divorYes), marrYes);

				if (firstGini < gini) {
					gini = firstGini;
					condi2 = "Single";
				}

				if (secondGini < gini) {
					gini = secondGini;
					condi2 = "Divorced";
				}

				if (thirdGini < gini) {
					gini = thirdGini;
					condi2 = "Married";
				}

			}

			if (gini < minGini) {
				minGini = gini;
				nextAtrribute = i;

			}

		}
		Slipt sl = new Slipt();
		sl.setIndex(nextAtrribute);
		sl.setCondi1(condi1);
		if (nextAtrribute == 2) {
			sl.setCondi2(null);
		} else {
			sl.setCondi2(condi2);
		}

		return sl;
	}

	private double getGini(double leftNode, double rightNode, double cheatingLeft, double cheatingRight) {

		// print(leftNode + " " + cheatingLeft + " " + rightNode + " " +
		// cheatingRight);
		double leftGini, rightGini;

		if (leftNode == 0 || cheatingLeft == 0) {
			leftGini = 0;
		}

		else {
			leftGini = 1 - Math.pow(((leftNode - cheatingLeft) / leftNode), 2) - Math.pow((cheatingLeft / leftNode), 2);
			// smaller
			// is
			// better,
			// max
			// is
			// 0.5
		}

		if (rightNode == 0 || cheatingRight == 0) {
			rightGini = 0;
		}

		else {

			rightGini = 1 - Math.pow(((rightNode - cheatingRight) / rightNode), 2)
					- Math.pow((cheatingRight / rightNode), 2);// smaller
			// is
			// better,
			// max
			// is
			// 0.5
		}

		double gini = (leftNode / (leftNode + rightNode)) * leftGini + (rightNode / (leftNode + rightNode)) * rightGini;

		return gini;
	}
	// ++++++++++++ Part Two ++++++++++++

	public float crossValidation(Instances trainingData, Instances testingData, boolean isBestSplit, String lable) {
		// print("testing..............");
		tree = new ArrayList<Node>();
		List<Slipt> usedAttribute = new ArrayList<Slipt>();

		// while (usedAttribute.size() < trainingData.numAttributes() - 1) {
		// Slipt a = findRandomSplit(trainingData, usedAttribute);
		// boolean isAdd = true;
		// for (int i = 0; i < usedAttribute.size(); i++) {
		// if (usedAttribute.get(i).getIndex() == a.getIndex()) {
		// isAdd = false;
		// break;
		// }
		// }
		// if (isAdd)
		// usedAttribute.add(a);
		// }

		// testing.....
		// RandomTree a = new RandomTree();
		// a.buildClassifier(data);
		// print ( a.graph());
		// showTree(a.graph());

		// print(data.size()+" | "+data.numInstances());
		Node root = new Node();
		tree.add(root);
		growthTree(root, trainingData, usedAttribute, isBestSplit);
		showTree(printTree(), lable);

		// for( int i = 0 ; i < data.size(); i++ ){
		//
		// print (classification(data.get(i), tree, 0)+"\n");
		// }

		// print ("validation: "+validation(data,tree));
		return validation(testingData, tree);
	}

	private float validation(Instances ins, List<Node> tr) {
		float accurate = 0.0f;
		int labelIndex = ins.numAttributes() - 1;
		for (int i = 0; i < ins.size(); i++) {

			String result = classification(ins.get(i), tree, 0); // root node
																	// starts at
																	// zero;

			if (ins.get(i).stringValue(labelIndex).compareTo(result) == 0) {
				++accurate;
			}

		}

		return (accurate / (ins.numInstances() * 1f));
	}

	private String classification(Instance ins, List<Node> tr, int nodeIndex) {
		Node node = tr.get(nodeIndex);
		if (node.isLeaf()) {
			return node.getLabel();

		} else {

			int nextNode = node.GetNextNode(ins);
			return classification(ins, tr, nextNode);
		}
	}

	// reference: https://weka.wikispaces.com/Visualizing+a+Tree
	private void showTree(String gra, String lable) {
		JFrame jf = new JFrame(lable);
		jf.setSize(500, 400);
		jf.getContentPane().setLayout(new BorderLayout());
		TreeVisualizer tv = new TreeVisualizer(null, gra, new PlaceNode2());
		jf.getContentPane().add(tv, BorderLayout.CENTER);
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				jf.dispose();
			}
		});

		jf.setVisible(true);
		tv.fitToScreen();
	}

	private String printTree() {
		// print("tree.size(): "+tree.size());
		//
		// for(int i = 0 ; i<tree.size() ; i++)
		// {
		// print(i+" "+tree.get(i).toString());
		//
		// }
		// return null;

		String yn = "YesNo";
		String mar = "Single,Married,Divorced";
		char a = '"';
		Node n;

		String str = "digraph RandomTree { \nedge [style=bold]\n";
		for (int i = 0; i < tree.size(); i++) {
			n = tree.get(i);
			// if(n.getLabel() == null)
			// if(n.getLeftNode() != -1 || n.getRightNode() != -1)
			if (!n.isLeaf())

			{
				str = str + i + " [label=" + a + i + ": " + n.getName() + a + "]\n";
				if (n.getCondition2() == null) {
					str = str + i + "->" + n.getLeftNode() + " [label=" + a + " <" + n.getCondition1() + a + "]\n";
					str = str + i + "->" + n.getRightNode() + " [label=" + a + " >=" + n.getCondition1() + a + "]\n";
				} else {

					str = str + i + "->" + n.getLeftNode() + " [label=" + a + n.getCondition2() + a + "]\n";

					if (yn.contains(n.getCondition2())) {
						str = str + i + "->" + n.getRightNode() + " [label=" + a + yn.replace(n.getCondition2(), "") + a
								+ "]\n";
					} else {
						String la = mar.replace(n.getCondition2(), "");
						la = la.replaceAll(",", "");

						str = str + i + "->" + n.getRightNode() + " [label=" + a + la + a + "]\n";
					}

				}

			} else {
				str = str + i + " [label=" + a + i + " : " + n.getLabel() + a + " shape=box]\n";
			}

		}
		str = str + "\n}\n";
		// print(str);
		return str;
	}

	// part 1
	private void growthTree(Node nod, Instances da, List<Slipt> usedAttribute, boolean isBestSplit) {
		// if(da.numInstances() == 0)
		// return;

		boolean isStop;
		if (isBestSplit) {
			isStop = BestStoppingCond(da);

		} else {
			isStop = RandomStoppingCond(da);

		}

		if (isStop || usedAttribute.size() == (da.numAttributes() - 1)) {
			String lab = majorityVote(da);
			nod.setName(lab);
			nod.setLabel(lab);
			nod.setLeaf(true);
			
		} else {

			Slipt sl;

			if (!isBestSplit) {
				sl = findRandomSplit(da, usedAttribute);
			} else {
				sl = finBestSlipt(da, usedAttribute);
			}
			usedAttribute.add(sl);

			Instances leftDa = new Instances(da, 0, 0);
			Instances rightDa = new Instances(da, 0, 0);
			leftDa.clear();
			rightDa.clear();

			
			nod.setName(da.attribute(sl.getIndex()).name());
			nod.setAttributeIndex(sl.getIndex());
			nod.setLeaf(false);
			Node lNode = new Node();
			Node rNode = new Node();

			if (sl.getCondi2() == null) // numerical value
			{
				nod.setCondition1(sl.getCondi1());
				for (int i = 0; i < da.numInstances(); i++)//
				{
					if (da.instance(i).value(sl.getIndex()) < nod.getCondition1()) {
						leftDa.add(da.get(i));
					} else {

						rightDa.add(da.get(i));

					}
				}
			} else {
				nod.setCondition2(sl.getCondi2()); // string value

				for (int i = 0; i < da.numInstances(); i++) {
					// print("asfsafsafs sadfsf "+sl.getIndex());
					if (da.instance(i).stringValue(sl.getIndex()).contains(nod.getCondition2())) {
						leftDa.add(da.get(i));
					} else {
						rightDa.add(da.get(i));
					}
				}
			}
			
			tree.add(lNode);
			nod.setLeftNode(tree.size() - 1);
			tree.add(rNode);
			nod.setRightNode(tree.size() - 1);
			// int nextAtt = index + 1;
			growthTree(lNode, leftDa, usedAttribute, isBestSplit);
			growthTree(rNode, rightDa, usedAttribute, isBestSplit);

		}

	}

	private boolean IsAttributeContains(List<Slipt> usedAttribute, int index) {

		for (int i = 0; i < usedAttribute.size(); i++) {
			if (usedAttribute.get(i).getIndex() == index)
				return true;
		}
		return false;

	}

	// find random attribute and value of that attribute
	private Slipt findRandomSplit(Instances da, List<Slipt> usedAttribute) { // find
																				// random
																				// best
																				// split


		if (usedAttribute.size() == da.numAttributes() - 1)
			return new Slipt();

		int index = ran.nextInt(da.numAttributes() - 1);
		while (IsAttributeContains(usedAttribute, index)) // random unit reach
															// new
		// attribute;
		{
			index = ran.nextInt(da.numAttributes() - 1);
		}


		int insIndex = ran.nextInt(da.numInstances());
		Slipt sl = new Slipt();
		sl.setIndex(index);
		if (index == 2) {

			sl.setCondi1(da.instance(insIndex).value(index)); // numerical value

		} else {

			sl.setCondi2(da.instance(insIndex).stringValue(index)); // String
																	// value

		}

		return sl;

	}

	private boolean RandomStoppingCond(Instances dataSet) {
		
		if (dataSet.numInstances() == 0)
			return true;

		float yes = 0;
		for (int i = 0; i < dataSet.size(); i++) {
			if (dataSet.get(i).stringValue(3).compareTo(labelY) == 0) {
				++yes;

			}
		}
//		float rate = yes * 1.0f / dataSet.size() * 1.0f;

		// not remove

		if (yes / (dataSet.size() * 1.0f) == 0.5) {
			return false;
		} else {
			return true;
		}

	

	}

	private boolean BestStoppingCond(Instances dataSet) {
		// String lable = "";

		if (dataSet.numInstances() == 0)
			return true;

		float yes = 0;
		for (int i = 0; i < dataSet.size(); i++) {
			if (dataSet.get(i).stringValue(3).compareTo(labelY) == 0) {
				++yes;

			}
		}
		float rate = yes * 1.0f / dataSet.size() * 1.0f;

		if (rate > threshold || rate < (1f - threshold)) {

			return true;

		} else

		{
			return false;
		}

	}

	// private Node CreateNode(String name) {
	//
	// return new Node(name);
	//
	// }

	private String majorityVote(Instances dataSet) {

		int yes = 0;
		for (int i = 0; i < dataSet.size(); i++) {
			if (dataSet.get(i).stringValue(3).compareTo(labelY) == 0) {
				++yes;

			}
		}
		float rate = yes * 1.0f / dataSet.size() * 1.0f;
		// print ("yes: "+yes+ " rate: "+rate+" data size:
		// "+dataSet.size()+"\n");
		if (rate > 0.5f)
			return labelY;
		else
			return labelN;
	}

	// private static Instances readData() throws Exception {
	// BufferedReader reader = new BufferedReader(new
	// FileReader("data//TrainingData.arff"));
	// // BufferedReader reader = new BufferedReader(new
	// // FileReader("data//weather.arff"));
	//
	// ArffReader arff = new ArffReader(reader);
	// Instances dataSet = arff.getData();
	// // print(data.toString());
	// return dataSet;
	// }

	// // create training data set, only apply for the first time.
	// private static void createDataSet() throws IOException {
	// String data = "@relation TrainingData\n\n" + "@attribute Refund {Yes,
	// No}\n"
	// + "@attribute MaritalStatus {Single, Married, Divorced}\n" + "@attribute
	// Income numeric\n"
	// + "@attribute Cheat {Yes, No}\n\n" + "@data\n";
	//
	// String[] arrayRefund = { "Yes", "No" };
	// String[] arrayMary = { "Single", "Married", "Divorced" };
	// int numInstant = 10;
	// Random ran = new Random();
	//
	// for (int i = 0; i < numInstant; i++) {
	// InstanceObject ob = new InstanceObject(arrayRefund[ran.nextInt(2)],
	// arrayMary[ran.nextInt(3)],
	// ThreadLocalRandom.current().nextInt(200, 401),
	// arrayRefund[ran.nextInt(2)]);
	// data = data + ob.getString() + "\n";
	// }
	// // print(data);
	// File myFoo = new File("data\\TrainingData.arff");
	// FileWriter fooWriter = new FileWriter(myFoo, false); // true to append
	// // false to
	// // overwrite.
	// fooWriter.write(data);
	// fooWriter.close();
	// }
	//
	// // simple print out results
	public void print(Object mess) {
		System.out.println(mess.toString());
	}
	//
}

class Slipt {
	private int index = -1; // attribute index
	private double condi1 = -1;
	private String condi2 = null;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getCondi1() {
		return condi1;
	}

	public void setCondi1(double condi1) {
		this.condi1 = condi1;
	}

	public String getCondi2() {
		return condi2;
	}

	public void setCondi2(String condi2) {
		this.condi2 = condi2;
	}

	
}
