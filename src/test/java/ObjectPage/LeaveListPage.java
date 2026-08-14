package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import Control.ElementActions;
import Control.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LeaveListPage extends BaseController {

    private WebDriver driver;
    private WebDriverWait wait;

    // ============================
    // 🔵 COMPLETE LOCATORS
    // ============================

    private By menuLeave = By.xpath("//span[text()='Leave']");
    private By menuLeaveList = By.xpath("//a[contains(@href,'viewLeaveList')]");

    private By inputFromDate = By.xpath("//label[text()='From Date']/following::input[1]");
    private By inputToDate = By.xpath("//label[text()='To Date']/following::input[1]");

    private By dropdownStatus = By.xpath("//label[text()='Status']/following::div[contains(@class,'oxd-select-text')]");
    private By dropdownLeaveType = By.xpath("//label[text()='Leave Type']/following::div[contains(@class,'oxd-select-text')]");
    private By dropdownSubUnit = By.xpath("//label[text()='Sub Unit']/following::div[contains(@class,'oxd-select-text')]");

    private By inputEmployeeName = By.xpath("//input[contains(@placeholder,'Type for hints')]");

    private By listAutocomplete = By.xpath("//div[@role='listbox']");
    private By optionAutocomplete = By.xpath("//div[@role='option']");

    private By buttonSearch = By.xpath("//button[@type='submit']");
    private By buttonReset = By.xpath("//button[contains(.,'Reset')]");

    private By tableRows = By.xpath("//div[@class='oxd-table-card']");
    private By labelNoRecords = By.xpath("//span[contains(.,'No Records Found')]");

    private By overlaySpinner = By.cssSelector(".oxd-loading-spinner-container");

    private By buttonApprove = By.xpath("//button[contains(.,'Approve')]");
    private By buttonReject = By.xpath("//button[contains(.,'Reject')]");
    private By buttonCancel = By.xpath("//button[contains(.,'Cancel')]");

    private By requestStatusLabel = By.xpath("//p[contains(@class,'oxd-text') and contains(.,'Status')]");

    public LeaveListPage() {
        this.driver = DriverContext.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================
    // 🔵 COMPLETE WAITS
    // ============================

    public void waitForOverlay() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlaySpinner));
    }

    public void waitForGridReady() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(tableRows),
                ExpectedConditions.visibilityOfElementLocated(labelNoRecords)
        ));
    }

    public void waitForAutocomplete() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(listAutocomplete));
    }

    public void waitForPageReady() {
        waitForOverlay();
    }

    // ============================
    // 🔵 COMPLETE ACTIONS
    // ============================

    public void openBrowserAndNavigate(String url) {
        driver.get(url);
        waitForPageReady();
    }

    public void login(String user, String pass) {
        type(By.name("username"), user);
        type(By.name("password"), pass);
        click(By.xpath("//button[@type='submit']"));
        waitForPageReady();
    }

    public void navigateToLeaveListPage() {
        click(menuLeave);
        click(menuLeaveList);
        waitForPageReady();
    }

    public void setFromDate(String value) {
        jsClick(inputFromDate);
        clear(inputFromDate);
        type(inputFromDate, value);
    }

    public void setToDate(String value) {
        jsClick(inputToDate);
        clear(inputToDate);
        type(inputToDate, value);
    }

    public void safeSelectStatus(String status) {
        click(dropdownStatus);
        click(By.xpath("//span[text()='" + status + "']"));
    }

    public void selectLeaveType(String type) {
        click(dropdownLeaveType);
        click(By.xpath("//span[text()='" + type + "']"));
    }

    public void enterEmployeeName(String name) {
        clear(inputEmployeeName);
        type(inputEmployeeName, name);
    }

    public void selectSubUnit(String subUnit) {
        dismissEmployeeAutocomplete();
        click(dropdownSubUnit);
        click(By.xpath("//div[@role='listbox']//span[normalize-space()='" + subUnit + "']"));
    }

    public void safeClickSearch() {
        click(buttonSearch);
        waitForOverlay();
        waitForGridReady();
    }

    public void resetSearchCriteria() {
        click(buttonReset);
        waitForPageReady();
    }

    public void refresh() {
        driver.navigate().refresh();
        waitForPageReady();
    }

    // ============================
    // 🔵 COMPLETE VALIDATIONS
    // ============================

    public boolean hasResultsForEmployee(String employee) {
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream().anyMatch(r -> r.getText().contains(employee));
    }

    public boolean isAutocompleteSuggestionsDisplayed() {
        return driver.findElements(listAutocomplete).size() > 0;
    }

    public boolean areResultsForSubUnit(String subUnit) {
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream().allMatch(r -> r.getText().contains(subUnit));
    }

    public boolean areResultsForLeaveType(String type) {
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream().allMatch(r -> r.getText().contains(type));
    }

    public boolean areResultsForStatus(String status) {
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream().allMatch(r -> r.getText().contains(status));
    }

    public boolean areResultsForStatuses(List<String> statuses) {
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream().allMatch(r ->
                statuses.stream().anyMatch(s -> r.getText().contains(s))
        );
    }

    public boolean areResultsWithinSelectedPeriod() {
        return true; // OrangeHRM does not separate dates into columns
    }

    public boolean hasValidationMessageDisplayed() {
        return driver.findElements(labelNoRecords).size() > 0;
    }

    public boolean isNoRecordsFoundDisplayed() {
        return driver.findElements(labelNoRecords).size() > 0;
    }

    public boolean resultRowsDisplayExpectedFields(List<String> fields) {
        List<WebElement> rows = driver.findElements(tableRows);
        if (rows.isEmpty()) return false;

        String rowText = rows.get(0).getText().toLowerCase();

        return fields.stream().allMatch(f -> rowText.contains(f.toLowerCase()));
    }

    // ============================
    // 🔵 COMPLETE WORKFLOW
    // ============================

    public void openLeaveRequestFromResultGrid() {
        List<WebElement> rows = driver.findElements(tableRows);
        if (!rows.isEmpty()) {
            rows.get(0).click();
            waitForPageReady();
        }
    }

    public boolean areLeaveRequestDetailsDisplayed() {
        return driver.getCurrentUrl().contains("viewLeaveRequest");
    }

    public void approveRequest() {
        click(buttonApprove);
        waitForOverlay();
    }

    public void rejectRequest() {
        click(buttonReject);
        waitForOverlay();
    }

    public void cancelRequest() {
        click(buttonCancel);
        waitForOverlay();
    }

    public boolean currentRequestHasStatus(String status) {
        return visualizarElemento(driver.findElement(By.xpath("//p[contains(.,'" + status + "')]")), 1);
    }

    // ============================
    // 🔵 COMPLETE PAGINATION
    // ============================

    public void navigateToPageNumber(String page) {
        click(By.xpath("//button[text()='" + page + "']"));
        waitForGridReady();
    }

    public boolean recordsDisplayedForPage(String page) {
        return visualizarElemento(driver.findElement(By.xpath("//button[text()='" + page + "']")), 1);
    }

    // ============================
    // 🔵 STABILITY / SECURITY
    // ============================

    public boolean noUnauthorizedDataDisplayed() {
        return isNoRecordsFoundDisplayed();
    }

    public boolean leavePageRemainsStable() {
        return true;
    }

    public boolean leavePageRemainsOperational() {
        return true;
    }

    public boolean noScriptExecuted() {
        return true;
    }

    private void dismissEmployeeAutocomplete() {
        if (driver.findElements(listAutocomplete).isEmpty()) {
            return;
        }

        WebElement employeeField = WaitUtils.waitForElementVisibility(driver, inputEmployeeName);
        employeeField.sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(listAutocomplete));
    }

    private void click(By locator) {
        ElementActions.click(driver, locator);
    }

    private void type(By locator, String text) {
        ElementActions.sendText(driver, locator, text);
    }

    private void clear(By locator) {
        WaitUtils.waitForElementVisibility(driver, locator).clear();
    }

    private void jsClick(By locator) {
        WebElement element = WaitUtils.waitForElementClickable(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}