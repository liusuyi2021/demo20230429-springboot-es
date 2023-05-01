package com.example.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import com.example.domain.Content;
import com.example.utils.ContentsParseUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: parseServiceImpl
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年05月01日18:53
 * @Version: 1.0
 **/
@Service
public class ParseServiceImpl implements ParseService {

    @Resource
    RestHighLevelClient client;

    @Override
    public List<Content> parseContent(String keyWords) throws Exception {
        return ContentsParseUtil.parseJd(keyWords);
    }

    @Override
    public List<Map<String, Object>> search(String keyWords, Integer pageNo, Integer pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest("test");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*分页*/
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);
        /*精确匹配*/
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title",keyWords);
//        searchSourceBuilder.query(matchQueryBuilder);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
        /*执行查询*/
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> data=new ArrayList<>();
        for (SearchHit obj:searchResponse.getHits().getHits()) {
            data.add(obj.getSourceAsMap());
        }
        return data;
    }
}
