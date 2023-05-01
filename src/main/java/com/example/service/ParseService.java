package com.example.service;

import com.example.domain.Content;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ParseService {
    List<Content> parseContent(String keyWords) throws Exception;
    List<Map<String,Object>>search(String keyWords, Integer pageNo, Integer pageSize) throws IOException;
}
