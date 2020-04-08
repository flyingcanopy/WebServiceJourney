package ai.test.elasticsearch.controller;

/**
 * @author: Bhuvan
 * @Date: 09-04-2020
 */

import ai.test.elasticsearch.entity.CurrencyPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("currencypair")
public class CurrencyPairController {
    @Autowired
    private RestHighLevelClient client;

    @GetMapping("/")
    public @ResponseBody
    List<CurrencyPair> readAll() throws IOException {
        List<CurrencyPair> currencyPairList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("currencydata");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(10000);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            System.out.println(searchHit.getSourceAsString());
            CurrencyPair currencyPair = new ObjectMapper().readValue(searchHit.getSourceAsString(), CurrencyPair.class);
            System.out.println(currencyPair + " = " + currencyPair.getName());
            currencyPairList.add(currencyPair);
        }
        return currencyPairList;
    }
}
