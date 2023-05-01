package com.example;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.domain.User;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class Demo20230429SpringbootEsApplicationTests {
    @Autowired
    RestHighLevelClient client;

    /*创建索引*/
    @Test
    public void createIndex() throws IOException {
        System.out.println("连接成功--------------------------");
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("test");
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.toString());
    }

    /*获取索引*/
    @Test
    public void testIndexExist() throws IOException {
        System.out.println("连接成功--------------------------");
        GetIndexRequest createIndexRequest = new GetIndexRequest("test");
        boolean exists = client.indices().exists(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /*删除索引*/
    @Test
    public void testIndexDelete() throws IOException {
        System.out.println("连接成功--------------------------");
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /*添加文档*/
    @Test
    public void testDocCreate() throws IOException {
        System.out.println("连接成功--------------------------");
        User user = new User("liu2", 50);
        IndexRequest indexRequest = new IndexRequest("test");
        indexRequest.id("2");
        indexRequest.timeout("1s");
        IndexRequest source = indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);
        IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(source.toString());
        System.out.println(index.toString());
        System.out.println(index.status());
    }

    /*获取文档是否存在*/
    @Test
    public void testDocExists() throws IOException {
        System.out.println("连接成功--------------------------");
        GetRequest getRequest = new GetRequest("test", "2");
        getRequest.fetchSourceContext(new FetchSourceContext(false));//不获取文档上下文
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /*获取文档的信息*/
    @Test
    public void testDocGet() throws IOException {
        System.out.println("连接成功--------------------------");
        GetRequest getRequest = new GetRequest("test", "2");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
    }

    /*更新文档*/
    @Test
    public void testDocUpdate() throws IOException {
        System.out.println("连接成功--------------------------");
        UpdateRequest updateRequest = new UpdateRequest("test", "2");
        User user = new User();
        user.setAge(222);
        updateRequest.doc(JSONObject.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
    }

    /*删除文档*/
    @Test
    public void testDocDelete() throws IOException {
        System.out.println("连接成功--------------------------");
        DeleteRequest deleteRequest = new DeleteRequest("test", "2");
        DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    /*批量增加*/
    @Test
    public void testDocbulk() throws IOException {
        System.out.println("连接成功--------------------------");
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1s");
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("d1", 1));
        users.add(new User("d2", 2));
        users.add(new User("d3", 3));
        users.add(new User("d4", 4));
        users.add(new User("d5", 5));

        for (int i = 0; i < users.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("test")
                            .id(String.valueOf(i + 1))
                            .source(JSON.toJSONString(users.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.hasFailures());
    }

    /*查询*/
    //searchRequest 搜索请求
    //searchSourceBuilder条件构造
    //termQueryBuilder精确查询
    //matchAllQueryBuilder 查询所有
    //highlighter 高亮显示
    //xxx.query(查询对象)
    @Test
    public void testSearch() throws IOException {
        System.out.println("连接成功--------------------------");
        SearchRequest searchRequest = new SearchRequest("test");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       // searchSourceBuilder.highlighter();
//        WildcardQueryBuilder termQueryBuilder = QueryBuilders.wildcardQuery("title", "java");
//        searchSourceBuilder.query(termQueryBuilder);

        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit obj:searchResponse.getHits().getHits()) {
            System.out.println(obj.getSourceAsString());
        }
    }
    @Test
    public void jsoupParse() throws IOException {
        String url="https://search.jd.com/Search?keyword=java";
        Document document = Jsoup.parse(new URL(url), 60000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        for (Element el:elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            System.out.println(img);
            String price = el.getElementsByClass("p-price").eq(0).text();
            System.out.println(price);
            String name = el.getElementsByClass("p-name").eq(0).text();
            System.out.println(name);
        }

    }
}
