package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TestCrawl {

    public static void main(String[] args) {
        // Set up the ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/Users/taeyun/Downloads/chromedriver-mac-arm64/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to the target URL
            driver.get("https://www.nhtsa.gov/search-safety-issues");

            // Wait until the elements with data-toggle="collapse" are present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> accordionTriggers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[data-toggle='collapse']")));
            System.out.println(accordionTriggers);

            // Click each accordion trigger to expand
            for (WebElement trigger : accordionTriggers) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);
            }

            // 주어진 XPath로 부모 요소를 찾음
            String parentXPath = "/html/body/div[1]/div[1]/main/article/div/article/div[2]/div[4]/div/div/div[3]/div[4]/div/div[2]/div[2]/div/div[4]/div[2]";
            WebElement parentElement = driver.findElement(By.xpath(parentXPath));

            // 부모 요소 아래에서 태그에 상관없이 텍스트가 정확히 'Recall 573 Report'인 요소의 a 태그를 찾음
            String aTagXPath = ".//*[normalize-space(text()[1])='Recall 573 Report']/a";
            List<WebElement> aTags = parentElement.findElements(By.xpath(aTagXPath));

            // a 태그가 여러 개 있을 수 있으므로 반복문으로 처리
            for (WebElement aTag : aTags) {
                // a 태그의 XPath를 구성
                String aTagFullXPath = parentXPath + aTagXPath.substring(1);
                System.out.println("a 태그의 XPath: " + aTagFullXPath);
                System.out.println("링크 텍스트: " + aTag.getText());
                System.out.println("링크 URL: " + aTag.getAttribute("href"));
            }

            // 충분한 시간 대기 (예: 10초)
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}