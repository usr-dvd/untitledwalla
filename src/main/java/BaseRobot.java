import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseRobot {

    private String rootWebsiteUrl;
    protected Document document;
    protected Map<String, Integer> map = new HashMap<>();
    protected ArrayList<String> titles = new ArrayList<>();
    protected ArrayList<String> links = new ArrayList<>();
    protected String words="";
    protected String titleAndSubTitleWords="";
    protected String titleText="";
    protected String[] wordsSplit;

    public BaseRobot(String rootWebsiteUrl) {
        this.rootWebsiteUrl = rootWebsiteUrl;
        defineMainPageDoc();
    }

    public String getRootWebsiteUrl() {
        return rootWebsiteUrl;
    }

    public void setRootWebsiteUrl(String rootWebsiteUrl) {
        this.rootWebsiteUrl = rootWebsiteUrl;
    }



    public abstract int countInArticlesTitles(String text);

    public abstract String getLongestArticleTitle();

    public abstract void extractLinks() throws IOException;

    public abstract void extractText() throws IOException;


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    private void defineMainPageDoc() {
        try {
            document = Jsoup.connect(getRootWebsiteUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortList(ArrayList<String> list){

        list.sort((o1, o2) -> o2.length() - o1.length());
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public abstract Map<String, Integer> getWordsStatistics();


    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getTitleAndSubTitleWords() {
        return titleAndSubTitleWords;
    }

    public void setTitleAndSubTitleWords(String titleAndSubTitleWords) {
        this.titleAndSubTitleWords = titleAndSubTitleWords;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String[] getWordsSplit() {
        return wordsSplit;
    }

    public void setWordsSplit(String[] wordsSplit) {
        this.wordsSplit = wordsSplit;
    }

    public void removeDuplicates(ArrayList<String> list) {

        ArrayList<String> tempTitles = new ArrayList<>();
        ArrayList<String> tempLinks = new ArrayList<>();
        Set<String> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);


        try {
            for (int i = 0; i < list.size(); i++) {

                Document site = Jsoup.connect(list.get(i)).get();
                tempTitles.add(site.title());
            }
            for (int i = 0; i < tempTitles.size(); i++) {
                for (int j = 0; j < tempTitles.size()-1; j++) {
                    if (tempTitles.get(i).equals(tempTitles.get(j)) && i != j) {
                        tempLinks.add(list.get(i));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        list.removeAll(tempLinks);
        tempTitles.clear();
        tempLinks.clear();


    }
    public void countWords(){

        for (String word : wordsSplit) {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }
    }
    public int getRepetitionsFromTitlesAndSubT(String usrText, String titleAndSubTitles) {


        Pattern pattern = Pattern.compile(usrText);
        Matcher matcher = pattern.matcher(titleAndSubTitles);
        int counter = 0;
        while (matcher.find()) {
            counter++;
        }


        return counter;
    }


}