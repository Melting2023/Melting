package com.melting.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melting.dao.CrawlingDAO;
import com.melting.domain.Crawling;

@Service
public class CrawlingService {
	
	@Autowired
	CrawlingDAO crawlingDao;
		
	public List<Crawling> getDcInsideCrawlingData() {
        List<Crawling> crawlingDataList = new ArrayList<>();

        try {
            // 디시인사이드 실시간 베스트 URL
            String dcUrl = "https://www.dcinside.com/";
            Document document = Jsoup.connect(dcUrl).get();

            Elements titles = document.select("div.box.besttxt > p");
            Elements replycnts = document.select("div.box.besttxt > span");
            Elements kinds = document.select("div.box.best_info > span.name");
            Elements links = document.select(".main_log");
            
            int count = Math.min(10, titles.size()); // 10개 이하의 게시물만 가져오기 위해 크기 제한
            for (int i = 0; i < count; i++) {
                Element titleElement = titles.get(i);
                String title = titleElement.text();

                Element replycntElement = replycnts.get(i);
                String replycnt = replycntElement.text();
                
                Element kindElement = kinds.get(i);
                String kind = kindElement.text();
                
                Element linkElement = links.get(i);
                String link = linkElement.attr("href");

                Crawling crawling = new Crawling(title, replycnt, kind, link);
                crawlingDataList.add(crawling);
                
                // 데이터를 DB에 저장
//                crawlingDao.insertCrawlingData(crawling);
            
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return crawlingDataList;
    }

    public List<Crawling> getFmKoreaCrawlingData() {
        List<Crawling> crawlingDataList = new ArrayList<>();

        try {
            String fmKoreaUrl = "https://www.fmkorea.com/best2";
            Document document = Jsoup.connect(fmKoreaUrl).get();

            Elements titles = document.select(".title > a");
            Elements replycnts = document.select(".title > a > span");
            Elements kinds = document.select(".category");
            Elements links = document.select(".hotdeal_var8");

            int count = Math.min(10, titles.size());
            for (int i = 0; i < count; i++) {
                Element titleElement = titles.get(i);
                String title = titleElement.text();

                Element replycntElement = replycnts.get(i);
                String replycnt = replycntElement.text();
                
                Element kindElement = kinds.get(i);
                String kind = kindElement.text();
                
                Element linkElement = links.get(i);
                String link = "https://www.fmkorea.com"+linkElement.attr("href");


                Crawling crawling = new Crawling(title, replycnt, kind, link);
                crawlingDataList.add(crawling);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return crawlingDataList;
    }
    
    public List<Crawling> getPpomppuCrawlingData() {
        List<Crawling> crawlingDataList = new ArrayList<>();

        try {
            String ppomppuUrl = "https://www.ppomppu.co.kr/hot.php?category=2";
            Document document = Jsoup.connect(ppomppuUrl).get();

            Elements titles = document.select(".line .title");
            Elements replycnts = document.select(".list_comment2");
            Elements kinds = document.select(".board_left a");
            Elements links = document.select(".line .title");	

            int count = Math.min(10, titles.size());
            for (int i = 0; i < count; i++) {
                Element titleElement = titles.get(i);
                String title = titleElement.text();

                Element replycntElement = replycnts.get(i);
                String replycnt = replycntElement.text();
                
                Element kindElement = kinds.get(i+1);
                String kind = kindElement.text();
                
                Element linkElement = links.get(i);
                String link = "https://www.ppomppu.co.kr"+linkElement.attr("href");

                Crawling crawling = new Crawling(title, replycnt, kind, link);
                crawlingDataList.add(crawling);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return crawlingDataList;
    }

	public boolean saveCrawlingData(Crawling crawling) {
		return crawlingDao.saveCrawlingData(crawling);
		
	}
    
    
	
}
