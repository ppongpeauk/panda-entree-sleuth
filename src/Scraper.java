import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Interacts with the Panda Express Guest Experience Survey for free entree codes
 * @param email - email to use for the survey
 * @param timeBetweenPages - time to wait between each page load
*/

public class Scraper {
  private static String mainUrl = "https://pandaguestexperience.com";
  private String email;

  // delay between each page load
  private Duration waitTime;

  private WebDriver driver;

  public Scraper(String email, int timeBetweenPages) {
    this.email = email;
    this.waitTime = Duration.ofSeconds(timeBetweenPages);

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(this.waitTime);
    driver.manage().window().maximize();
  }

  public Scraper(String email) {
    // default seconds between generations to 5 seconds
    this(email, 1);
  }

  public void generateCodes(int numCodes) {
    for (int i = 0; i < numCodes; i++) {
      // start with code input page
      driver.get(mainUrl);

      // repeatedly click next until the browser reaches the last page
      while (!driver.getCurrentUrl().contains("Finish.aspx")) {
        /*
          check if the email field is present
          element id S000057 - email
          element id S000064 - confirm email
        */

        if (driver.findElements(By.id("S000057")).size() > 0) {
          // if the email fields are present, fill them in with the email
          driver.findElement(By.id("S000057")).sendKeys(this.email);
          driver.findElement(By.id("S000064")).sendKeys(this.email);
        }

        // click next if the next button is present
        if (driver.findElements(By.id("NextButton")).size() > 0)
          driver.findElement(By.id("NextButton")).click();
      }
    }
    // close the browser on finish
    driver.close();
  }
}

class App {
  public static void main(String[] args) throws Exception {
      Scraper scraper = new Scraper("email@example.com", 0);
      scraper.generateCodes(4);
  }
}
