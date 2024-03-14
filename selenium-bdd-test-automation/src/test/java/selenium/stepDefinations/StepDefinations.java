package selenium.stepDefinations;

import com.cucumber.listener.Reporter;
import com.google.common.io.Files;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StepDefinations {

    public static String str;
    public static WebDriver driver;

    static final Logger logger = LogManager.getLogger(StepDefinations.class.getName());

    public static String getPropertyData(String Key)
    {
        String value= null;
        try
        {
            String currentDirectory = System.getProperty("user.dir");
            File file = new File (currentDirectory+"/src/test/resources/locators/webElements.properties");
            FileInputStream fileinput = new FileInputStream(file);
            Properties properties=new Properties();
            properties.load(fileinput);
            fileinput.close();
            value = properties.getProperty(Key);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    public static By getLocatorValue(String Key) throws Exception
    {
        try
        {
            String currentDirectory = System.getProperty("user.dir");
            File file = new File (currentDirectory+"/src/test/resources/locators/webElements.properties");
            FileInputStream fileinput = new FileInputStream(file);
            Properties properties=new Properties();
            properties.load(fileinput);
            fileinput.close();



            //System.out.println("Read Value is : "+value);
            // Read value using the logical name as Key
            String locator = properties.getProperty(Key);

            // Split the value which contains locator type and locator value
            String locatorType = locator.split("__")[0];
            String locatorValue = locator.split("__")[1];
            // Return a instance of By class based on type of locator
            if (locatorType.toLowerCase().equals("id"))
                return By.id(locatorValue);
            else if (locatorType.toLowerCase().equals("name"))
                return By.name(locatorValue);
            else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
                return By.className(locatorValue);
            else if ((locatorType.toLowerCase().equals("tagname"))|| (locatorType.toLowerCase().equals("tag")))
                return By.className(locatorValue);
            else if ((locatorType.toLowerCase().equals("linktext"))|| (locatorType.toLowerCase().equals("link")))
                return By.linkText(locatorValue);
            else if (locatorType.toLowerCase().equals("partiallinktext"))
                return By.partialLinkText(locatorValue);
            else if ((locatorType.toLowerCase().equals("cssselector"))|| (locatorType.toLowerCase().equals("css")))
                return By.cssSelector(locatorValue);
            else if (locatorType.toLowerCase().equals("xpath"))
                return By.xpath(locatorValue);
            else
                throw new Exception("\n"+ "Locator type '" + locatorType	+ "' is not defined!!");

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @After
    public void afterScenario(Scenario scenario)
    {
        if (scenario.isFailed())
        {
            Date d = new Date();
            String timeStamp = d.toString().replace(":", "_").replace(" ", "_");
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            try {

                TakesScreenshot scrShot =((TakesScreenshot)driver);
                File srcpath = scrShot.getScreenshotAs(OutputType.FILE);
                File destinationPath = new File(System.getProperty("user.dir") + "/target/ScreenPrints/" + screenshotName +"_Failed At_ "+ timeStamp + ".png");
                Files.copy(srcpath, destinationPath);
                Reporter.addScreenCaptureFromPath(destinationPath.toString());

            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
            driver.close();
        }
        else
        {
            driver.close();
        }
    }

    @Given("^User Opens chrome browser and launch url \"([^\"]*)\"$")
    public void user_opens_chrome_browser_and_launch_url(String urlValue) throws Throwable
    {
        DOMConfigurator.configure("log4j.xml");
        logger.info("The test is started - "+ LocalDateTime.now());
        System.setProperty("webdriver.chrome.driver","libs/chromedriver.exe");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory",System.getProperty("user.dir") + File.separator + "src" + File.separator +"test"+ File.separator +"resources"+ File.separator +"downloads");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.get(getPropertyData(urlValue));
        driver.manage().window().maximize();
        Thread.sleep(5000);
        String cookies = driver.getCurrentUrl();
        if(cookies.contains("consent.google.com")) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,1000)");
            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[text()='Accept all']")).click();
            logger.info("The consent.google.com accepted ");
        }
    }

    @And("^User enters data \"([^\"]*)\" in TextBox \"([^\"]*)\"$")
    public void user_enters_data_in_TextBox(String inputData, String webElement) throws Throwable
    {
        try {
            WebElement textBox = driver.findElement(StepDefinations.getLocatorValue(webElement));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(textBox));
            textBox.clear();
            textBox.sendKeys(inputData);

        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @And("^User enters data \"([^\"]*)\" in TextBox \"([^\"]*)\" and Enter$")
    public void user_enters_data_in_TextBox_andEnter(String inputData, String webElement) throws Throwable
    {
        try {
            WebElement textBox = driver.findElement(StepDefinations.getLocatorValue(webElement));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(textBox));
            textBox.clear();
            textBox.sendKeys(inputData);
//            textBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @And("^User clicks on \"([^\"]*)\"$")
    public void user_Clicks_on(String webElement) throws Throwable
    {	try {

        WebElement action = driver.findElement(StepDefinations.getLocatorValue(webElement));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(action));
        action.click();
        Thread.sleep(2000);
        } catch (Exception e) {
        e.printStackTrace();
        throw new Exception(e);
        }


    }

    @And("^User enters \"([^\"]*)\" data in Auto-Suggestion box \"([^\"]*)\"$")
    public void user_enters_data_in_Auto_Suggestion_box(String inputData, String webElement) throws Throwable
    {
        try {
            WebElement autosuggestiontextBox = driver.findElement(StepDefinations.getLocatorValue(webElement));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(autosuggestiontextBox));
            autosuggestiontextBox.sendKeys(inputData);
            Thread.sleep(1000);
            Actions action = new Actions(driver);
            action.sendKeys(Keys.DOWN).perform();
            action.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User displayed with \"([^\"]*)\"$")
    public void user_displayed_with(String Element) throws Throwable
    {
        try {
            WebElement pageObject = driver.findElement(StepDefinations.getLocatorValue(Element));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(pageObject));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User verify web element \"([^\"]*)\" is present$")
    public void user_verify_the_webelement(String Element) throws Throwable
    {
        try {
            WebElement pageObject = driver.findElement(StepDefinations.getLocatorValue(Element));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(pageObject));
            if (pageObject.isDisplayed() && pageObject.isEnabled()) {
//                Reporter.addStepLog("Element is available and can be interacted with.");
                System.out.println("Element is available and can be interacted with.");
            } else {
//                Reporter.addStepLog("Element is not available or not interactable.");
                System.out.println("Element is not available or not interactable.");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }

    }
    @Given("^User verify web page contains the text \"([^\"]*)\"$")
    public void user_verify_the_webpage_contains_text(String data) throws Throwable {
        try {
            String xpath_str = "//*[contains(text(), '"+data+"')]";
            WebElement pageObject = driver.findElement(By.xpath(xpath_str));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(pageObject));
            Assert.assertEquals(true,pageObject.isDisplayed());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User selects Radio button \"([^\"]*)\"$")
    public void user_selects_Radio_button(String arg1) throws Throwable
    {
        try {

            WebElement radiobutton = driver.findElement(StepDefinations.getLocatorValue(arg1));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(radiobutton));
            boolean bValue;
            bValue = radiobutton.isSelected();
            if(bValue == false)
            {
                radiobutton.click();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User selects Checkbox \"([^\"]*)\"$")
    public void user_selects_Checkbox(String arg1) throws Throwable
    {
        try {

            WebElement checkbox = driver.findElement(StepDefinations.getLocatorValue(arg1));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(checkbox));
            boolean bValue;
            bValue = checkbox.isSelected();
            if(bValue == false)
            {
                checkbox.click();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User selects value in Listbox \"([^\"]*)\"$")
    public void user_selects_value_in_Listbox(String inputData) throws Throwable
    {
        try {
            Thread.sleep(1000);
            WebElement listbox = driver.findElement(StepDefinations.getLocatorValue(inputData));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(listbox));
            List<WebElement> list = listbox.findElements(By.tagName("li"));
            Thread.sleep(1000);
            listbox.sendKeys(Keys.CONTROL);
            Thread.sleep(1000);
            list.get(0).click();
//
//				list.get(2).click();
//
//				list.get(4).click();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User selects \"([^\"]*)\" from dropdown \"([^\"]*)\"$")
    public void user_selects_from_dropdown(String inputData, String webElement) throws Throwable
    {
        try {

            WebElement element = driver.findElement(StepDefinations.getLocatorValue(webElement));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            Select dropdown	= new Select(element);
            dropdown.selectByVisibleText(inputData);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Given("^User captutres screenprints \"([^\"]*)\"$")
    public void user_captutres_screenprints(String filename) throws Throwable
    {
        try {

            TakesScreenshot scrShot =((TakesScreenshot)driver);
            File srcpath = scrShot.getScreenshotAs(OutputType.FILE);
            File destinationPath = new File(System.getProperty("user.dir") + "/src/test/resources/downloads/" + filename + ".png");
            Files.copy(srcpath, destinationPath);
            Reporter.addScreenCaptureFromPath(String.valueOf(destinationPath),"ReportScreenshot");

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @And("^User close the webdriver instance$")
    public void user_close_the_webdriver_instance() throws Throwable
    {
        driver.quit();
    }


    @When("^User verifies the \"([^\"]*)\"$")
    public void userVerifiesThe(String expectedTitle) throws Exception {
        try {

            String actualTitle = driver.getTitle();
            Assert.assertEquals(expectedTitle,actualTitle);
         }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @And("^User enters the text \"([^\"]*)\" in \"([^\"]*)\"$")
    public void userEntersTheTextIn(String arg0, String arg1) throws Throwable {

    }

    @And("^User clicks on trip type$")
    public void userClicksOnTripType() throws Exception {
        try {

            WebElement action = driver.findElement(StepDefinations.getLocatorValue("TripDropdown"));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(action));
            action.click();
            Thread.sleep(2000);
            WebElement Onewaylist = driver.findElement(StepDefinations.getLocatorValue("OneWayTrip"));
            WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait1.until(ExpectedConditions.visibilityOf(action));
            Onewaylist.click();
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @And("^User clear data in test box \"([^\"]*)\"$")
    public void userClearDataInTestBox(String arg0) throws Throwable {
        try {

            WebElement action = driver.findElement(StepDefinations.getLocatorValue(arg0));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(action));
            action.clear();
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @And("^User enters date in TextBox \"([^\"]*)\"$")
    public void userEntersDateInTextBox(String webElement) throws Throwable {
        try {
            WebElement textBox = driver.findElement(StepDefinations.getLocatorValue(webElement));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(textBox));
            LocalDate currentDate = LocalDate.now();
            LocalDate nextMonth = currentDate.plusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd");
            String formattedDate = nextMonth.format(formatter);
            textBox.sendKeys(formattedDate);
            Thread.sleep(5000);
            textBox.sendKeys(Keys.TAB);
            Thread.sleep(10000);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
        }

    @And("^User scroll down the page$")
    public void userScrollDownThePage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)");
    }

    @And("^User log the details of \"([^\"]*)\"$")
    public void userLogTheDetailsOf(String arg0) throws Throwable {
        WebElement airlines_data =  driver.findElement(StepDefinations.getLocatorValue(arg0));
        airlines_data.getText();
        logger.info("The log details for the "+arg0 + "is " + airlines_data.getText());


    }
}
