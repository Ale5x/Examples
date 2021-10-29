package xml.parsing.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    /*
        <?xml version="1.0" encoding="UTF-8"?>
    <report number="12345">
        <employers department="II">
            <employee number="1">
                <name>John Doe</name>
                <age>25</age>
                <salary currency="USD">5000</salary>
            </employee>

            <employee number="2">
                <name>Mike Frost</name>
                <age>35</age>
                <salary currency="CAD">8000</salary>
            </employee>

            <employee number="3">
                <name>Jim Brown</name>
                <age>32</age>
                <salary currency="F">15000</salary>
            </employee>
        </employers>
    </report>
     */

    /*
    Employee{department='II', number=1, name='John Doe', age=25, salary=5000.0 USD}
    Employee{department='II', number=2, name='Mike Frost', age=35, salary=8000.0 CAD}
    Employee{department='II', number=3, name='Jim Brown', age=32, salary=15000.0 F}
     */

public class DOMParsingXML {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String path = "src/main/resources/XML Parsing v3.xml";

        File file = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        /*
            *  т.к. в документе один узел employers, получаем первый item и приводим к Element
        */
        Element employersElement = (Element) document.getElementsByTagName("employers").item(0);
        String department = employersElement.getAttribute("department");    //  Получаем значение атрибута "department"

        NodeList employeeNodeList = document.getElementsByTagName("employee");  // Получаем всех сотрудников из document
        List<Employee> employeeList = new ArrayList<>();

        for(int i = 0; i < employeeNodeList.getLength(); i++) {
            /*
              *   Проверяем является ли наш Node является по типу элементом, нас интересует элемент, а они могут быть:
              *   текстовое; сидэйтэ (такая секция в рамках xml, которая может быть расположена, скажем так, с
              *   критической точки зрения xml - контент: xml угловые скобки (<,>) воспринимает как начало и конец элемента,
              *   если мы хотим, чтобы в рамках нашего xml, к примеру в n-строке фигурировали угловые скобки, но не
              *   воспринимались, как элементные скобки,- то мы заключаем их в сидэтэ
            */
            if(employeeNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element employeeElement = (Element) employeeNodeList.item(i); // Выполняем приведение нашего Node к элементу

                Employee employee = new Employee();
                employee.setDepartment(department);
                employee.setNumber(Integer.parseInt(employeeElement.getAttribute("number")));

                /*
                Чтобы пробежаться по внутренним элементам employee, повторим цикл-действия
                 */
                NodeList childNodes = employeeElement.getChildNodes();
                for(int j = 0; j < childNodes.getLength(); j++) {
                    if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) childNodes.item(j);  // выполняем приведение нашего childNodes к элементу

                        switch (childElement.getNodeName()) {
                            case "name" : {
                                employee.setName(childElement.getTextContent());
                            } break;
                            case "age" : {
                                employee.setAge(Integer.parseInt(childElement.getTextContent()));
                            } break;
                            case "salary" : {
                                employee.getSalary().setValue(Double.valueOf(childElement.getTextContent()));
                                employee.getSalary().setCurrency(childElement.getAttribute("currency"));
                            } break;

                        }
                    }
                }
                employeeList.add(employee);
            }
        }
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }
}

class Employee {
    private String department;
    private Integer number;
    private String name;
    private Integer age;
    private Salary salary = new Salary();

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "department='" + department + '\'' +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary.getValue() + " " + salary.getCurrency() +
                '}';
    }
}

class Salary {
    private String currency;
    private Double value;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

