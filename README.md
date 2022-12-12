# BreadBOSS
Using Microsoft Access as Database
Capstone Project Making A Database
Code Contributors: Johnson Li-FullStack Developer Role and Cyril Thomas-FrontEnd Developer Role


The BOSS system is a comprehensive software solution intended for bakeries to allow ordering
to be done on a system. The days of paper and pencil or over. The BOSS systems help bakeries
manage their orders, invoices, and customer data. Customers are allowed to access the system

through a login menu and are presented options to view the bakery, create an order, check an
order’s status, cancel an order, and view their order history. The Employees view of the system
allows the employee to view the current offerings of the bakery, views the orders, change the
status of the orders and access to a customer’s contacts to notify the customer for completion or
changes to their order.
The system allows all users to create an account with the system and assigns them a unique
userID. The Create Account methods queries the user for personal data and performs a check if
the user is a customer or employee through an input of an employee access code. Accounts are
created and stored in a vector and updates into the database allowing created accounts to be used
immediately. Unique IDs are created for user creation and order ID’s. The system verifies if the
user already exists and prompts the user for valid inputs.
The system utilizes vectors data structures to manage invoices, customer information and
product offerings and update into the database. The system is integrated with an Access Database
and SQL injections. The searching method searches orders through a unique userID – matching
customer orders to customers. The searching method also searches for “In-Progress” and
“Started Orders” to display existing in progress orders. The searching method utilizes the vector
data structure to iterate through products, user information, and orders.
While placing orders, customers are able to view the bakery – select a product and enter the
quantity they wish to order. The system will capture this data and calculate the total cost for this
customer on their invoice.
Employees are able to check order information and change the status of the order for
transparency as the order gets processed. Customers will be able to check their status and contact
the bakery for any additional concerns
Testing with multiple cases have been completed to assure validity of user inserts and issues.
The BOSS system can later be implemented as a framework for a POS system that can accept
and process payment for a bakery environment. Also plans for a delivery and SMS service are
intended for future updates. The system can be scalable to adapt to future needs of the bakery
business.
