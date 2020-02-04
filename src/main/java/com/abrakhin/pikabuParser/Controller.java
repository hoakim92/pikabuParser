package com.abrakhin.pikabuParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    public Controller(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    private ArticleService articleService;




    @GetMapping("/test")
    public String greeting() {
        return "test";
    }

    @GetMapping("/start")
    public String start() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);      // 1st day of month
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        int myMonth = cal.get(Calendar.MONTH);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
//        while (myMonth == cal.get(Calendar.MONTH)) {
//            executor.execute(new Parser().setDate(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime())));
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//        }
        executor.execute(new Parser(articleService).setDate(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime())));
        return "test";
    }
}