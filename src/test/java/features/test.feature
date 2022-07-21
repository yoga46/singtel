Feature: Test Todo fuctionalities
  Verify if the user is able to add todo items, mark complete and clear items. 
  
   Scenario Outline: User adds the todo Items
    Given user launch the todo homepage
    When user types <ToDO> Item
    And user hit enter key
    And user clicks on Active list
    Then Added items <ToDO> should be displayed in Active list
    When user clicks on Completed list
    Then Completed List should be empty
    
    Examples:
    |ToDO                                                                                    | 
    |Exercise,Check Post,Check Mails,Prepare Breakfast,Set Alarm,Order Lunch,Test List,Coding|
    
   Scenario: User marks all items as done and unmark it again in single click
    Given user has some active items in Todo List
    When user selects all items
    And user clicks on Active list
    Then Active List should be empty
    And Items left count should equal activelist Count
    And Items left should be zero
    When user selects all items
    And user clicks on Completed list
    Then Completed List should be empty
    And Items left count should equal activelist Count
   
   Scenario Outline: User marks few Items as done
    Given user has some active items in Todo List
    When user marks few <ToDO> Items as done
    And user clicks on Completed list
    Then Completed items <ToDO> should be displayed in Completed list
    And Completed items not dislayed in Active List
    
    Examples:
    |ToDO                                | 
    |Exercise,Prepare Breakfast,Set Alarm|
    
   Scenario Outline: User edits the existing active todo Item
    Given user has some active items in Todo List
    When user edits <ActiveToDO> Items as <EditValue>
    And user hit enter key
    Then All List should display <EditValue> value
    
    Examples:
    |ActiveToDO   |EditValue          |
    |Check Mails  |Check Offical mails|
    
   Scenario Outline: User edits the completed active todo Item
    Given user has some active items in Todo List
    When user edits <CompletedToDO> Items as <EditValue>
    And user hit enter key
    Then All List should display <EditValue> value
    
    Examples:
    |CompletedToDO   |EditValue|
    |Exercise        |Workout  |
    
   Scenario Outline: User removes/detele Item from the todo list
    Given user has some active items in Todo List
    When user removes <ItemsToBeRemoved> item
    Then All List should not display deleted <ItemsToBeRemoved> value
    
    Examples:
    |ItemsToBeRemoved   |
    |Order Lunch        |
    
   Scenario: User clears all the completed list
    Given user has some active items in Todo List
    When user clicks clear completed button
    Then Completed List should be empty
    
   Scenario Outline: User tries to add different combinations of input
    Given user launch the todo homepage
    When user types <ToDO> Item
    And user hit enter key
    And user clicks on Active list
    Then Newly Added items <ToDO> should be displayed in Active list
    
    Examples:
    |ToDO                                                                                    | 
    |E#$12,@#$%,@come,3heat, space,qwertyuiopa;dlfkjefhgvbnm.bvcxzasfghhytrrewqasderftgyhyjukilo|