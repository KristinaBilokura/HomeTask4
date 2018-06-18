package HomeTask4;

import data.UserDataProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeTask4 {
    protected EventFiringWebDriver driver;

    @BeforeClass
    @Parameters({"browser"})
    public  void setDriver(String browser) {
        driver = new EventFiringWebDriver(getDriver(browser));
        driver.register(new EventHandler());
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }
    private  WebDriver getDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        "src/main/resources/geckodriver.exe");
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        "src/main/resources/IEDriverServer.exe");
                return new InternetExplorerDriver();
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        "src/main/resources/chromedriver.exe");
                return new ChromeDriver();
        }
    }

    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "Credentials")
    public void GmailTest(String login , String password) {
        String url = "http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/index.php?controller=" +
                "AdminLogin&token=d251f363cceb5a849cb7330938c64dea";
        By loginInput = By.id("email");
        By passwordInput = By.id("passwd");
        By submitLoginButton = By.name("submitLogin");
        driver.get(url);
        driver.findElement(loginInput).sendKeys(login);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(submitLoginButton).submit();

        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("subtab-AdminCatalog"))).perform();
        driver.findElement(By.xpath("//ul[@class='submenu']//li[@data-submenu='10']")).click();
        driver.findElement(By.xpath("//a[@id='page-header-desc-configuration-add']//i")).click();
        final String s = getRandomString(10);
        final Double price = getRandomDouble();
        final Integer quantity = getRandomInteger();
        clearAndSendKeys(By.id("form_step1_name_1"), s);
        driver.findElement(By.id("tab_step3")).click();
        clearAndSendKeys(By.id("form_step3_qty_0"), String.valueOf(quantity));
        driver.findElement(By.id("tab_step2")).click();
        clearAndSendKeys(By.id("form_step2_price_ttc"), String.valueOf(price));
        driver.findElement(By.xpath("//div[@class='switch-input ']")).click();
        driver.findElement(By.xpath("//button[@class='btn btn-primary js-btn-save']")).click();
        Assert.assertTrue(driver.findElement(By.id("growls")).isDisplayed());

        driver.get("http://prestashop-automation.qatestlab.com.ua");
        driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")).click();
        driver.findElement(By.name("s")).sendKeys(s);
        driver.findElement(By.name("s")).sendKeys(Keys.ENTER);

        Assert.assertTrue(driver.findElement(By.xpath("//h1[@class='h3 product-title']/a[text()='"+s+"']")).isDisplayed());
        driver.findElement(By.xpath("//h1[@class='h3 product-title']/a[text()='"+s+"']")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='h1']")).getText(),s.toUpperCase());
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='current-price']/span")).getText().replace(",",""),
                String.valueOf(BigDecimal.valueOf(price).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue()+" ₴").replace(".",""));
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='product-quantities']//span")).getText(),String.valueOf(quantity)+ " Товары");

    }

    private List<String> getNames() {
        List<WebElement> namesFields = driver.findElements(By.xpath("//h1[@class='h3 product-title']/a"));
        List<String> names = new ArrayList<>();
        namesFields.forEach(webElement -> names.add(webElement.getText()));
        return names;
    }

    @AfterClass
    public  void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String getRandomString(int length) {
        String randomChars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder rand = new StringBuilder();
        Random rnd = new Random();
        while (rand.length() < length) {
            int index = (int) (rnd.nextFloat() * randomChars.length());
            rand.append(randomChars.charAt(index));
        }
        String randomStr = rand.toString();
        return randomStr;
    }

    public Integer getRandomInteger() {
        Random rand = new Random();
        int randomNum = rand.nextInt((100 - 1) + 1) + 1;
        return randomNum;
    }

    public Double getRandomDouble() {
        Random r = new Random();
        return 0.1 + (100 - 0.1) * r.nextDouble();
    }

    public void clearAndSendKeys(By by, String s) {
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(s);
    }

}
