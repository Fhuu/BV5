// BV Ue4 WS2019/20 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16

package bv_ws1920;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ToneCurve {
	
	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    
    private int brightness = 0;
    private double gamma = 1.0;
    private double contrast = 1.0;

    private int[] grayTable = new int[grayLevels];

    public ToneCurve(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public void setBrightness(int brightness) {
		this.brightness = brightness;
		updateTable();
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
		updateTable();
	}

	public void setContrast(double contrast) {
        this.contrast = contrast;
        updateTable();
    }

	private void updateTable() {
		// TODO: Fill the grayTable[] array to map gray input values to gray output values.
		// It will be used as follows: grayOut = grayTable[grayIn].
		//
		int grayOut = 0;
		for(int grayIn = 0; grayIn < grayLevels; grayIn++) {
			grayTable[grayIn] = grayIn;
			grayOut = grayTable[grayIn];
			grayOut += brightness;
			grayOut = (
			        (int)((grayOut - 128) * this.contrast)
                            + 128);
			grayOut = validateLimit(grayOut);
			grayOut = (int)((255 * (double)(Math.pow(grayOut, (1 / gamma))) /
					Math.pow(255,(1/gamma))));
			grayTable[grayIn] = grayOut;
		}



		// Use brightness and gamma values.
		// See "Gammakorrektur" at slide no. 18 of 
		// http://home.htw-berlin.de/~barthel/veranstaltungen/GLDM/vorlesungen/04_GLDM_Bildmanipulation1_Bildpunktoperatoren.pdf
		//
		// First apply the brightness change, afterwards the gamma correction.
		
		
	}

	private int validateLimit (int color) {
		if(color >= 256) {
			color = 255;
		}
		if(color < 0)
			color = 0;
		return color;
	}
	
	public int mappedGray(int inputGray) {
		return grayTable[inputGray];
	}
	
	public void draw() {
		gc.clearRect(0, 0, grayLevels, grayLevels);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(3);

		// TODO : draw the tone curve into the gc graphic context

		// Remark: This is some dummy code to give you an idea for graphics drawing with pathes		
		gc.beginPath();
		for (int grayLevel = 0; grayLevel < grayLevels; grayLevel++) {
//			gc.moveTo(grayLevel, 255 - grayTable[grayLevel]);
			gc.lineTo(grayLevel, 255 - grayTable[grayLevel]);
			gc.stroke();
		}

	}

	
}
