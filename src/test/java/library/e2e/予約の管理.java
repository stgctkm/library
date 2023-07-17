package library.e2e;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class 予約の管理 {

    @LocalServerPort
    int port;

    @Test
    @Timeout(10)
    public void 図書の予約_取置_貸出_返却() {
        open("http://localhost:%d".formatted(port));
        {
            // 予約画面 (図書の検索)
            var 本の予約リンク = $(By.id("link-reservation-for-member"));
            本の予約リンク.click();
        }

        // 予約画面 (図書の検索)
        {
            $(By.id("header-search-result")).should(Condition.appear);

            var キーワード入力欄 = $(By.id("value"));
            キーワード入力欄.setValue("現場");

            var 検索ボタン = $(By.id("button-search"));
            検索ボタン.click();
        }

        // 予約画面 (図書の検索結果)
        {
            var 検索結果表 = $(By.id("table-search-result")).should(Condition.appear);

            var 検索結果一覧 = 検索結果表.find(By.tagName("tbody")).findAll(By.tagName("tr"));
            assertThat(検索結果一覧.size(), is(1));

            var 予約リンク = 検索結果一覧.first().findAll(By.tagName("td")).first().find(By.tagName("a"));

            予約リンク.click();
        }

        // 予約画面
        {
            var 予約ボタン = $(By.id("button-reserve")).should(Condition.appear);

            var 会員番号入力欄 = $(By.id("value"));
            会員番号入力欄.setValue("1");
            予約ボタン.click();
        }

        // 予約完了
        {
            var 完了メッセージ = $(By.className("notification")).should(Condition.appear);

            assertThat(完了メッセージ.getText(), is("本を予約しました。"));

            var ホームリンク = $(By.id("link-to-home"));
            ホームリンク.click();
        }

        // ホーム画面
        {
            var 予約の管理リンク = $(By.id("link-reservations")).should(Condition.appear);
            予約の管理リンク.click();
        }

        // 予約の管理画面
        {
            var 予約表 = $(By.id("table-reservations")).should(Condition.appear);
            var 予約の一覧 = 予約表.find(By.tagName("tbody")).findAll(By.tagName("tr"));

            assertThat(予約の一覧.size(), is(1));

            var 取置ボタン = 予約の一覧.get(0).findAll(By.tagName("button")).first();

            取置ボタン.click();
        }

        // 取置き画面
        {
            var 取置き実行ボタン = $(By.id("button-retention")).should(Condition.appear);

            var 所蔵品番号入力欄 = $(By.id("item"));
            所蔵品番号入力欄.setValue("1-A");

            取置き実行ボタン.click();
            // ホーム画面
            var ホームリンク = $(By.id("link-to-home"));
            ホームリンク.click();
        }

        // メニュー画面
        {
            var 取置きの管理リンク = $(By.id("link-retentions"));

            取置きの管理リンク.click();
        }

        // 取置き画面
        {
            var 取置き表 = $(By.id("table-retentions")).should(Condition.appear);
            var 取置一覧 = 取置き表.find(By.tagName("tbody")).findAll(By.tagName("tr"));
            assertThat(取置一覧.size(), is(1));

            var 貸出ボタン = 取置一覧.first().find(By.tagName("button"));
            貸出ボタン.click();

            // ホーム画面
            var ホームリンク = $(By.id("link-to-home"));
            ホームリンク.click();
        }

        // メニュー画面
        {
            var 貸出と返却リンク = $(By.id("link-loan-return")).should(Condition.appear);
            貸出と返却リンク.click();
        }

        // 貸出と返却画面
        {
            var 返却リンク = $(By.id("link-return")).should(Condition.appear);
            返却リンク.click();
        }

        // 貸出図書の返却
        {
            var 返却登録ボタン = $(By.id("button-return")).should(Condition.appear);
            var 返却所蔵品番号入力欄 = $(By.id("itemNumber.value"));

            返却所蔵品番号入力欄.setValue("1-A");
            返却登録ボタン.click();
        }
    }

}
