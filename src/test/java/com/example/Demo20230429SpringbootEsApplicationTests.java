package com.example;

import com.alibaba.fastjson2.JSONObject;
import com.example.domain.User;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class Demo20230429SpringbootEsApplicationTests {
    @Autowired
    RestHighLevelClient client;
    /*创建索引*/
    @Test
    public void createIndex() throws IOException {
        System.out.println("连接成功--------------------------");
        CreateIndexRequest createIndexRequest=new CreateIndexRequest("test");
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.toString());
    }
    /*获取索引*/
    @Test
    public void testIndexExist() throws IOException {
        System.out.println("连接成功--------------------------");
        GetIndexRequest createIndexRequest=new GetIndexRequest("test");
        boolean exists = client.indices().exists(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    /*删除索引*/
    @Test
    public void testIndexDelete() throws IOException {
        System.out.println("连接成功--------------------------");
        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest("test");
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
    /*添加文档*/
    @Test
    public void testDocCreate() throws IOException {
        System.out.println("连接成功--------------------------");
        User user=new User("liu2",50);
        IndexRequest indexRequest=new IndexRequest("test");
        indexRequest.id("2");
        indexRequest.timeout("1s");
        IndexRequest source = indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);
        IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(source.toString());
        System.out.println(index.toString());
        System.out.println(index.status());
    }
}
