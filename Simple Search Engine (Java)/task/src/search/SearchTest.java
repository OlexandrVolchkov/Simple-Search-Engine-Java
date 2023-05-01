package search;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SearchTest {

    @Test
    public void testSearchAny() {
        HashMap<Integer, String> indexStringFile = Main.getIndexStringFile("/Users/ovolchkov/IdeaProjects/Simple Search Engine (Java)/Simple Search Engine (Java)/task/src/search/text");
        HashMap<String, HashSet<Integer>> indexSearch = Main.getIndexSearch(indexStringFile);
        Main.menu(indexStringFile, indexSearch);

        ArrayList<String> query = new ArrayList<>();
        query.add("Erick");
        query.add("Dwight");
        query.add("webb@gmail.com");

        HashSet<String> expectedResult = new HashSet<>();
        expectedResult.add("Erick Harrington harrington@gmail.com");
        expectedResult.add("Erick Burgess");
        expectedResult.add("Dwight Joseph djo@gmail.com");
        expectedResult.add("Rene Webb webb@gmail.com");

        SearchStrategy searchStrategy = new SearchANY();
        HashSet<String> realResult = searchStrategy.getResult(indexSearch, query, indexStringFile);

//        Assert.assertEqal(realResult, expectedResult);
    }

    @Test
    public void testSearchAll() {
        HashMap<Integer, String> indexStringFile = Main.getIndexStringFile("/Users/ovolchkov/IdeaProjects/Simple Search Engine (Java)/Simple Search Engine (Java)/task/src/search/text");
        HashMap<String, HashSet<Integer>> indexSearch = Main.getIndexSearch(indexStringFile);
        Main.menu(indexStringFile, indexSearch);

        ArrayList<String> query = new ArrayList<>();
        query.add("Harrington");
        query.add("Erick");

        HashSet<String> expectedResult = new HashSet<>();
        expectedResult.add("Erick Harrington harrington@gmail.com");

        SearchStrategy searchStrategy = new SearchANY();
        HashSet<String> realResult = searchStrategy.getResult(indexSearch, query, indexStringFile);

//        Assert.assertEqal(realResult, expectedResult);
    }

    @Test
    public void testSearchNone() {
        HashMap<Integer, String> indexStringFile = Main.getIndexStringFile("/Users/ovolchkov/IdeaProjects/Simple Search Engine (Java)/Simple Search Engine (Java)/task/src/search/text");
        HashMap<String, HashSet<Integer>> indexSearch = Main.getIndexSearch(indexStringFile);
        Main.menu(indexStringFile, indexSearch);

        ArrayList<String> query = new ArrayList<>();
        query.add("djo@gmail.com");
        query.add("ERICK");

        HashSet<String> expectedResult = new HashSet<>();
        expectedResult.add("EKatie Jacobs");
        expectedResult.add("Myrtle Medina");
        expectedResult.add("Rene Webb webb@gmail.com");

        SearchStrategy searchStrategy = new SearchANY();
        HashSet<String> realResult = searchStrategy.getResult(indexSearch, query, indexStringFile);

//        Assert.assertEqal(realResult, expectedResult);
    }

}
