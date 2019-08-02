import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import webprobe.WebProbe;
import webprobe.enums.BrowserType;
import webprobe.pages.Page;
import webprobe.pages.PageBlock;
import webprobe.pages.PageElement;

public class Test1 {

    Page yandexPage;
    PageBlock commonElements;
    PageElement input;
    PageElement searchButton;
    WebProbe webProbe;


    private void initYandexPage(WebProbe webProbe){
        By inputLocator = By.cssSelector(".input__control.input__input");
        By searchButtonLocator = By.cssSelector("button.suggest2-form__button");

        input = new PageElement(inputLocator);
        searchButton = new PageElement(searchButtonLocator).setExpectedText("Найти");

        yandexPage = new Page(webProbe);
        commonElements = new PageBlock();

        commonElements.addPageElement(input);
        commonElements.addPageElement(searchButton);

        yandexPage.addPageBlock(commonElements);
        yandexPage.setLastLoadElement(searchButton);

        yandexPage.addErrorString("400 Error");

    }


    @Test
    public void test1(){
        By searchResultLocator = By.cssSelector(".serp-list");
        String testString = "Тестовая строка";

        webProbe = new WebProbe(BrowserType.CHROME);
        initYandexPage(webProbe);

        webProbe.navigateTo("https://yandex.ru/");
        webProbe.maximize();
        yandexPage.verify();

        input.type(testString);
        searchButton.click();

        webProbe.waitForElementToBeVisibleBy(searchResultLocator, 10);

        if (!webProbe.getText(searchResultLocator).contains(testString)){
            Assert.fail("В результатах поиска не найдена строка" +  testString);
        }

    }

    @AfterMethod
    public void afterMethod(){
        webProbe.stop();
    }

}