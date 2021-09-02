package com.hitty.hmall.list.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hitty.hmall.bean.SkuLsInfo;
import com.hitty.hmall.bean.SkuLsParam;
import com.hitty.hmall.bean.SkuLsResult;
import com.hitty.hmall.config.RedisUtil;
import com.hitty.hmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ListServiceImpl implements ListService {

    public static final String ES_INDEX="gmall";
    public static final String ES_TYPE="SkuInfo";

    @Autowired
    private JestClient jestClient;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(SkuLsInfo skuLsInfo) {
        Index build = new Index.Builder(skuLsInfo).index(ES_INDEX).type(ES_TYPE).id(skuLsInfo.getId()).build();
        try {
            jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SkuLsResult search(SkuLsParam skuLsParam) {
        SkuLsResult skuLsResult = new SkuLsResult();
        SearchResult searchResult = null;
        String query = makeQueryStringForSearch(skuLsParam);
        Search search = new Search.Builder(query).addIndex(ES_INDEX).addType(ES_TYPE).build();
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        skuLsResult = makeResultForSearch(searchResult,skuLsParam);
        return skuLsResult;
    }

    @Override
    public void incrHotScore(String skuId) {
        Jedis jedis = redisUtil.getJedis();
        String scoreKey = "hotscore";
        int timeToES=10;
        Double hotScore = jedis.zincrby(scoreKey, 1d, "skuId:" + skuId);
        if(hotScore%timeToES==0){
            updateScore(skuId,Math.round(hotScore));
        }
    }

    private void updateScore(String skuId, long hotScore) {
        String upd= "{\n" +
                "   \"doc\": {\n" +
                "     \"hotScore\":"+hotScore+"\n" +
                "   }\n" +
                " }";
        Update build = new Update.Builder(upd).index(ES_INDEX).type(ES_TYPE).id(skuId).build();
        try {
            jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makeQueryStringForSearch(SkuLsParam skuLsParam) {
        //创建查询器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //判断catalog3Id非空并添加到过滤器
        if(skuLsParam.getCatalog3Id()!=null && skuLsParam.getCatalog3Id().length()>0){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",skuLsParam.getCatalog3Id());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        //判断ValueId非空并添加到过滤器
        if(skuLsParam.getValueId()!=null && skuLsParam.getValueId().length>0){
            for (String valueId : skuLsParam.getValueId()) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }

        }
        //判断Keyword非空
        if(skuLsParam.getKeyword() != null && skuLsParam.getKeyword().length()>0){
            //创建match
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",skuLsParam.getKeyword());
            //创建must
            boolQueryBuilder.must(matchQueryBuilder);

                //设置高亮
            //创建高亮对象
            HighlightBuilder highlighter = searchSourceBuilder.highlighter();
            //设置高亮对象属性
            highlighter.field("skuName");
            highlighter.preTags("<span style=color:red>");
            highlighter.postTags("</span>");
            //创建高亮容器
            searchSourceBuilder.highlight(highlighter);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        //设置分页
        int from = (skuLsParam.getPageNo()-1)*skuLsParam.getPageSize();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(skuLsParam.getPageSize());

        //设置排序
        searchSourceBuilder.sort("hotScore", SortOrder.DESC);

        //设置聚合
        TermsBuilder groupby_attr = AggregationBuilders.terms("groupby_attr");
        groupby_attr.field("skuAttrValueList.valueId");
        searchSourceBuilder.aggregation(groupby_attr);

        String query = searchSourceBuilder.toString();
        System.out.println("query ="+query);
        return query;
    }

    private SkuLsResult makeResultForSearch(SearchResult searchResult, SkuLsParam skuLsParam) {
        SkuLsResult skuLsResult = new SkuLsResult();
        //skuLsInfoList
        ArrayList<SkuLsInfo> skuLsInfoArrayList = new ArrayList<>();
        List<SearchResult.Hit<SkuLsInfo, Void>> hits = searchResult.getHits(SkuLsInfo.class);
        for (SearchResult.Hit<SkuLsInfo, Void> hit : hits) {
            SkuLsInfo skuLsInfo = hit.source;
            if(hit.highlight != null && hit.highlight.size()>0){
                Map<String, List<String>> highlight = hit.highlight;
                List<String> list = highlight.get("skuName");
                String skuNameHI = list.get(0);
                skuLsInfo.setSkuName(skuNameHI);
            }
            skuLsInfoArrayList.add(skuLsInfo);
        }
        skuLsResult.setSkuLsInfoList(skuLsInfoArrayList);

        //total
        skuLsResult.setTotal(searchResult.getTotal());

        //totalPages
        long totalPages = (searchResult.getTotal()+skuLsParam.getPageSize()-1)/skuLsParam.getPageSize();
        skuLsResult.setTotalPages(totalPages);

        //valueIdList
        ArrayList<String> stringArrayList = new ArrayList<>();
        MetricAggregation aggregations = searchResult.getAggregations();
        TermsAggregation groupby_attr = aggregations.getTermsAggregation("groupby_attr");
        List<TermsAggregation.Entry> buckets = groupby_attr.getBuckets();
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            stringArrayList.add(key);
        }
        skuLsResult.setValueIdList(stringArrayList);

        return skuLsResult;
    }


}
