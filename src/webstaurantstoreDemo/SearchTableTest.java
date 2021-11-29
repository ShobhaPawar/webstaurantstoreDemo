package webstaurantstoreDemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SearchTableTest {
	
	public static WebDriver driver;

	@BeforeMethod
	public void setUp(){
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);	
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@Test
	public void searchWorkTableTest(){
		driver.get("https://www.webstaurantstore.com");
		
		driver.findElement(By.name("searchval")).sendKeys("stainless work table");
		driver.findElement(By.xpath("//button[text()='Search'][@type='submit']")).click();
		
		List <WebElement> items = driver.findElements(By.xpath("//a[@data-testid='itemDescription']"));
		List <String> itemNames = new ArrayList<String>();
		
		for(int i=0; i< items.size(); i++){
			itemNames.add(items.get(i).getText());
			Assert.assertFalse(!itemNames.toString().contains("Table"),"Item " + i +" does not contain word Table in its title "+ itemNames);	
		}
		
		String nextButtonClassname = driver.findElement(By.xpath("//li[@class='rc-pagination-next']")).getAttribute("class");
			
		while(!nextButtonClassname.contains("disabled")){
			
			driver.findElement(By.xpath("//li[@class='rc-pagination-next']")).click();
			items = driver.findElements(By.cssSelector("div.ag-item.gtm-product"));
							
			for(int j=0; j< items.size(); j++){
				itemNames.add(items.get(j).getText());
				if(!itemNames.toString().contains("Table")){
					System.out.println("Item " + j +" does not contain word Table in its title "+ itemNames);
				}
			}		
			nextButtonClassname = driver.findElement(By.cssSelector(".rc-pagination-next")).getAttribute("class");
		}
	
		int totalItems = itemNames.size();		
		System.out.println("Total no of Tables displayed : "+totalItems);
		
		driver.findElement(By.xpath("(//div[@class='add-to-cart'])[last()]")).click();				
		driver.findElement(By.xpath("//button[text()='Add To Cart'][@type='submit']")).click();
		driver.findElement(By.xpath("//div[@class='notification__action']//a[text() = 'View Cart']")).click();
		
		String itemsInCart = driver.findElement(By.xpath("//span[@id='cartItemCountSpan']")).getText();
		Assert.assertEquals(itemsInCart, "1");		
		driver.findElement(By.xpath("//a[contains(@class,'emptyCartButton')]")).click();		
		driver.findElement(By.xpath("//button[text()='Empty Cart']")).click();
		
		driver.navigate().refresh();
		itemsInCart = driver.findElement(By.xpath("//span[@id='cartItemCountSpan']")).getText();
		Assert.assertEquals(itemsInCart, "0");
		String text = driver.findElement(By.xpath("//div[@class='empty-cart__text']/p[@class='header-1']")).getText();
		Assert.assertEquals(text, "Your cart is empty.");	
	}
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
}
