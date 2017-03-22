package TrungHuynh;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class Main extends Thread {
	static Instances data1, data2;
	static BuildTree tr;
	
	public static void main(String[] str) throws Exception {
		tr = new BuildTree();
		print("++++++++++++  Part One   ++++++++++++\n");
		// create data for training
		print("Create Data Set...\n");
		 createDataSet();
		// read data
		data1 = readData();
		data1.setClassIndex(data1.numAttributes() - 1);
		data2 = new Instances(data1, data1.size() / 2, data1.size() / 2);
		
		int sizeDat = data1.size();

		while (data1.size() != sizeDat / 2) {
			data1.remove(data1.size() - 1);
		}
//		print(data1.size() + " " + data2.size());

		float v1 = tr.crossValidation(data1, data2, false, "Randomized Decision Tree 1");
		float v2 = tr.crossValidation(data2, data1, false, "Randomized Decision Tree 2");
		print("2-fold Cross Validation for Randomized Decision Tree");
		print("Tree 1: "+v1);
		print("Tree 2: "+v2);
		print("average: "+(v1+v2)/2f);

		print("++++++++++++  Part Two   ++++++++++++\n");
		print("Threshold: "+ tr.threshold);
		float v3 = tr.crossValidation(data1, data2, true,"Gain Based Decision Tree 1");
		float v4 = tr.crossValidation(data2, data1, true, "Gain Based Decision Tree 2");
		
		print("2-fold Cross Validation for Gain Based Decision Tree");
		print("Tree 1: "+v3);
		print("Tree 2: "+v4);
		print("average: "+(v3+v4)/2f);
		
	}
	
//	private static void buildTree(Instances data1, Instances data2, boolean isUseBestSplit,String lable){
//		Thread t = new Thread(new Runnable() {
//			public void run() {
//
//				BuildTree tr = new BuildTree();
//				print(tr.crossValidation(data1, data2, isUseBestSplit)); 
//				// isUseBestSplit: false random attribute
//				// isUseBestSplit: true findBestAttribute attribute
//			}
//		});
//		t.start();
//		
//		print(tr.crossValidation(data1, data2, isUseBestSplit, lable)); 
//	}

	private static Instances readData() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("data//TrainingData.arff"));
		// BufferedReader reader = new BufferedReader(new
		// FileReader("data//weather.arff"));

		ArffReader arff = new ArffReader(reader);
		Instances dataSet = arff.getData();
		// print(data.toString());
		return dataSet;
	}

	// create training data set, only apply for the first time.
	private static void createDataSet() throws IOException {
		String data = "@relation TrainingData\n\n" + "@attribute Refund {Yes, No}\n"
				+ "@attribute MaritalStatus {Single, Married, Divorced}\n" + "@attribute Income numeric\n"
				+ "@attribute Cheat {Yes, No}\n\n" + "@data\n";

		String[] arrayRefund = { "Yes", "No" };
		String[] arrayMary = { "Single", "Married", "Divorced" };
		int numInstant = 10;
		Random ran = new Random();

		for (int i = 0; i < numInstant; i++) {
			InstanceObject ob = new InstanceObject(arrayRefund[ran.nextInt(2)], arrayMary[ran.nextInt(3)],
					ThreadLocalRandom.current().nextInt(200, 401), arrayRefund[ran.nextInt(2)]);
			data = data + ob.getString() + "\n";
		}
		// print(data);
		File myFoo = new File("data//TrainingData.arff");
		FileWriter fooWriter = new FileWriter(myFoo, false); // true to append
																// false to
																// overwrite.
		fooWriter.write(data);
		fooWriter.close();
	}

	// simple print out results
	private static void print(Object mess) {
		System.out.println(mess.toString());
	}

}
