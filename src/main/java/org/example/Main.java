package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //List<Employee> list = parseXML("data.xml");
        String jsonString = listToJson(parseXML("data.xml"));
        writeString(jsonString, "jsonString.json");
    }

    public static List<Employee> parseXML(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Employee> list = new ArrayList<>();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            Node root = doc.getDocumentElement();
            read(root, list);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private static void read(Node root, List<Employee> list) {

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;

                long id = Integer.parseInt(employee.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employee.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());


                Employee employee1 = new Employee(id, firstName, lastName, country, age);
                list.add(employee1);
            }
        }
    }


    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String jsonString, String filePath) {
        try (Writer fw = new FileWriter(filePath)) {
            fw.write(jsonString);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

