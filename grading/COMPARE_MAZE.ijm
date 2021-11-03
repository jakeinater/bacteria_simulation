
// throw error when wrong image is used
//if (getHeight() != 402 && getWidth() != 402) {
//	exit("image is not correct dimensions, requires 402x402 image");
//}

//this is the folder that will be checked for the files you specify in the dialog box
Dialog.create("WARNING");
Dialog.addMessage("Warning", 12, "#ff0000");
Dialog.addMessage("Using this macro will clear your measurements and loaded ROI selections");
Dialog.addCheckbox("continue?", false);
Dialog.show();
cont = Dialog.getCheckbox();
if (!cont) {
	exit();
}

//delete old data
run("Clear Results");
//roiManager("Select", newArray(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81));
//roiManager("Clear Results");


path = getDirectory("Choose a directory containing images and ROIs");

Dialog.create("Choose images you want to compare and thier respective ROI.zip files");
Dialog.addString("Image 1:", "test.jpg");
Dialog.addString("ROI 1:", "RoiSet.zip");

Dialog.addString("Image 2:", "image 2.png");
Dialog.addString("ROI 2:", "roi 2.zip");

Dialog.addCheckbox("Use prerecorded reference?", true);
Dialog.addToSameRow();
Dialog.addString("Reference file:", "ref.txt");
//TODO: add ability to choose between using mean, max, min
Dialog.show();

image1 = path + Dialog.getString();
roi1 = path + Dialog.getString();
image2 = path + Dialog.getString();
roi2 = path + Dialog.getString();
ref = path + Dialog.getString();
use_ref = Dialog.getCheckbox();

if (use_ref) {
		//open file1 and load roi1, then call measure, then use getResults to get the values. 
		print(image1);
		open(image1);
		roiManager("Open", roi1);
		roiManager("Show All");
		roiManager("Measure");
		ref_means = split(File.openAsString(ref), '\n');
		sum_diff = 0;
		for (i = 0; i < 82; i++)
		{
			sim_mean = getResult("Mean", i);
			ref_mean = parseFloat(ref_means[i]);
			diff = abs(sim_mean-ref_mean);
			print(diff);
			sum_diff += diff;
		}
} else {
	exit("not implemented");
}

print("sum diff:" + sum_diff);
run("Clear Results");
roiManager("Delete");
close();


