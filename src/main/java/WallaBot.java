import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WallaBot extends BaseRobot {

    public WallaBot(String rootWebsiteUrl){
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
    public void extractLinks() throws IndexOutOfBoundsException {
        System.out.println("Loading links from \"www.walla.co.il\"");

        try {
            Elements a = document.getElementsByTag("a");
            for (Element attrHref: a) {
                String url = attrHref.attr("href");
                if (url.contains("/item/")) {
                    links.add(url);
                }
            }
            removeDuplicates(links);

        } catch (IndexOutOfBoundsException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void extractText() throws IOException {
        System.out.println("Loading text from www.walla.co.il");

        try {
            for (String url : links) {
                String articleBody="";
                Document site = Jsoup.connect(url).get();
                titleText = site.select("header").select("h1").text();
                titles.add(titleText);

                String subTitle = site.select("header").select("p").text() + " ";

                titleAndSubTitleWords += titleText + " ";
                titleAndSubTitleWords += subTitle;

                words = titleText + " ";
                words += subTitle + " ";

                Elements textContainer = site.getElementsByClass("css-onxvt4  ");
                for (Element p : textContainer) {
                     articleBody += p.getElementsByTag("p").text();

                }
                words += articleBody;


                wordsSplit = words.split(" ");

                countWords();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        return map;
    }

}
