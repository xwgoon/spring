package com.myapp.service.util.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.Arrays;

public class CSVTest {

    public static void main(String[] args) {
        simpleRead();

        System.out.println("-----");

        openCSVRead();

        System.out.println("-----");

        String[] arr = {"a", "b"};
        System.out.println(Arrays.toString(arr));

    }

    private static void openCSVRead() {
        String csvFile = "E:\\project\\spring\\source\\service\\src\\main\\java\\com\\myapp\\service\\util\\csv\\test1.csv";
        String[] line;
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "GBK"))) {
//            reader.skip(1);
            while ((line = reader.readNext()) != null) {
                System.out.println(Arrays.toString(line));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }

    /**
     * 有局限，不能正确解析逗号（,）和双引号（""），并且会把最外层的双引号解析为值的一部分
     */
    private static void simpleRead() {
        String csvFile = "E:\\project\\spring\\source\\service\\src\\main\\java\\com\\myapp\\service\\util\\csv\\test1.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(csvSplitBy);
                System.out.println(Arrays.toString(arr));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
