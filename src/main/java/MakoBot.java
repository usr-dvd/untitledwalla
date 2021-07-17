import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public class MakoBot extends BaseRobot {

    public MakoBot(String rootWebsiteUrl){
        super(rootWebsiteUrl);
        try {
            extractLinks();
            extractText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void extractLinks() throws IndexOutOfBoundsException {
        System.out.println("Loading links from www.mako.co.il");
        try {
            String rootLink = getRootWebsiteUrl().substring(0, getRootWebsiteUrl().length() - 1);
            Elements a = document.getElementsByTag("a");
            for (Element attrHref : a) {
                String url = attrHref.attr("href");
                if (isValidUrl(url)) {
                    if (!(url.contains(rootLink))) {
                        url = rootLink.concat(url);
                    }
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

        System.out.println("Loading text from www.mako.co.il");

            for (String url : links) {
                Document site = Jsoup.connect(url).get();


                Elements writerData = site.getElementsByClass("writer-data").parents();

                for (Element h1 : writerData) {
                    titleText = h1.getElementsByTag("h1").text();
                }
                titles.add(titleText);

                for (Element h2 : writerData) {

                    titleAndSubTitleWords = h2.getElementsByTag("h2").get(0).text() + " ";

                }

                titleAndSubTitleWords += titleText + " ";

                words = titleAndSubTitleWords+" ";


                    for (Element p: site.getElementsByClass("article-body").get(0).children()) {
                        try {
                        words += p.getElementsByTag("p").text();

                        throw new NoSuchElementException();

                        } catch (NoSuchElementException e) {
                        }
                    }


                wordsSplit = words.split(" ");
                countWords();

            }
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        return map;
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

    public boolean isValidUrl(String url) {
        return !(
                url.contains("tv-comingsoon") || url.contains("spirituality") ||
                url.contains("help") || url.contains("living-architecture/local/") ||
                url.contains("/tv/") || url.contains("http:") ||
                !url.contains("Article") || !url.contains("news")
        );
    }

}
