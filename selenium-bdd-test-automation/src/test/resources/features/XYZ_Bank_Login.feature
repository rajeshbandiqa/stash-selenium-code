Feature: Flight Search - Perform test simulation on web site www.google.com/travel/flights

  Background: 
  Given User Opens chrome browser and launch url "APP_URL"
    
  @Sanity
  Scenario Outline: TC01 Search flights between two cities with lowest possible fares
    When User verifies the "Google Flights - Find Cheap Flight Options & Track Prices"
    And User clicks on trip type
    And User clear data in test box "FromCity"
    And User enters "Chennai, Tamil Nadu, India" data in Auto-Suggestion box "FromCity"
    And User captutres screenprints "<testcase>_From"
    And User enters "Bengaluru, Karnataka, India" data in Auto-Suggestion box "ToCity"
    And User captutres screenprints "<testcase>_To"
    And User enters date in TextBox "DepatureDate"
    And User captutres screenprints "<testcase>_DepatureDate"
    And User clicks on "SearchButton"
    And User scroll down the page
    And User captutres screenprints "<testcase>_Search_Results"
    And User log the details of "LowPrice"
    And User log the details of "Airline_Service"

  Examples:
  |testcase|
  |TC01    |



