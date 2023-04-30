package com.example.utils;

import com.example.domain.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ContentsParseUtil
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年04月30日22:36
 * @Version: 1.0
 **/
public class ContentsParseUtil {
    public static void main(String[] args) throws Exception {
        ContentsParseUtil.parseJd("主板").forEach(System.out::println);
    }
    public static List<Content> parseJd(String keyWord) throws Exception {
        String url="https://search.jd.com/Search?keyword="+keyWord;
        Document document = Jsoup.parse(new URL(url), 60000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        List<Content> goodsList=new ArrayList<>();
        for (Element el:elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            Content content=new Content();
            content.setImg(img);
            content.setTitle(title);
            content.setPrice(price);
            goodsList.add(content);
        }
        return goodsList;
    }
}
