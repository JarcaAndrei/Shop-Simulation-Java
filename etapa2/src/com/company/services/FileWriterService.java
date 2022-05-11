package com.company.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterService {
    private static FileWriterService makeOutInstance = null;

    private FileWriterService() {

    }

    public static FileWriterService getInstance() {
        if (makeOutInstance == null)
            makeOutInstance = new FileWriterService();
        return makeOutInstance;
    }

    public void write(String path, List<String> lista){

        File out_path = new File(path);
        try {
            if (!out_path.exists()) {
                out_path.createNewFile();
            }

            FileWriter w = new FileWriter(out_path,true);
            StringBuilder out_message = new StringBuilder();
            for (String l : lista) {
                out_message.append(l);
                out_message.append(",");
            }
            out_message.deleteCharAt(out_message.length()-1);
            out_message.append("\n");
            w.write(out_message.toString());
            w.flush();
            w.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}