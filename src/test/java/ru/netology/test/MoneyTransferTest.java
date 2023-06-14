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
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer("5000", DataHelper.getCard1Info().getCard())
                .getCards().get(1).shouldBe(text("15000"));
        dashboardPage.getCards().get(0).shouldBe(text("5000"));
        //assertEquals(5000, dashboardPage.getCardBalance(0));
    }

    @Test
    public void happyPathTransferFromCard2() {
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer("3000", DataHelper.getCard2Info().getCard())
                .getCards().get(0).shouldBe(text("13000"));
        dashboardPage.getCards().get(1).shouldBe(text("7000"));
    }

    @Test
    public void transferLimitValueFromCard1V1() {
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer("9999", DataHelper.getCard1Info().getCard())
                .getCards().get(1).shouldBe(text("19999"));
        dashboardPage.getCards().get(0).shouldBe(text("1"));
    }

    @Test
    public void transferLimitValueFromCard2V1() {
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer("9999", DataHelper.getCard2Info().getCard())
                .getCards().get(0).shouldBe(text("19999"));
        dashboardPage.getCards().get(1).shouldBe(text("1"));
    }

    @Test
    public void transferLimitValueFromCard1V2() {
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer("10000", DataHelper.getCard1Info().getCard())
                .getCards().get(1).shouldBe(text("20000"));
        dashboardPage.getCards().get(0).shouldBe(text("0"));
    }

    @Test
    public void transferLimitValueFromCard2V2() {
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer("10000", DataHelper.getCard2Info().getCard())
                .getCards().get(0).shouldBe(text("20000"));
        dashboardPage.getCards().get(1).shouldBe(text("0"));
    }

    @Test
    public void invalidActionTransferFromCard1IfAmountMoreThenBalanceTest() {
        dashboardPage
                .replenishmentCard(1)
                .invalidInput("10001", DataHelper.getCard1Info().getCard())
                .shouldBe(text("Произошла ошибка"));
    }

    @Test
    public void invalidActionTransferFromCard2IfAmountMoreThenBalanceTest() {
        dashboardPage
                .replenishmentCard(0)
                .invalidInput("10001", DataHelper.getCard2Info().getCard())
                .shouldBe(text("Произошла ошибка"));
    }

    @Test
    public void cancelTransferTest() {
        dashboardPage
                .replenishmentCard(0)
                .cancelTransfer("5000", DataHelper.getCard2Info().getCard())
                .getCards().get(1).shouldBe(text("10000"));
        dashboardPage.getCards().get(0).shouldBe(text("10000"));
    }

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard1Test() {
        dashboardPage
                .replenishmentCard(0)
                .actionTransfer("5000", DataHelper.getCard1Info().getCard())
                .getCards().get(1).shouldBe(text("10000"));
        dashboardPage.getCards().get(0).shouldBe(text("10000"));
    }

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard2Test() {
        dashboardPage
                .replenishmentCard(1)
                .actionTransfer("5000", DataHelper.getCard2Info().getCard())
                .getCards().get(1).shouldBe(text("10000"));
        dashboardPage.getCards().get(0).shouldBe(text("10000"));
    }

    @Test
    public void invalidActionTransferIfCardIsNotInServiceTest() {
        dashboardPage
                .replenishmentCard(0)
                .invalidInput("5000", "5559 0000 0000 0003")
                .shouldBe(text("Произошла ошибка"));
    }
}
