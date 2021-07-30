package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

public class RefillPage
{
    @FindBy(css = "[data-test-id=amount] input")
    private SelenideElement amountField;
    @FindBy(css = "[data-test-id=from] input")
    private SelenideElement fromField;
    @FindBy(css = "[data-test-id=to] input")
    private SelenideElement toField;
    @FindBy(css = "[data-test-id=action-transfer]")
    private SelenideElement transferButton;
    @FindBy(css = "[data-test-id=error-notification]")
    private SelenideElement errorNotification;

    public RefillPage() {}

    public DashboardPage transfer(int amount, int fromCard) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(DataHelper.validCards[fromCard]);
        transferButton.click();
        return page(DashboardPage.class);
    }

    public boolean transferFromInvalidCard() {
        amountField.setValue("1000");
        fromField.setValue(DataHelper.invalidCard);
        transferButton.click();
        errorNotification.shouldBe(visible);
        return errorNotification.isDisplayed();
    }

}
