package xml.parsing.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    /*
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <company>
        <name>IT-Heaven</name>
        <offices>
            <office floor="1" room="1">
                <employees>
                    <employee name="Maksim" job="Middle Software Developer" />
                    <employee name="Ivan" job="Junior Software Developer" />
                    <employee name="Franklin" job="Junior Software Developer" />
                </employees>
            </office>
            <office floor="1" room="2">
                <employees>
                    <employee name="Herald" job="Middle Software Developer" />
                    <employee name="Adam" job="Middle Software Developer" />
                    <employee name="Leroy" job="Junior Software Developer" />
                </employees>
            </office>
        </offices>
    </company>
     */

    /*
    Имя сотрудника: Maksim, его должность: Middle Software Developer
    Имя сотрудника: Ivan, его должность: Junior Software Developer
    Имя сотрудника: Franklin, его должность: Junior Software Developer
    Имя сотрудника: Herald, его должность: Middle Software Developer
    Имя сотрудника: Adam, его должность: Middle Software Developer
    Имя сотрудника: Leroy, его должность: Junior Software Developer
     */
public class SAXParsingXML_v2 {

    private static List<Employee> employees = new ArrayList<>();
    private final static String PATH_FILE = "src/main/resources/XML Parsing v2.xml";

    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        DefaultHandler  handler = new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("employee")) {
                    String name = attributes.getValue("name");
                    String job = attributes.getValue("job");
                    employees.add(new Employee(name, job));
                }
            }
        };
        parser.parse(new File(PATH_FILE), handler);

        for (Employee employee : employees)
            System.out.println(String.format("Имя сотрудника: %s, его должность: %s", employee.getName(), employee.getJob()));
    }
}

 class Employee {
    private String name, job;

    public Employee(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
