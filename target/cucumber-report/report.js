$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/features/EditAndDeleteEployee.*.feature");
formatter.feature({
  "line": 1,
  "name": "Edit/Delete existing employee functionality",
  "description": "",
  "id": "edit/delete-existing-employee-functionality",
  "keyword": "Feature"
});
formatter.scenario({
  "comments": [
    {
      "line": 3,
      "value": "# Created by Mohammad Majid on 4/11/2020"
    }
  ],
  "line": 6,
  "name": "Admin user Edit/Delete Employee",
  "description": "",
  "id": "edit/delete-existing-employee-functionality;admin-user-edit/delete-employee",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 5,
      "name": "@smoke"
    }
  ]
});
formatter.step({
  "line": 7,
  "name": "An Admin",
  "keyword": "Given "
});
formatter.step({
  "line": 8,
  "name": "I can browse to the site",
  "keyword": "When "
});
formatter.step({
  "line": 9,
  "name": "Ableto Admin Dashboard page should display",
  "keyword": "Then "
});
formatter.step({
  "line": 10,
  "name": "I can select the Action Edit",
  "keyword": "When "
});
formatter.step({
  "line": 11,
  "name": "I can edit employee details",
  "keyword": "Then "
});
formatter.step({
  "line": 12,
  "name": "the data should change in the table",
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I click the Action X",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "the employee should be deleted",
  "keyword": "Then "
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.an_Admin()"
});
formatter.result({
  "duration": 2605433008,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.i_can_browse_to_the_site()"
});
formatter.result({
  "duration": 598857498,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.ableto_Admin_Dashboard_page_should_display()"
});
formatter.result({
  "duration": 11724135,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.i_can_select_the_Action_Edit()"
});
formatter.result({
  "duration": 60951318,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.i_can_edit_employee_details()"
});
formatter.result({
  "duration": 3890582918,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.the_data_should_change_in_the_table()"
});
formatter.result({
  "duration": 32991688,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.i_click_the_Action_X()"
});
formatter.result({
  "duration": 3331306572,
  "status": "passed"
});
formatter.match({
  "location": "EditAndDeleteEmpSteps.the_employee_should_be_deleted()"
});
formatter.result({
  "duration": 15831524,
  "status": "passed"
});
});