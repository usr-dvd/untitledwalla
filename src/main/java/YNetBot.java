import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YNetBot extends BaseRobot {


    @Override
    public void extractLinks() throws IOException{
        System.out.println("Loading links from \"www.ynet.co.il\"");
        try {
            Elements a = document.getElementsByTag("a");
            for (Element attrHref: a) {
                String url = attrHref.attr("href");
                if (url.contains("article/") && url.contains("https://www.ynet.co.il/")) {
                    links.add(url);
                }
            }
            removeDuplicates(links);


        } catch (IndexOutOfBoundsException e) {

            e.printStackTrace();

        }
    }

    public YNetBot(String rootWebsiteUrl){
        super(rootWebsiteUrl);
        try {
            extractLinks();
            extractText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int countInArticlesTitles(String text) {



        return getRepetitionsFromTitlesAndSubT(text, titleAndSubTitleWords);
    }

    @Override
    public String getLongestArticleTitle() {

        sortList(titles);

        return titles.get(0);
    }
    @Override
    public void extractText() throws IOException{

        System.out.println("Loading text from \"www.ynet.co.il\"");
        for (String url : links) {
            try {

                Document articlePage = Jsoup.connect(url).get();
                titleText = (articlePage.getElementsByClass("mainTitle").text());
                titles.add(titleText);

                titleAndSubTitleWords += titleText + " ";
                titleAndSubTitleWords += (articlePage.getElementsByClass("subTitle").text() + " ");

                words = (articlePage.getElementsByClass("mainTitle").text()) + " ";
                words += (articlePage.getElementsByClass("subTitle").text()) + " ";
                words += (articlePage.getElementsByClass("text_editor_paragraph rtl").text());
                wordsSplit = words.split(" ");

                countWords();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        return map;
    }
}
