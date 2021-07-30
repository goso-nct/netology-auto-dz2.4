package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
class MoneyTransferTest
{
    int beforeBalance0, beforeBalance1;
    int afterBalance0, afterBalance1;
    DashboardPage dashboardPage;

    LoginPage loginPage = open("http://localhost:9999", LoginPage.class);

    void report(String name, int b0, int b1, int a0, int a1) {
        System.out.println("\n" + name);
        System.out.printf("Card %16s: %7d %7d %n", DataHelper.validCards[0], b0, a0);
        System.out.printf("Card %16s: %7d %7d %n%n", DataHelper.validCards[1], b1, a1);
    }

    void gotoDashboard() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    void getBeforeBalances() {
        beforeBalance0 = dashboardPage.getBalance(0);
        beforeBalance1 = dashboardPage.getBalance(1);
    }

    void getAfterBalances() {
        afterBalance0 = dashboardPage.getBalance(0);
        afterBalance1 = dashboardPage.getBalance(1);
    }

    @AfterEach
    void tearDown() { closeWebDriver(); }

    @Test
    void shouldRefillFirstCard() {
        gotoDashboard();
        getBeforeBalances();

        var refillPage = dashboardPage.refill(0);
        var amount = 500;
        dashboardPage = refillPage.transfer(amount,1);

        getAfterBalances();
        report("Transfer " + amount + " from 2 to 1",
                beforeBalance0, beforeBalance1, afterBalance0, afterBalance1);

        assertEquals(beforeBalance0 + amount, afterBalance0);
        assertEquals(beforeBalance1 - amount, afterBalance1);
    }

    @Test
    void shouldRefillSecondCard() {
        gotoDashboard();
        getBeforeBalances();

        var refillPage = dashboardPage.refill(1);
        var amount = 500;
        dashboardPage = refillPage.transfer(amount,0);

        getAfterBalances();
        report("Transfer " + amount + " from 1 to 2",
                beforeBalance0, beforeBalance1, afterBalance0, afterBalance1);

        assertEquals(beforeBalance0 - amount, afterBalance0);
        assertEquals(beforeBalance1 + amount, afterBalance1);
    }

    @Test
    void z_dontShouldNegativeAccount() {
        gotoDashboard();
        getBeforeBalances();

        var refillPage = dashboardPage.refill(0);
        var amount = beforeBalance1 + 1_000;
        dashboardPage = refillPage.transfer(amount,1);

        getAfterBalances();
        report("Checking for negative account by transfer " + amount + " from 2 to 1",
                beforeBalance0, beforeBalance1, afterBalance0, afterBalance1);

        assertTrue(afterBalance0 >= 0);
        assertTrue(afterBalance1 >= 0);
    }

    @Test
    void shouldDontTransferFromInvalidCard() {
        gotoDashboard();
        var refillPage = dashboardPage.refill(0);
        assertTrue(refillPage.transferFromInvalidCard());
    }

    @Test
    void shouldReloadedMoneyBeSame() {
        gotoDashboard();
        getBeforeBalances();

        dashboardPage.reload();

        getAfterBalances();
        report("Checking refresh, accounts be same",
                beforeBalance0, beforeBalance1, afterBalance0, afterBalance1);

        assertEquals(beforeBalance0, afterBalance0);
        assertEquals(beforeBalance1, afterBalance1);
    }

    @Test
    void shouldBeErrorIfInvalidAuth() {
        assertTrue(loginPage.invalidLogin());
    }

}

