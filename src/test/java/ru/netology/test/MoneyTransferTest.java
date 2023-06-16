package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        dashboardPage = new LoginPage()
                .validLogin(DataHelper.getAuthInfo())
                .validVerify(DataHelper.getVerificationCodeFor());
        int firstCard = dashboardPage.getCardBalance(0);
        int secondCard = dashboardPage.getCardBalance(1);
        if (firstCard < 10000) {
            dashboardPage.replenishmentCard(0)
                    .actionTransfer(Integer.toString(10000 - firstCard), DataHelper.getCard2Info().getCard());
        } else if (firstCard > 10000) {
            dashboardPage.replenishmentCard(1)
                    .actionTransfer(Integer.toString(10000 - secondCard), DataHelper.getCard1Info().getCard());
        }
    }

    @Test
    public void happyPathTransferFromCard1() {
        int amount = 5000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard1Info().getCard());
        assertEquals(initBalance1 - amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 + amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void happyPathTransferFromCard2() {
        int amount = 5000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard2Info().getCard());
        assertEquals(initBalance1 + amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 - amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void transferLimitValueFromCard1V1() {
        int amount = 9999;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard1Info().getCard());
        assertEquals(initBalance1 - amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 + amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void transferLimitValueFromCard2V1() {
        int amount = 9999;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard2Info().getCard());
        assertEquals(initBalance1 + amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 - amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void transferLimitValueFromCard1V2() {
        int amount = 10000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard1Info().getCard());
        assertEquals(initBalance1 - amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 + amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void transferLimitValueFromCard2V2() {
        int amount = 10000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard2Info().getCard());
        assertEquals(initBalance1 + amount, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2 - amount, dashboardPage.getCardBalance(1));
    }

    @Test
    public void invalidActionTransferFromCard1IfAmountMoreThenBalanceTest() {
        int amount = 10001;
        String msg = "Произошла ошибка";
        dashboardPage
                .replenishmentCard(1)
                .invalidInput(Integer.toString(amount), DataHelper.getCard1Info().getCard(), msg);
    }

    @Test
    public void invalidActionTransferFromCard2IfAmountMoreThenBalanceTest() {
        String msg = "Произошла ошибка";
        dashboardPage
                .replenishmentCard(0)
                .invalidInput("10001", DataHelper.getCard2Info().getCard(), msg);
    }

    @Test
    public void cancelTransferTest() {
        int amount = 5000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(0)
                .cancelTransfer(Integer.toString(amount), DataHelper.getCard2Info().getCard());
        assertEquals(initBalance1, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2, dashboardPage.getCardBalance(1));
    }

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard1Test() {
        int amount = 5000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard1Info().getCard());
        assertEquals(initBalance1, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2, dashboardPage.getCardBalance(1));
    }

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard2Test() {
        int amount = 5000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer(Integer.toString(amount), DataHelper.getCard2Info().getCard());
        assertEquals(initBalance1, dashboardPage.getCardBalance(0));
        assertEquals(initBalance2, dashboardPage.getCardBalance(1));
    }

    @Test
    public void invalidActionTransferIfCardIsNotInServiceTest() {
        String msg = "Произошла ошибка";
        int amount = 5000;
        dashboardPage
                .replenishmentCard(0)
                .invalidInput(Integer.toString(amount), "5559 0000 0000 0003", msg);
    }
}
