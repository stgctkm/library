package library.e2e;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class 貸出と返却 {

    @LocalServerPort
    int port;

    @Test
    @Timeout(10)
    public void 図書の貸出と返却() {
        //  図書の貸出
        open("http://localhost:%d/loan/register".formatted(port));
        {
            SelenideElement 会員番号入力欄 = $(By.id("memberNumber.value"));
            SelenideElement 所蔵品番号入力欄 = $(By.id("itemNumber.value"));
            SelenideElement 登録ボタン = $(By.id("button-register-loan"));


            会員番号入力欄.setValue("1");
            所蔵品番号入力欄.setValue("2-A");
            登録ボタン.click();
        }

        {
            //  貸出図書の一覧
            SelenideElement 貸出リスト表 = $(By.id("loan-list")).should(Condition.appear);

            assertThat(Selenide.title(), is("貸出完了"));
            var 貸出リスト = 貸出リスト表.find(By.tagName("tbody"));
            var 貸出図書の一覧 = 貸出リスト.findAll(By.tagName("tr"));
            assertThat(貸出図書の一覧.size(), is(1));

            // 貸出図書の返却画面へ遷移
            var 返却リンク = $(By.id("link-return"));
            返却リンク.click();
        }

        {
            // 貸出図書の返却画面
            $(By.id("title-return")).should(Condition.appear);

            // 貸出図書の返却
            var 返却所蔵品番号入力欄 = $(By.id("itemNumber.value"));
            var 返却登録ボタン = $(By.id("button-return"));

            返却所蔵品番号入力欄.setValue("2-A");
            返却登録ボタン.click();
        }

    }
}
