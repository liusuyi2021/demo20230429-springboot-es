package com.example.controller;

import com.alibaba.fastjson2.JSON;
import com.example.domain.Content;
import com.example.service.ParseService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: testController
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年05月01日18:51
 * @Version: 1.0
 **/
@RestController
public class testController {

    @Resource
    ParseService parseService;
    @Resource
    RestHighLevelClient client;

    @GetMapping("/parse/{keywords}")
    Boolean parseContent(@PathVariable("keywords") String keywords) throws Exception {
        List<Content> contents = parseService.parseContent(keywords);
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("test").source(JSON.toJSON(contents.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }
    @GetMapping("/search/{keywords}/{pageNo}/{pageSize}")
    List<Map<String, Object>> search(@PathVariable("keywords") String keywords,
                         @PathVariable("pageNo") Integer pageNo,
                         @PathVariable("pageSize") Integer pageSize) throws Exception {
        return parseService.search(keywords, pageNo, pageSize);
    }
}
