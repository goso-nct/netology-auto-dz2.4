package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.page;

public class DashboardPage {

    @FindBy(css = ".list__item")
    private ElementsCollection cards;
    @FindBy(css = "button.button_view_extra")
    private ElementsCollection buttons;
    @FindBy(css = "[data-test-id=action-reload]")
    private SelenideElement reloadButton;

    public DashboardPage() {
    }

    public int getBalance(int nomCard) {
        var text = cards.get(nomCard).text();
        var balance = Integer.parseInt(Arrays.stream(text.split(" ")).skip(5).findFirst().orElse("0"));
        return balance;
    }

    public RefillPage refill(int toCard) {
        buttons.get(toCard).click();
        return page(RefillPage.class);
    }

    public void reload() {
        reloadButton.click();
    }

}
