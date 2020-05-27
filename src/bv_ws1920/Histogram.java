// BV Ue4 WS2019/20 Vorgabe
//
// Copyright (C) 2019 by Klaus Jung
// All rights reserved.
// Date: 2019-05-12

package bv_ws1920;

import bv_ws1920.ImageAnalysisAppController.StatsProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.image.Raster;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Histogram {

	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    private int maxHeight;
    private int pixelCount;
	double level;
	int min, max;
	int sum;
	double mean;
	int medSum;
	int median;
	double variance;
	double entropy;


	public int[] getHistogram() {
		return histogram;
	}

	public int getPixelCount() {return pixelCount;}

	private int[] histogram = new int[grayLevels];

	public Histogram(GraphicsContext gc, int maxHeight) {
		this.gc = gc;
		this.maxHeight = maxHeight;
	}
	
	public void update(RasterImage image, Point2D ellipseCenter, Dimension2D ellipseSize, int selectionMax, ObservableList<StatsProperty> statsData) {
		// TODO: calculate histogram[] out of the gray values of the image for pixels inside the given ellipse
		for(int index = 0; index < grayLevels; index++) {
			histogram[index] = 0;
		}
		pixelCount = 0;



//		int minimum = 255, maximum = 0, mean = 0, varianz = 0, median = 0, entropie = 0;

		double ellCenX = ellipseCenter.getX();
		double ellCenY = ellipseCenter.getY();
		double ellHandX = ellipseSize.getWidth() / 2;
		double ellHandY = ellipseSize.getHeight() / 2;

		for(int x = 0; x < image.width;x++) {
			for(int y = 0; y < image.height;y++) {
				int position = y * image.width + x;
				if(isInArea(x, y, ellCenX, ellCenY, ellHandX, ellHandY)) {
					int color = getColor(image.argb[position]);
					histogram[color]++;
					pixelCount++;
				}
			}
		}
		printStatisticOnGUI(image, selectionMax, statsData);
		// Remark: Please ignore selectionMax and statsData in Exercise 4. It will be used in Exercise 5.
	}

	private void printStatisticOnGUI(RasterImage image, int selectionMax, ObservableList<StatsProperty> statsData) {
		level = 0;
		min = 0;
		max = 0;
		sum = 0;
		mean = 0;
		medSum = 0;
		median = 0;
		variance = 0;
		entropy = 0;

		for(int index = 0; index < histogram.length;index++) {

			if(index <= selectionMax)
			{
				sum = sum + histogram[index];
			}

			//Get Mean Color
			mean = mean + (double)(histogram[index] * index);

			//Get the median
			medSum = medSum + histogram[index];
			if(
					((medSum-histogram[index]) * 100.00) / image.argb.length < 50.00
							&&
							((medSum * 100.00) / image.argb.length) >= 50.00
			) {
				median = index;
			}

			//Get entropy
			if (histogram[index] != 0) {
				double pj = pixelCount/histogram[index];
				entropy = entropy + -1 * ((1 / pj) * (((-1) * Math.log(pj)) / Math.log(2)));
			}
		}

		for (StatsProperty property : statsData) {
			switch (property) {
				case Level:
					level = (sum * 100.00) / pixelCount / 100.00;
					property.setValueInPercent(level);
					break;
				case Minimum:
					for (int index = 0; index < histogram.length; index++) {
						if(histogram[index] != 0){
							min = index;
							property.setValue(min);
							break;
						}
					}
					break;
				case Maximum:
					for(int index = histogram.length - 1; index >= 0; index--) {
						if(histogram[index] != 0) {
							max = index;
							property.setValue(max);
							break;
						}
					}
					break;
				case Mean:
					mean = mean/pixelCount;
					property.setValue(mean);
					break;
				case Median:
					property.setValue(median);
					break;
				case Variance:
					for (int index = 0; index < histogram.length; index++) {
						variance = variance + ((index - mean) * (index - mean) )* histogram[index]/pixelCount;
					}
					property.setValue(variance);
					break;
				case Entropy:
					property.setValue(entropy);
					break;
			}
		}
	}
	/**
	 * Get color blue of 32 bits color,
	 * for grayscale, r = g = b
	 * @param colors - 32 bits of integer defining a color
	 * @return blue bits of colors
	 */
	private int getColor (int colors) {
		return (int)(colors & 0xff);
	}

	/**
	 * Chekcs wheter a point is inside an ellipse or not
	 * @param x x-coordinate of current point
	 * @param y y-coordinate of current point
	 * @param centerX x-coordinate of ellipse center
	 * @param centerY y-coordinate of ellipse center
	 * @param rx x radius of ellipse
	 * @param ry y radius of ellipse
	 * @return true if is in area
	 */
	public boolean isInArea(int x, int y, double centerX, double centerY, double rx, double ry) {
		double formula =
				((x - centerX) * (x - centerX)
						/ (rx * rx))
						+
				((y - centerY) * (y - centerY)
						/ (ry * ry));
//		System.out.println(formula);
		if(formula <= 1) {return true;}
		return false;
	}
	    
	public void draw() {
		gc.clearRect(0, 0, grayLevels, maxHeight);
		gc.setLineWidth(1);

		// TODO: draw histogram[] into the gc graphic context

		// Remark: This is some dummy code to give you an idea for graphics drawing
		double shift = 0.5;
		int highestPixel = 200;
		// note that we need to add 0.5 to all coordinates to get a one pixel thin line
		gc.setStroke(Color.BLACK);
		int maxValue = max(histogram);
		for(int index = 0; index < grayLevels; index++) {
			if (maxValue != 0)
				gc.strokeLine(index + shift, highestPixel - (int) (histogram[index] * highestPixel / maxValue), index + shift, 200 + shift);
		}
	}

	/**
	 * Gets max value of an array
	 * Source :
	 * https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
	 *
	 * @param array
	 * @return max value of the whole array
	 */
	public int max(int[] array) {
		return Arrays.stream(array).max().getAsInt();
	}
    
}
