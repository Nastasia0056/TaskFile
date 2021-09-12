package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        ArrayList<FileInfo> fileInfos = null;
        try {
            fileInfos = defineSizeFiles(args);

            for (FileInfo info : fileInfos) {
                if (info.isService())
                    System.out.println(info.getName() + " " + info.getSize() + " " + info.getUnit());
                else
                    System.out.println("Название: " + info.getName() + " Размер: " + info.getSize() + " " + info.getUnit());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static ArrayList<FileInfo> defineSizeFiles(String[] args) throws FileNotFoundException {
        ArrayList<String> filenames = new ArrayList<>();
        HashSet<String> params = new HashSet<>();

        parseArgs(args, filenames, params);
        if (!checkParams(params)){
            throw new IllegalArgumentException("Обнаружены неизвестные параметры");
        }

        ArrayList<FileInfo> fileInfos = formFileInfo(filenames);
        double total;
        if (params.contains("-c"))
        {
            total = totalSize(fileInfos);
            FileInfo fi = new FileInfo("Общий размер файлов/папок", total, true);
            fileInfos.add(fi);
        }

        applyParams(fileInfos, params);

        return fileInfos;
    }

    private static double totalSize(ArrayList<FileInfo> fileInfos){
        double total = 0;
        for(FileInfo info : fileInfos){
            total += info.getSize();
        }
        return total;
    }

    private static boolean checkParams(HashSet<String> params){
        HashSet<String> allowParams = new HashSet<>(Arrays.asList("--si", "-h", "-c"));
        for(String p: params){
            if (!allowParams.contains(p)){
                return false;
            }
        }
        return true;
    }

    private static void applyParams(ArrayList<FileInfo> fileInfos, HashSet<String> params){
        int base = 1024;
        if (params.contains("--si"))
            base = 1000;

        for (int i = 0; i < fileInfos.size(); i++) {
            if (params.contains("-h"))
                fileInfos.get(i).convertUnit(base);
            else
                fileInfos.get(i).convertDefaultUnit(base);
        }
    }

    private static ArrayList<FileInfo> formFileInfo(ArrayList<String> filenames) throws FileNotFoundException {
        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        if (filenames.size() == 0)
            throw new IllegalArgumentException("Передайте в качестве аргументов названия файлов или папок");

        for (int i = 0; i < filenames.size(); i++) {
            File file = new File(filenames.get(i));
            if (file.exists()){
                long size;
                if (file.isFile())
                    size = file.length();
                else
                    size = folderSize(file);

                FileInfo fileInfo = new FileInfo(filenames.get(i), size, true);
                fileInfos.add(fileInfo);
            }
            else
            {
                throw new FileNotFoundException("Файл " + filenames.get(i) + " не существует");
            }
        }
        return fileInfos;
    }

    private static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
    
    private static void parseArgs(String[] args, ArrayList<String> filenames, HashSet<String> params){
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) //if (!args[i].equals("-c") && !args[i].equals("-h") && !args[i].equals("--ci"))
            {
                filenames.add(args[i]);
            }
            else
                params.add(args[i]);
        }
    }
}
