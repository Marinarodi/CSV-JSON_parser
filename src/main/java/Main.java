import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        //___создание csv____
        write();

        //___чтение csv____
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        //____Полученный список преобразуйте в строчку в формате JSON____
        String json = listToJson(list);


    }


    private static void write() {
        String[] employee = "1,John,Smith,USA,25,2,Inav,Petrov,RU,23".split(",");

        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
            writer.writeNext(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List <Employee> parseCSV (String[] a, String  b){
        try(CSVReader csvReader = new CSVReader(new FileReader(b))){
            var strategy = new ColumnPositionMappingStrategy<Employee>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(a);

            var csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            var employees = csv.parse();
            employees.forEach(System.out::println);
            return employees;

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return List.of();
    }

    private static String listToJson(List<Employee> i) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();

        String json = gson.toJson(i, listType);

//    }
//    private static void writeString(){

        try(FileWriter writeString = new FileWriter("data.json")){
            writeString.write(json);

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return json;
    }

}
