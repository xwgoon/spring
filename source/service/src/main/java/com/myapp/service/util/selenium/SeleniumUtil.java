package com.myapp.service.util.selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeleniumUtil {

    public static void main(String[] args) throws Exception {

        // 2.设置下载文件存放路径
        String downloadDir = "E:\\download\\" + UUID.randomUUID();

        // 3.创建HashMap 保存下载文件存放路径
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("download.default_directory", downloadDir);

        // 4.ChromeOptions 中设置下载路径信息，传入保存有下载路径的 HashMap
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", hashMap);

//        chromeOptions.addArguments("--ignore-certificate-errors","headless");

        // 依据 ChromeOptions 来产生 DesiredCapbilities，这时 DesiredCapbilities 也就具备了下载路径的信息
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        chromeOptions.merge(desiredCapabilities);


        WebDriver driver = new ChromeDriver(chromeOptions);

        try {
            driver.get("https://rmp.adas.com/#/user/login");
//            driver.get("https://www.baidu.com");
//            System.out.println(driver.getCurrentUrl());
//            Thread.sleep(2000);

//            driver.navigate().refresh();

//            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//            FileUtils.copyFile(scrFile, new File("/image.png"));

            // Waiting 30 seconds for an element to be present on the page, checking
            // for its presence once every 5 seconds.
//            Wait<WebDriver> wait = new FluentWait<>(driver)
//                    .withTimeout(Duration.ofSeconds(30))
//                    .pollingEvery(Duration.ofSeconds(5))
//                    .ignoring(NoSuchElementException.class);

            //Click the link to activate the alert
//            driver.findElement(By.linkText("See a sample prompt")).click();

            //Wait for the alert to be displayed and store it in a variable
//            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            //Type your message
//            alert.sendKeys("Selenium");

            //Press the OK button
//            alert.accept();

            WebElement accountInput = driver.findElement(By.id("account"));
            accountInput.sendKeys("13999999999");
            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("123456");

            driver.findElement(By.cssSelector("button[type=submit]")).click();

            Thread.sleep(3000);

            driver.navigate().to("https://rmp.adas.com/#/detail/report/?loanId=92cae444-ba9d-4bda-a5cd-b8996fdb7b77&productId=11&type=1");

            Thread.sleep(5000);

//            driver.findElement(By.cssSelector("span[textContent='导出报告']")).click();
            driver.findElement(By.xpath("//span[text()='导出报告']")).click();

            Thread.sleep(5000);

            Path downloadPath = Paths.get(downloadDir);
            List<Path> filePaths = Files.list(downloadPath).collect(Collectors.toList());
            File file = filePaths.get(0).toFile();
            System.out.println(file);


        } finally {
//            driver.quit();
        }

    }
}
