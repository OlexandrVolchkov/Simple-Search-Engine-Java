package search;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
//        HashMap<Integer, String> indexStringFile = getIndexStringFile("/Users/ovolchkov/IdeaProjects/Simple Search Engine (Java)/Simple Search Engine (Java)/task/src/search/text");
        HashMap<Integer, String> indexStringFile = getIndexStringFile(args[1]);
        HashMap<String, HashSet<Integer>> indexSearch = getIndexSearch(indexStringFile);
        menu(indexStringFile, indexSearch);
    }

    public static HashMap<Integer, String> getIndexStringFile(String linkToFile) {
        HashMap<Integer, String> indexStringFile = new HashMap<>();
        File peopleData = new File(linkToFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(peopleData))) {
            Integer stringCounter = 0;
            while (true) {
                String readLine = reader.readLine();
                if (readLine != null) {
                    indexStringFile.put(stringCounter, readLine);
                    stringCounter++;
                } else {
                    break;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        return indexStringFile;
    }

    public static HashMap<String, HashSet<Integer>> getIndexSearch(HashMap<Integer, String> indexStringFile) {
        HashMap<String, HashSet<Integer>> indexSearchedStringByWord = new HashMap<>();

        for (int i = 0; i < indexStringFile.size(); i++) {
            String[] magicWords = indexStringFile.get(i).split(" ");
            for (String magicWord : magicWords) {
                if (!indexSearchedStringByWord.containsKey(magicWord)) {
                    indexSearchedStringByWord.put(magicWord, new HashSet<Integer>());
                }
                indexSearchedStringByWord.get(magicWord).add(i);
            }
        }
        return indexSearchedStringByWord;
    }

    public static HashSet<String> searchByWord(HashMap<String, HashSet<Integer>> indexSearch,
                                               HashMap<Integer, String> indexStringFile) {

        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String searchStrategyType = getString();
        SearchStrategy searchStrategy = null;
        if ("all".equalsIgnoreCase(searchStrategyType)) {
            searchStrategy = new SearchALL();
        } else if ("any".equalsIgnoreCase(searchStrategyType)) {
            searchStrategy = new SearchANY();
        } else if ("none".equalsIgnoreCase(searchStrategyType)) {
            searchStrategy = new SearchNONE();
        } else {
            System.out.println("Error!");
            System.exit(0);
        }
        System.out.println();

        System.out.println("Enter a name or email to search all suitable people.");
        ArrayList<String> queryWordsList = new ArrayList<String>(Arrays.asList(getString().split(" ")));

        return searchStrategy.search(indexSearch, queryWordsList, indexStringFile);
    }

    public static String getString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void menu(HashMap<Integer, String> indexStringFile,
                            HashMap<String, HashSet<Integer>> indexSearch) {
        System.out.println();
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");

        int menuOption = Integer.parseInt(getString());

        if (menuOption == 1) {
            System.out.println();
            searchByWord(indexSearch, indexStringFile).forEach(System.out::println);
            menu(indexStringFile, indexSearch);
        } else if (menuOption == 2) {
            System.out.println();
            System.out.println("=== List of people ===");
            indexStringFile.forEach((key, value) -> System.out.println(value));
            menu(indexStringFile, indexSearch);
        } else if (menuOption == 0) {
            System.out.println();
            System.out.println("Bye!");
        } else {
            System.out.println();
            System.out.println("Incorrect option! Try again.");
            menu(indexStringFile, indexSearch);
        }
    }
}

abstract class SearchStrategy {
    public HashSet<String> search(HashMap<String, HashSet<Integer>> indexSearch,
                                  ArrayList<String> query,
                                  HashMap<Integer, String> indexStringFile) {
        HashSet<String> result = getResult(indexSearch, query, indexStringFile);
        return result;
    }

    abstract HashSet<String> getResult(HashMap<String, HashSet<Integer>> indexSearch,
                                       ArrayList<String> query,
                                       HashMap<Integer, String> indexStringFile);

}

class SearchALL extends SearchStrategy {
    @Override
    HashSet<String> getResult(HashMap<String, HashSet<Integer>> indexSearch, ArrayList<String> query, HashMap<Integer, String> indexStringFile) {
        HashSet<String> resultHashSet = new HashSet<>();

        try {
            for (String index : indexSearch.keySet()) {
                Set<String> queryWordsSet = query.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                for (Integer ind : indexSearch.get(index)) {
                    ArrayList<String> indexedStringWordsList = new ArrayList<String>(Arrays.asList(indexStringFile.get(ind).split(" ")));
                    Set<String> indexStringWordsSet = indexedStringWordsList.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                    if (indexStringWordsSet.containsAll(queryWordsSet)) {
                        resultHashSet.add(indexStringFile.get(ind));
                    }
                }
            }
            if (resultHashSet.size() == 0) {
                System.out.println("No matching people found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHashSet;
    }
}

class SearchANY extends SearchStrategy {
    @Override
    HashSet<String> getResult(HashMap<String, HashSet<Integer>> indexSearch, ArrayList<String> query, HashMap<Integer, String> indexStringFile) {
        HashSet<String> resultHashSet = new HashSet<>();

        try {
            for (String index : indexSearch.keySet()) {
                Set<String> queryWordsSet = query.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                for (Integer ind : indexSearch.get(index)) {
                    ArrayList<String> indexedStringWordsList = new ArrayList<String>(Arrays.asList(indexStringFile.get(ind).split(" ")));
                    Set<String> indexStringWordsSet = indexedStringWordsList.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                    for (String queryWord : queryWordsSet) {
                        if (indexStringWordsSet.contains(queryWord)) {
                            resultHashSet.add(indexStringFile.get(ind));
                        }
                    }
                }
            }
            if (resultHashSet.size() == 0) {
                System.out.println("No matching people found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultHashSet.forEach(System.out::println);
        return resultHashSet;
    }
}

class SearchNONE extends SearchStrategy {
    @Override
    HashSet<String> getResult(HashMap<String, HashSet<Integer>> indexSearch, ArrayList<String> query, HashMap<Integer, String> indexStringFile) {
        HashSet<String> resultHashSet = new HashSet<>();

        try {
            for (String index : indexSearch.keySet()) {
                Set<String> queryWordsSet = query.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                for (Integer ind : indexSearch.get(index)) {
                    ArrayList<String> indexedStringWordsList = new ArrayList<String>(Arrays.asList(indexStringFile.get(ind).split(" ")));
                    Set<String> indexStringWordsSet = indexedStringWordsList.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));

                    boolean notContain = true;
                    for (String queryWord : queryWordsSet) {
                        if (indexStringWordsSet.contains(queryWord)) {
                            notContain = false;
                            break;
                        }
                    }
                    if (notContain) {
                        resultHashSet.add(indexStringFile.get(ind));
                    }
                }
            }
            if (resultHashSet.size() == 0) {
                System.out.println("No matching people found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultHashSet.forEach(System.out::println);
        return resultHashSet;
    }
}



