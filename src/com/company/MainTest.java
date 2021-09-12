package com.company;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void defineSizeFiles() {
        String[] args = {"file1.txt"};
        double[] expectedSizes = { 357.01 };
        String[] expectedUnits = {""};

        assertResult(expectedSizes, args, expectedUnits);
    }

    @Test
    public void defineSizeFilesParamHTest() {
        String[] args = {"-h", "Lola.mp3", "file1.txt"};
        double[] expectedSizes = { 7.47, 357.01 };
        String[] expectedUnits = {"МБ", "КБ"};

        assertResult(expectedSizes, args, expectedUnits);
    }

    @Test
    public void defineSizeFilesParams_H_SI_Test() {
        String[] args = {"-h", "--si", "Lola.mp3"};
        double[] expectedSizes = { 7.84 };
        String[] expectedUnits = {"МБ"};

        assertResult(expectedSizes, args, expectedUnits);
    }

    @Test
    public void defineSizeFilesParams_H_SI_C_Test() {
        String[] args = {"-h", "-c", "Lola.mp3", "file1.txt"};
        double[] expectedSizes = { 7.47, 357.01, 7.82 };
        String[] expectedUnits = {"МБ", "КБ", "МБ"};

        assertResult(expectedSizes, args, expectedUnits);
    }

    private void assertResult(double[] expectedSizes, String[] args, String[] expectedUnits){
        ArrayList<FileInfo> fileInfos = null;
        try {
            fileInfos = Main.defineSizeFiles(args);

            assertEquals(expectedSizes.length, fileInfos.size());

            for (int i = 0; i < expectedSizes.length; i++) {
                assertEquals(expectedSizes[i], fileInfos.get(i).getSize(), 0.00001);
                assertEquals(expectedUnits[i],  fileInfos.get(i).getUnit());
            }
        } catch (FileNotFoundException e) {
            assertEquals(1,0);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotFound_Test() throws FileNotFoundException {
        String[] args = {"-h", "-c", "Lo7la.mp3", "file1.txt"};

        Main.defineSizeFiles(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileParamsNoFiles_Test() throws FileNotFoundException {
        String[] args = {"-h", "-c"};

        Main.defineSizeFiles(args);
    }
}