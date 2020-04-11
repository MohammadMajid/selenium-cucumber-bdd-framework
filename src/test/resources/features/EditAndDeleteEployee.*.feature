Feature: Edit/Delete existing employee functionality

  # Created by Mohammad Majid on 4/11/2020

  @smoke
  Scenario: Admin user Edit/Delete Employee
    Given An Admin
    When I can browse to the site
    Then Ableto Admin Dashboard page should display
    When I can select the Action Edit
    Then I can edit employee details
    And the data should change in the table
    When I click the Action X
    Then the employee should be deleted