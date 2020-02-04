package com.abrakhin.pikabuParser;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parser implements Runnable {
    private ArticleService articleService;

    public Parser() {
    }

    public Parser(ArticleService articleService) {
        this.articleService = articleService;
    }

    public Parser setDate(String date) {
        this.date = date;
        return this;
    }

    String date;

    @Override
    public void run() {
        try {
            startParse(date);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startParse(String date) throws InterruptedException {
        final List<String> posts = new ArrayList<String>();
        WebDriver driver = getNewWebDriver();
        driver.get("https://pikabu.ru/best/" + date);
        System.out.println("https://pikabu.ru/best/" + date);
//        driver.navigate().refresh();


        int oldCount = 0;
        int count = getPostsCount(driver);
        posts.addAll(collectPosts(count, oldCount, driver));

        while (!isFinish(driver)) {
            oldCount = count;
            count = getPostsSimple(oldCount, driver);
            if (count == 0) {
                driver.quit();
                System.out.println("Count of posts " + posts.size());
                System.out.println("Date " + date + " FINISHED");
                break;
            }
            posts.addAll(collectPosts(count, oldCount, driver));
            System.out.println("Current size of posts " + posts.size());
        }
        System.out.println("Final size of posts " + posts.size());
    }

    public static WebDriver getNewWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File("webdriver/extension_1_1_0_0.crx"));
        options.addExtensions(new File("webdriver/extension_4_1_0_0.crx"));
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(capabilities);

        String driverPath = System.getProperty("webdriver.chrome.driver");
        if (driverPath == null) {
            System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        } else {
            System.out.println("Selenium driver configured: " + driverPath);
        }
        return new ChromeDriver(options);
    }

    public List<String> collectPosts(int count, int oldCount, WebDriver driver) {
        System.out.println("Start collecting new posts");
        System.out.println("oldCount: " + oldCount + " count: " + count);
        List<String> posts = new ArrayList<String>();
//        for (WebElement s : driver.findElements(By.className("story")).subList(oldCount, count)) {
        for (WebElement s : driver.findElements(By.className("story"))) {
            if (s.findElements(By.className("story__title-link")).size() > 0) {
                String href = s.findElement(By.className("story__title-link")).getAttribute("href");
                articleService.saveArticle(new Article(href));
                System.out.println(href);
                posts.add(href);
            }
        }
        return posts;
    }

    public static int getPostsCount(WebDriver driver) {
        int postsOnPage = driver.findElements(By.className("story")).size();
        System.out.println("Posts on page "+ postsOnPage);
        return postsOnPage;
    }

    public static int getPostsSimple(int oldCount, WebDriver driver) throws InterruptedException {
        System.out.println("Start getting new posts simple");
        System.out.println("oldCount: " + oldCount + " currentCountOnPage: " + getPostsCount(driver));
        int tries = 0;
        int currentCount = getPostsCount(driver);
        driver.findElement(By.tagName("body")).sendKeys(Keys.END);
        while (oldCount >= currentCount) {
//            System.out.println("Try number " + tries);
//            System.out.println("Current count " + currentCount);
            Thread.sleep(1000);
            tries++;
            driver.findElement(By.tagName("body")).sendKeys(Keys.END);
            currentCount = getPostsCount(driver);
            if (tries > 100 & isFinish(driver))
                return 0;
        }
        System.out.println("Count of posts after getting " + getPostsCount(driver));
        return getPostsCount(driver);
    }

    public static Boolean isFinish(WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.className("stories-feed__message"));
        return elements.size() > 0 && elements.get(0).isDisplayed();
//        int count = driver.findElements(By.xpath("//*[contains(text(), 'Отличная работа, все прочитано! Выберите')]")).size();
//        System.out.println("Count of storis feed " + count);
//        return count > 0;
    }

}
