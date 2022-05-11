package com.company.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileReaderService {
    private static FileReaderService makeReadInstance = null;

    private FileReaderService() {

    }

    public static FileReaderService getInstance() {
        if (makeReadInstance == null)
            makeReadInstance = new FileReaderService();
        return makeReadInstance;
    }

    public ArrayList<ArrayList<String>> read(String path) {

        ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();

        try {
            File input = new File(path);
            Scanner s = new Scanner(input);
            while (s.hasNextLine()) {

                String line = s.nextLine().strip();
                String[] spl = line.split(",");
                ArrayList<String> splices = new ArrayList<String>(Arrays.asList(spl));

                content.add(splices);
            }
            return content;
        } catch (FileNotFoundException e) {
            System.out.println("wrong path: " + path);
        }
        return null;
    }
}