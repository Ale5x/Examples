package xml.parsing.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
<?xml version="1.0" encoding="UTF-8"?>
<document>
    <person id="1">
        <name>Din</name>
        <age>27</age>
        <country>US</country>
        <state>NY</state>
    </person>
    <person id="2">
        <name>Sam</name>
        <age>20</age>
        <country>US</country>
        <state>CA</state>
    </person>
</document>
 */

/*
    Person{idPerson=1, name='Din', age=27, country='US', state='NY'}
    Person{idPerson=2, name='Sam', age=20, country='US', state='CA'}
 */

public class SAXParsingXML {

    static String currentElement;
    static String name, country, state;
    static int age, id;
    private static final String PATH_FILE = "src/main/resources/XML Parsing v1.xml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        final String PERSON_ID_TAG = "id";
        final String NAME_TAGE = "name";
        final String AGE_TAGE = "age";
        final String COUNTRY_TAGE = "country";
        final String PERSON_TAGE = "person";
        final String STATE_TAGE = "state";

        final List<Person> list = new ArrayList<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        DefaultHandler handler = new DefaultHandler() {

            @Override
            public void startDocument() throws SAXException {
                // Start parsing...
            }

            @Override
            public void endDocument() throws SAXException {
                // End parsing...
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                currentElement = qName;     // set name
                switch (currentElement) {
                    case PERSON_TAGE:
                        id = Integer.parseInt(attributes.getValue(PERSON_ID_TAG));
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                //проверяем, если тег закрывается, то добавляем в коллекцию
                if(qName.equalsIgnoreCase(PERSON_TAGE)) {
                    list.add(new Person(id, name, age, country, state));
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String text = new String(ch, start, length);

                // проверяем на открытие скобок; если данных нет; и перевод строки, если данные элементы есть, то выход из метода
                if(text.contains("<") || currentElement == null || text.contains("\n")) {
                    return;
                }
                switch (currentElement) {
                    case NAME_TAGE:
                        name = text;
                        break;
                    case AGE_TAGE:
                        age = Integer.parseInt(text);
                        break;
                    case COUNTRY_TAGE:
                        country = text;
                        break;
                    case STATE_TAGE:
                        state = text;
                }
            }
        };

        xmlReader.setContentHandler(handler);
        xmlReader.parse(PATH_FILE);

        for (Person person : list) {
            System.out.println(person);
        }

    }
   static class Person {

        private int idPerson;
        private String name;
        private int age;
        private String country;
        private String state;

        public Person(int idPerson, String name, int age, String country, String state) {
            this.idPerson = idPerson;
            this.name = name;
            this.age = age;
            this.country = country;
            this.state = state;
        }

       @Override
       public String toString() {
           return "Person{" +
                   "idPerson=" + idPerson +
                   ", name='" + name + '\'' +
                   ", age=" + age +
                   ", country='" + country + '\'' +
                   ", state='" + state + '\'' +
                   '}';
       }
   }
}