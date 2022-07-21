package stepDefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.singleton.Runner;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class test {

	Logger logger = LoggerFactory.getLogger(test.class);

	Properties props = Runner.getProperties();
	WebDriver driver = Runner.getDriver();

	@ParameterType("(?:[^,]*)(?:,\\s?[^,]*)*")
	public List<String> listOfStrings(String arg) {
		return Arrays.asList(arg.split(",\\s?"));
	}

	@Given("user launch the todo homepage")
	public void userInHomepage() throws Throwable {
		driver.get(props.getProperty("appUrl"));
		System.out.println("Testing App Launched");
	}

	@Given("user has some active items in Todo List")
	public void userActiveList() throws Throwable {
		userNavigatesToActiveList();
		List<String> activelistItems = activeToDOList();
		assertTrue("Active Item List is Zero", activelistItems.size() > 0);
		System.out.println("Active List Count is not Zero");
	}

	@When("user types {listOfStrings} Item")
	public void userTypesToDoItems(List<String> toDoItems) throws Throwable {
		for (String eachItem : toDoItems) {
			driver.findElement(By.xpath(props.getProperty("toDoInput"))).sendKeys(eachItem);
			userHitsEnterKey();
		}
		System.out.println("Items Added To ToDO List");
	}

	@When("user hit enter key")
	public void userHitsEnterKey() throws Throwable {
		driver.findElement(By.xpath(props.getProperty("toDoInput"))).sendKeys(Keys.ENTER);
	}

	@When("user clicks on Active list")
	public void userNavigatesToActiveList() throws Throwable {
		driver.findElement(By.linkText("Active")).click();
		System.out.println("Clicked on Active Tab");
	}

	@When("user clicks on Completed list")
	public void userNavigatesToCompletedList() throws Throwable {
		driver.findElement(By.linkText("Completed")).click();
		System.out.println("Clicked on Completed Tab");
	}

	@When("user clicks on All list")
	public void userNavigatesToAllList() throws Throwable {
		driver.findElement(By.linkText("All")).click();
		System.out.println("Clicked on All Tab");
	}

	@When("user marks few {listOfStrings} Items as done")
	public void markItemsAsDone(List<String> itemToBeDone) throws Throwable {
		userNavigatesToAllList();
		for (String eachItem : itemToBeDone) {
			driver.findElement(By.xpath("//label[text()='" + eachItem + "']//..//input[@type='checkbox']")).click();
			System.out.println("Item Marked as Done/Completed:" + eachItem);
		}
	}

	@When("user selects all items")
	public void user_marks_all_as_complete() throws Throwable {
		driver.findElement(By.xpath(props.getProperty("markAllComlete"))).click();
		System.out.println("All Item Marked are Done/Completed");
	}

	@When("user clicks clear completed button")
	public void clear_completedItems() throws Throwable {
		driver.findElement(By.xpath(props.getProperty("clearCompleted"))).click();
		System.out.println("All Completed Items are cleared");
	}

	@When("^user edits (.*) Items as (.*)$")
	public void editList(String Item, String editValue) throws Throwable {
		userNavigatesToAllList();
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//label[text()='" + Item + "']"))).perform();
		action.doubleClick().perform();
		action.keyDown(Keys.CONTROL);
		action.sendKeys("A");
		action.keyUp(Keys.CONTROL);
		action.build().perform();
		action.sendKeys(editValue).perform();
	}

	@When("^user removes (.*) item$")
	public void deleteListItem(String Item) throws Throwable {
		userNavigatesToAllList();
		driver.findElement(By.xpath("//label[text()='" + Item + "']//..//button[@class='destroy']")).click();
	}

	@Then("Added items {listOfStrings} should be displayed in Active list")
	public void checkActiveList(List<String> itemList) throws Throwable {
		boolean flag = false;
		List<String> exp_items = itemList;
		List<String> act_items = new ArrayList<String>();
		List<WebElement> actualListAsElements = driver.findElements(By.xpath(props.getProperty("todoList")));
		for (WebElement webElement : actualListAsElements) {
			act_items.add(webElement.getText());
		}
		if (exp_items.equals(act_items)) {
			flag = true;
		}
		assertTrue("List is not macthed", flag);
	}

	@Then("Completed List should be empty")
	public void completedListNullCheck() throws Throwable {
		List<WebElement> actualList = driver.findElements(By.xpath(props.getProperty("completedList")));
		assertEquals("Completed List Size is not as Expected", actualList.size(), 0);
	}

	@Then("Active List should be empty")
	public void activeListNullCheck() throws Throwable {
		List<WebElement> actualList = driver.findElements(By.xpath(props.getProperty("todoList")));
		assertEquals("Active List Size is not as Expected", actualList.size(), 0);
	}

	@Then("Items left count should equal activelist Count")
	public void itemsLeftCountCheck() throws Throwable {
		userNavigatesToAllList();
		List<String> activeList = activeToDOList();
		int activeListSize = activeList.size();
		String expectedCount = driver.findElement(By.xpath(props.getProperty("itemLeftCount"))).getText();
		assertEquals("Items Left Count not Matched with Active List count", activeListSize,
				Integer.parseInt(expectedCount));

	}

	@Then("Completed items {listOfStrings} should be displayed in Completed list")
	public void markedAsDoneItemCheck(List<String> completedItems) throws Throwable {
		boolean flag = false;
		List<String> exp_items = completedItems;
		List<String> act_Items = new ArrayList<String>();
		List<WebElement> actualListAsElements = driver.findElements(By.xpath(props.getProperty("completedList")));
		for (WebElement eachElement : actualListAsElements) {
			act_Items.add(eachElement.getText());
		}
		if (exp_items.equals(act_Items)) {
			flag = true;
		}
		assertTrue("Completed Item List is not as expected", flag);
	}

	@Then("Completed items not dislayed in Active List")
	public void completedItemNotInActiveCheck() throws Throwable {
		boolean flag = true;
		List<String> Completed_items = completedToDOList();
		List<String> active_Items = activeToDOList();
		if (Completed_items.contains(active_Items)) {
			flag = false;
		}
		assertTrue("Completed Item List are available in Active List", flag);
	}

	@Then("Newly Added items {listOfStrings} should be displayed in Active list")
	public void updatedToDoInActiveCheck(List<String> newList) throws Throwable {
		boolean flag = true;
		List<String> new_items = newList;
		List<String> active_Items = activeToDOList();
		if (active_Items.contains(new_items)) {
			flag = false;
		}
		assertTrue("Newly Added Item are not available in Active List", flag);
	}

	@Then("Items left should be zero")
	public void itemsLeftCheckZero() throws Throwable {
		String actualList = driver.findElement(By.xpath(props.getProperty("itemLeftCount"))).getText();
		assertEquals("Item Left count not as Expected", Integer.parseInt(actualList), 0);
	}

	@Then("^All List should display (.*) value$")
	public void checkEditedValueDisplayed(String editedValued) throws Throwable {
		boolean flag = false;
		String exp_value = editedValued;
		List<WebElement> actualListAsElement = driver.findElements(By.xpath(props.getProperty("allList")));
		for (WebElement webElement : actualListAsElement) {
			if (webElement.getText().equals(exp_value)) {
				flag = true;
				break;
			}
		}
		assertTrue("Edited Value is not displayed", flag);
	}

	@Then("^All List should not display deleted (.*) value$")
	public void checkRemovedValueNotDisplayed(String editedValued) throws Throwable {
		boolean flag = true;
		String exp_value = editedValued;
		List<WebElement> actualListAsElement = driver.findElements(By.xpath(props.getProperty("allList")));
		for (WebElement webElement : actualListAsElement) {
			if (webElement.getText().equals(exp_value)) {
				flag = false;
				break;
			}
		}
		assertTrue("Removed Value is displayed", flag);
	}

	public List<String> activeToDOList() {
		List<String> activeListInText = new ArrayList<String>();
		List<WebElement> activeListAsElements = driver.findElements(By.xpath(props.getProperty("todoList")));
		for (WebElement webElement : activeListAsElements) {
			activeListInText.add(webElement.getText());
		}
		return activeListInText;
	}

	public List<String> completedToDOList() {
		List<String> completedListInText = new ArrayList<String>();
		List<WebElement> completedListAsElements = driver.findElements(By.xpath(props.getProperty("completedList")));
		for (WebElement webElement : completedListAsElements) {
			completedListInText.add(webElement.getText());
		}
		return completedListInText;
	}
}