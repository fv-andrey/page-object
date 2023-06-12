package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void happyPathTransferFromCard1() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .actionTransfer("5000", DataHelper.CardsNumber.getCardsNumber().getCard2());
    }

    @Test
    public void happyPathTransferFromCard2() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .actionTransfer("3000", DataHelper.CardsNumber.getCardsNumber().getCard1());
    }

    @Test
    public void cancelTransferTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .cancelTransfer();
    }
}
