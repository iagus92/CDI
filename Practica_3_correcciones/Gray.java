/*
 * (c) copyright 2017 Universidade de Vigo. All rights reserved.
 * formella@uvigo.es, http://formella.webs.uvigo.es
 */

/**
  \file
  \brief Gray scale image read/write
*/

  // These are the packages we need for the example.
import java.awt.image.*;
import java.io.File;
import javax.imageio.*;

/// \brief Main class to launch application.
/// \note We always print a final message before leaving.
class Gray {
  static BufferedImage img;

  public static void main(
      // Well, we still don't use the arguments.
    String[] args
  ) {
    System.out.println("starting...");
    try {
        // Here, we just read a fixed file, the example is a 16-bit
        // gray scale image.
      File file_in=new File("nanotube.png");
      img=ImageIO.read(file_in);

        // We prepare our data array and a raster to access the image.
      final int width=img.getWidth();
      final int height=img.getHeight();
      int[][] data=new int[width][height];
      Raster raster_in=img.getData();

        // We copy the data from the image to our array.
      for(int i=0; i<width; i++) {
        for(int j=0; j<height; j++) {
          final int d=raster_in.getSample(i, j, 0);
          data[i][j]=d;
        }
      }

        // We build an output raster and fill-in with some modified data.
      WritableRaster raster_out=img.getRaster();
      for(int i=0; i<width; i++) {
        for(int j=0; j<height; j++) {
          raster_out.setSample(i,j,0,data[i][j]/2);
        }
      }

        // We set an output image with the raster and write the
        // image to a file.
      img.setData(raster_out);
      File file_out=new File("n.png");
      ImageIO.write(img,"png",file_out);
    }
    catch(Exception E) {
        // Here, we just exit the program, something more useful should
        // be implemented...
      System.exit(1);
    }
      // As always: out last line.
    System.out.println("exiting...");
  }
}

/**
  \mainpage Excercise Documentation

  \author Arno Formella
  \version Actividad 2

  \section SEC_OBS General

  This examples shows you a simple method to read and write
  gray scale images in ".png" format.
 
  Feel free to experiment, improve, adapt.

  \section SEC_CON Doxygen

  The source shows some basic features of the documentation tool
  doxygen.

  Feel free to study more and use it to generate automatically the
  reports of your exercises.
*/

/**
  \page PAGE_INFO Additional information

  You can write additional information very convieniently with doxygen
  directly into the source code files of your application.

  Please, take a close look to the doxygen documention.

  There exists the possibility to use markdown formatting within all
  documentating comments, as done here for simple enumerations: 

  -# some first item
  -# a second item
  -# a third one
  -# and as you see, comparing input and output, the counting is
     done automatically

  - and here are no numbers
  - just simple bullet points
  - ...

  \section SEC_INTRO Introduction

  This is just a sample section to show how to extend the code
  documentation...

*/
