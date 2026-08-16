@Leave
# Demo OrangeHRM data changes frequently; scenarios are resilient to empty results when filters apply.

Feature: Leave List Management
    As an HR administrator
    I want to search and manage leave requests
    So that I can monitor employee leave activities effectively

    Background:
        Given open the browser and navigate to "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
        And the user is logged into OrangeHRM with user "Admin" and password "admin123"
        And user navigates to Leave List page

    @search_leave_request @test-01 @browserstack
    Scenario Outline: Search leave request with multiple filters
        When the user enters from date "<from_date>"
        And the user enters to date "<to_date>"
        And the user selects leave status "<status>"
        And the user selects leave type "<leave_type>"
        And the user enters Leave employee name "<employee_name>"
        And the user selects Leave sub unit "<sub_unit>"
        And the user clicks the Search button
        Then the system should display records for "<employee_name>"

        Examples:
            | from_date  | to_date    | status | leave_type       | employee_name | sub_unit        |
            | 2024-01-01 | 2026-12-31 | Taken  | CAN - Bereavement| a             | Administration  |

    @validate_autocomplete @test-02
    Scenario Outline: Validate employee autocomplete suggestions
        When the user types "<partialName>" in Leave Employee Name field
        Then employee suggestions should be displayed

        Examples:
            | partialName |
            | a           |
            | r           |

    @search_by_subunit @test-03
    Scenario Outline: Search leave requests by sub unit "<subUnit>"
        When the user selects Leave sub unit "<subUnit>"
        And the user clicks the Search button
        Then all returned Leave requests should belong to "<subUnit>"

        Examples:
            | subUnit          |
            | Administration   |
            | Engineering      |
            | Human Resources  |

    @search_by_leave_type @test-04
    Scenario Outline: Search leave requests by leave type "<leaveType>"
        When the user selects all statuses
        And the user selects leave type "<leaveType>"
        And the user clicks the Search button
        Then all leave records should have leave type "<leaveType>"

        Examples:
            | leaveType         |
            | CAN - Vacation    |
            | US - Personal     |
            | CAN - Bereavement |

    @search_by_leave_status @test-05
    Scenario Outline: Search leave requests by leave status "<status>"
        When the user selects leave status "<status>"
        And the user clicks the Search button
        Then all returned requests should have status "<status>"

        Examples:
            | status           |
            | Rejected         |
            | Cancelled        |
            | Pending Approval |
            | Scheduled        |
            | Taken            |

    @search_by_leave_type @test-06
    Scenario Outline: Search leaves by leave type
        When the user selects all statuses
        And the user selects leave type "<leaveType>"
        And the user clicks Search
        Then the displayed records should belong to leave type "<leaveType>"

        Examples:
            | leaveType         |
            | CAN - Personal    |
            | US - Vacation     |
            | CAN - Bereavement |

    @search_by_multiple_statuses @test-07
    Scenario Outline: Search leave requests by multiple statuses
        When the user selects the following leave statuses:
            | status    |
            | <status1> |
            | <status2> |
        And the user clicks the Search button
        Then returned records should have either "<status1>" or "<status2>"

        Examples:
            | status1          | status2   |
            | Pending Approval | Scheduled |
            | Taken            | Cancelled |

    @search_by_date_range @test-08
    Scenario Outline: Search leave requests using date range
        When the user selects all statuses
        And the user enters from date "<from_date>"
        And the user enters to date "<to_date>"
        And the user clicks the Search button
        Then all leave requests should be within the selected period

        Examples:
            | from_date  | to_date    |
            | 2024-01-01 | 2024-12-31 |
            | 2025-01-01 | 2026-12-31 |

    @validate_invalid_date_range @test-09
    Scenario Outline: Validate invalid date ranges
        When the user enters from date "<from_date>"
        And the user enters to date "<to_date>"
        And the user clicks the Search button
        Then a validation message should be displayed

        Examples:
            | from_date  | to_date    |
            | 2026-12-31 | 2026-01-01 |

    @reset_leave_filters @test-10
    Scenario: Reset search criteria
        Given the user performs a leave search using filters
        When the user clicks the Reset button
        Then all fields should return to default values
        And all filters should be cleared

    @validate_leave_result_grid @test-11
    Scenario: Verify leave request details from result grid
        When the user searches for leave requests of "a"
        And the user clicks the Search button
        Then each result row should display:
            | field         |
            | Employee      |
            | Leave Type    |
            | Leave Balance |
            | Date          |
            | Status        |

    @open_leave_request_details @test-12
    Scenario: Open leave request details
        Given leave requests are displayed for employee "a"
        When the user opens a leave request from the result grid
        Then leave request details should be displayed

    @approve_pending_leave_request @test-13
    Scenario: Approve pending leave request
        Given a leave request for employee "a" is in Pending Approval status
        When the user approves the request
        Then the request status should become Approved

    @reject_pending_leave_request @test-14
    Scenario: Reject pending leave request
        Given a leave request for employee "a" is in Pending Approval status
        When the user rejects the request
        Then the request status should become Rejected

    @cancel_leave_request @test-15
    Scenario: Cancel leave request
        Given a leave request exists for employee "a"
        When the user cancels the leave request
        Then the request status should become Cancelled

    @validate_leave_persistence @test-16
    Scenario: Validate leave data persistence after refresh
        Given a leave request for employee "a" is in Pending Approval status
        When the user approves the request
        And the user refreshes the browser
        Then the leave request for "a" should remain Approved

    @leave_list_pagination @test-17
    Scenario Outline: Navigate through leave list pages
        When the user navigates to Leave List page number "<page>"
        Then leave records for Leave List page "<page>" should be displayed

        Examples:
            | page |
            | 1    |

    @search_no_matching_records @test-18
    Scenario Outline: Search with no matching records
        When the user searches Leave employee "<employee_name>"
        And the user clicks the Search button
        Then the Leave page should display No Records Found

        Examples:
            | employee_name        |
            | EmployeeDoesNotExist |

    @security_sql_injection @test-19
    Scenario Outline: Prevent SQL Injection in leave search
        When the user enters "<malicious_input>" in Leave Employee Name field
        And the user clicks the Search button
        Then unauthorized data should not be displayed
        And the Leave page should remain stable

        Examples:
            | malicious_input |
            | ' OR 1=1 --     |
            | admin' --       |

    @security_xss @test-20
    Scenario Outline: Prevent XSS execution in leave search
        When the user enters "<payload>" in Leave Employee Name field
        And the user clicks the Search button
        Then no script should be executed in Leave page
        And the Leave page should remain operational

        Examples:
            | payload                       |
            | <script>alert('xss')</script> |

    @create_leave_request @test-21
    Scenario: Create leave request and validate in leave list
        Given employee "a" creates a leave request
        When the administrator searches the employee in Leave List
        Then the new leave request should be displayed

    @leave_approval_workflow @test-22
    Scenario: Request leave and approve workflow
        Given employee "a" submits a leave request
        And the leave request is displayed in the Leave List with status "Pending Approval"
        When the administrator approves the request
        Then the leave request status should be "Approved"
        And the approved leave request should be displayed in the Leave List

    @leave_rejection_workflow @test-23
    Scenario: Request leave and reject workflow
        Given employee "a" submits a leave request
        And the leave request is displayed in the Leave List with status "Pending Approval"
        When the administrator rejects the request
        Then the leave request status should be "Rejected"
        And the rejected leave request should be displayed in the Leave List
