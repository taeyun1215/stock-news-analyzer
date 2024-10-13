package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class ImagesCrawler {

    public static void main(String[] args) {
        // ChromeDriver 설정
        System.setProperty("webdriver.chrome.driver", "/Users/taeyun/Downloads/chromedriver-mac-arm64/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            // 대상 URL로 이동
//            driver.get("https://www.cpsc.gov/Recalls/2025/Textron-Specialized-Vehicles-Expands-Recall-of-Arctic-Cat-and-Tracker-Side-by-Side-Recreational-Off-Highway-Vehicles-Due-to-Crash-Hazard-Recall-Alert"); // cpsc
//            driver.get("https://www.recall.caa.go.jp/result/detail.php?rcl=00000033011&screenkbn=06"); // caa
//            driver.get("https://www.lebensmittelwarnung.de/___lebensmittelwarnung.de/Meldungen/2024/10_Oktober/241021_10_ST_Rockly_Nacho_Cheese/241021_10_ST_Rockly_Nacho_Cheese.html"); // bvl
//            driver.get("https://favv-afsca.be/fr/produits/rappel-de-lafsca-54"); // afsca
//            driver.get("https://www.ccpc.ie/consumers/product_recalls/product-safety-information-notice-on-a-qchomee-kids-car-travel-pillow-car-seat-belt-cushion-sold-on-amazon/"); // ccpc
//            driver.get("https://www.fsai.ie/news-and-alerts/food-alerts/recall-of-specific-batches-of-true-natural-goodnes"); // fsai
//            driver.get("https://www.nvwa.nl/documenten/waarschuwingen/2024/10/22/veiligheidswaarschuwing-rijstdessert-chocolade-van-vitariz-alinor"); // nvwa
//            driver.get("https://recalls-rappels.canada.ca/en/alert-recall/irex-magic-25000-puffs-refillable-vape-recalled-due-lack-child-resistant-packaging"); // health canada
//            driver.get("https://www.nite.go.jp/jiko/jikojohou/recall_new/2022/2023032201.html"); // nite
            driver.get("https://www.meti.go.jp/product_safety/recall/file/240927-1.html"); // meti

            // 페이지 로딩을 위한 대기 시간 (필요 시 조정)
            Thread.sleep(5000);

            // 주어진 XPath로 부모 요소를 찾음
//            String parentXPath = "/html/body/main/div[2]/article/div/div/div[3]/div/div[1]/div/div[2]/div/div/div/div/div/div"; // cpsc
//            String parentXPath = "/html/body/div[1]/div/div/div[5]/div[2]"; // caa
//            String parentXPath = "/html/body/div[2]/main/div[2]/div/div/div/div[1]/figure"; // bvl
//            String parentXPath = "/html/body/div[2]/main/div/div/article/div[3]/div/div/div[1]/div[1]/div/div"; // afsca
//            String parentXPath = "/html/body/div[1]/div[8]/div[3]/div/div/div[1]/div/p[3]"; // ccpc
//            String parentXPath = "/html/body/main/div/div/div/div/article/img"; // fsai
//            String parentXPath = "/html/body/div/main/div/div[1]/div[2]/div/figure/img"; // nvwa
//            String parentXPath = "/html/body/div[1]/div/div[1]/main/section/div[1]/div[4]/div/div/div/div/div"; // health canada
//            String parentXPath = "/html/body/div[5]/div[1]/p[7]/img"; // nite
            String parentXPath = "/html/body/div/div[3]/div/div[2]/div/div[2]/p[6]/img"; // meti

            WebElement parentElement = driver.findElement(By.xpath(parentXPath));

            // img 태그의 XPath를 저장할 리스트
            List<String> imgXPaths = new ArrayList<>();

            // 이미 처리한 src 값을 저장할 Set
            Set<String> processedSrcs = new HashSet<>();

            // 재귀적으로 DOM을 탐색하여 img 태그의 XPath를 찾음
            traverseDOM(driver, parentElement, parentXPath, imgXPaths, processedSrcs);

            // 결과 출력
            for (String imgXPath : imgXPaths) {
                WebElement imgElement = driver.findElement(By.xpath(imgXPath));
                System.out.println("img 태그의 XPath: " + imgXPath);
            }

            // 충분한 대기 시간 (예: 10초)
            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    // DOM 트리를 재귀적으로 탐색하여 img 태그의 XPath를 찾는 메서드
    public static void traverseDOM(WebDriver driver, WebElement element, String currentXPath, List<String> imgXPaths, Set<String> processedSrcs) {
        // 현재 요소를 먼저 검사 (바로 img를 가져오는 경우를 대비하기 위해, ex : fsai site)
        String tagName = element.getTagName();

        if (tagName.equalsIgnoreCase("img")) {
            String src = element.getAttribute("src");
            if (src != null && !processedSrcs.contains(src)) {
                imgXPaths.add(currentXPath);
                processedSrcs.add(src);
            }
        }

        // 자식 요소들을 가져옴
        List<WebElement> children = element.findElements(By.xpath("./*"));
        Map<String, Integer> tagCount = new HashMap<>();

        for (WebElement child : children) {
            String childTagName = child.getTagName();

            // 같은 태그 이름을 가진 형제 요소들 중에서의 인덱스를 계산
            if (!tagCount.containsKey(childTagName)) {
                tagCount.put(childTagName, 1);
            } else {
                tagCount.put(childTagName, tagCount.get(childTagName) + 1);
            }
            int index = tagCount.get(childTagName);

            // 현재 요소의 XPath를 업데이트
            String childXPath = currentXPath + "/" + childTagName + "[" + index + "]";

            // 자식 요소들을 재귀적으로 탐색
            traverseDOM(driver, child, childXPath, imgXPaths, processedSrcs);
        }
    }
}