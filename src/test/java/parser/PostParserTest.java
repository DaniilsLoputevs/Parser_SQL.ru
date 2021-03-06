package parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostParserTest {

    private String postUrl = "https://www.sql.ru/forum/1324373/java-razrabotchik-krasnodar-moskva-90-000-150-000-net";
    private PostParser parser = new PostParser(postUrl);

    @Test
    public void parseName() {
        var result = parser.parseName();
        var expected = "Java-разработчик Краснодар, Москва 90 000 - 150 000 net [new]";
        assertEquals(expected, result);
    }

    @Test
    public void parseDesc() {
        var result = parser.parseDesc();
        var expected = "Знание Java 8+; ";
        assertTrue(result.contains(expected));
    }

    @Test
    public void parseLink() {
        var result = parser.parseLink();
        var expected = postUrl;
        assertEquals(expected, result);
    }

    @Test
    public void parseDate() {
        var result = parser.parseDate().toString();
        var expected = "Tue Apr 14";
        assertTrue(result.contains(expected));
    }

}