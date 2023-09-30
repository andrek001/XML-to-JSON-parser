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

    private static void read(Node node, List<Employee> list) {
        NodeList nodeList = node.getChildNodes();
        Employee newEmployee = new Employee();
        boolean toNewObject = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (toNewObject) {
                Employee newEmployee1 = new Employee();
                newEmployee = newEmployee1;
            }
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                String nodeName = node_.getNodeName();
                switch (nodeName) {
                    case "id":
                        newEmployee.setId(Long.parseLong(readNextText(node_)));
                        break;
                    case "firstName":
                        newEmployee.setFirstName(readNextText(node_));
                        break;
                    case "lastName":
                        newEmployee.setLastName(readNextText(node_));
                        break;
                    case "country":
                        newEmployee.setCountry(readNextText(node_));
                        break;
                    case "age":
                        newEmployee.setAge(Integer.parseInt(readNextText(node_)));
                        list.add(newEmployee);
                        toNewObject = true;
                        break;
                }
                read(node_, list);
            }
        }
    }

    private static String readNextText(Node node) {
        NodeList nodeList = node.getChildNodes();
        Node node_ = nodeList.item(0);
        if (node_.getNodeType() == 3) {
            return node_.toString().substring(8, node_.toString().length() - 1);
        }
        return "";
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

