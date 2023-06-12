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
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .actionTransfer("5000", cardsNumber.getCard1());
    }

    @Test
    public void happyPathTransferFromCard2() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .actionTransfer("3000", cardsNumber.getCard2());
    }

    @Test
    public void transferLimitValueFromCard1V1() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .actionTransfer("9999", cardsNumber.getCard1());
    }

    @Test
    public void transferLimitValueFromCard2V1() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .actionTransfer("9999", cardsNumber.getCard2());
    }

    @Test
    public void transferLimitValueFromCard1V2() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .actionTransfer("10000", cardsNumber.getCard1());
    }

    @Test
    public void transferLimitValueFromCard2V2() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .actionTransfer("10000", cardsNumber.getCard2());
    }

    @Test
    public void invalidActionTransferFromCard1IfAmountMoreThenBalanceTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .invalidActionTransfer("10001", cardsNumber.getCard1());
    }

    @Test
    public void invalidActionTransferFromCard2IfAmountMoreThenBalanceTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .invalidActionTransfer("10001", cardsNumber.getCard2());
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

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard1Test() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(0)
                .invalidActionTransfer("3000", cardsNumber.getCard1());
    }

    @Test
    public void invalidActionTransferIfCardNumbersEqualsCard2Test() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var cardsNumber = DataHelper.CardsNumber.getCardsNumber();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .invalidActionTransfer("3000", cardsNumber.getCard2());
    }

    @Test
    public void invalidActionTransferIfCardIsNotInServiceTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCodeFor(authInfo))
                .replenishmentCard(1)
                .invalidActionTransfer("3000", "5559 0000 0000 0003");
    }

}
