



1.Use case description

UC ID and Name:
01 - Register Recruiter Account .
Created By:
Nguyễn Quang Quyền 
Date Created:
May 2026 
Primary Actor:
Recruiter 
Secondary Actors:
Email  Service
Trigger:
The Recruiter selects the Register option and submits the registration form. 
Description:
Allows a Recruiter to create a recruiter account by providing account information, personal information, and company information. 
Preconditions:
The Recruiter is not logged in.
The Recruiter does not already have an account associated with the provided email.
Postconditions:
Success:
Recruiter account is created.
Account status = Email Verification Pending.
Verification email is sent.
 Failure:
No account is created.
Normal Flow:
Recruiter opens the registration page.
System displays registration form.
Recruiter enters:
Email
Password
Confirm Password
Full Name
Gender
Phone Number
Company Name
Work Location
Recruiter accepts Terms of Service and Privacy Policy.
Recruiter clicks Register.
System validates input data.
System checks email uniqueness.
System hashes passwords.
System creates a recruiter account.
System generates email verification token.
System sends verification email.
System displays registration successful message.


Alternative Flows:
AF-01 Register with Google
At Step 3, the Recruiter selects Register with Google.
The System redirects the Recruiter to Google Authentication.
The Recruiter grants authorization.
The System retrieves the user's basic profile information.
The System creates the recruiter account.
The System marks the email as verified.
The System displays the registration success message.
AF-02: Optional Marketing Consent Not Selected
At Step 6, the Recruiter declines receiving recruitment consultation and promotional information.
The System continues the registration process.
The account is created successfully.
The Recruiter will not receive marketing communications.


Exceptions:
EF-01: Email Already Exists
At Step 9, the System detects that the email is already registered.
The System displays: "This email address is already associated with an existing account."
The Recruiter remains on the registration page.
EF-02: Invalid Email Format
At Step 8, the System detects an invalid email format.
The System displays an error message.
The Recruiter corrects the email address.
EF-03: Password and Confirm Password Do Not Match
At Step 8, the System detects that the passwords do not match.
The System displays an error message.
The Recruiter re-enters the passwords.
EF-04: Required Information Missing
At Step 8, the System detects one or more mandatory fields are empty.
The System highlights the missing fields.
The Recruiter provides the required information.
EF-05: Terms and Privacy Policy Not Accepted
At Step 6, the Recruiter does not agree to the Terms of Service and Privacy Policy.
The System prevents registration.
The System displays:

 "You must accept the Terms of Service and Privacy Policy to continue."
EF-06: Email Delivery Failure
At Step 13, the email service fails to send the verification email.
The System logs the error.
The Recruiter account is still created.
The Recruiter may request a new verification email later.


Priority:
High 
Frequency of Use:
High  
Business Rules:
BR-01,BR-02,BR-03,BR-10
Other Information:
Registration supports both Email/Password and Google OAuth.
Email verification links expire after a configurable period (e.g., 24 hours).
Audit logs should record account creation activities.
Assumptions:
Email service is available and operational.
Password hashing is handled by the backend service.
Frontend validation is implemented using React.
Backend APIs are implemented using Java Spring Boot.


2. UC-02:  Verify Email  Recruiter

UC ID and Name:
02 - Verify Email  Recruiter
Created By:
Nguyễn Quang Quyền
Date Created:
May 2026 
Primary Actor:
Recruiter 
Secondary Actors:
Email Service
Trigger:
Recruiter clicks the verification link in the email. 
Description:
Allows a Recruiter to verify ownership of the registered email address. 
Preconditions:
Recruiter account exists.
Account status = Email Verification Pending.
Verification token is valid.
Postconditions:
Success
Email is verified.
Account status = Active.
Recruiter can access the system.
Failure
Email remains unverified. 
Normal Flow:
Recruiter receives verification email.
Recruiter opens email.
Recruiter clicks verification link.
System validates verification token.
System activates account.
System updates email status to Verified.
The system displays verification successful message.
Alternative Flows:
None


Exceptions:
EF-01 Invalid Token
The system detects invalid tokens.
Verification is rejected.
EF-02 Expired Token
System detects expired token.
System requests token regeneration.
EF-03 Email Already Verified
System detects email already verified.
System informs the user.
Priority:
High 
Frequency of Use:
High 
Business Rules:
BR-02
Other Information:
None
Assumptions:
 None.


6. UC-06: Login Recruiter  

UC ID and Name:
06 - Login Recruiter   
Created By:
Nguyễn Quang Quyền 
Date Created:
May 2026 
Primary Actor:
Recruiter
Secondary Actors:
None  
Trigger:
The Recruiter enters login credentials and clicks the Login button. 
Description:
Allows a recruiter to authenticate using valid credentials and gain access to authorized system functions.  
Preconditions:
The Recruiter has a registered account.
The Recruiter account is active and not locked.
The Recruiter has verified their email address.
Postconditions:
Success:
The Recruiter is authenticated.
A user session is created.
The Recruiter is redirected to the dashboard .
Failure:
The Recruiter remains on the login page.
No session is created.
Normal Flow:
The Recruiter accesses the login page.
The System displays the login form.
The Recruiter enters an email address and password.
The Recruiter clicks the Login button.
The System validates the entered credentials.
The System authenticates the Recruiter.
The System creates a user session.
The System redirects the Recruiter to the dashboard.
Alternative Flows:
A1. Login with Google
At Step 3, the Recruiter selects Login with Google.
The System redirects the Recruiter to Google Authentication.
The Recruiter grants authorization.
The System authenticates the Recruiter using Google account information.
The System creates a user session and redirects the Recruiter to the dashboard. 
Exceptions:
E1. Invalid Credentials
At Step 5, the System detects an incorrect email or password.
The System displays an error message.
The Recruiter remains on the login page.
E2. Unverified Email
At Step 6, the System detects that the email address has not been verified.
The System denies access and prompts the Recruiter to verify the email address.
E3. Account Locked
At Step 6, the System detects that the account is locked or suspended.
The System denies access and displays an appropriate notification.
E4. System Error
At Step 6, an unexpected system error occurs.
The System displays an error message and logs the incident. 
Priority:
High 
Frequency of Use:
High  
Business Rules:
BR-01.2,BR-10.1,BR-10.2 
Other Information:
The system supports both Email/Password authentication and Google OAuth login.
Login activities should be recorded for security auditing. 
Assumptions:
Authentication services are available.
The Recruiter has a stable internet connection.
Security policies are configured and enforced by the system.


BR

ID
Business Rule
Business Rule Description
BR-01 
Email  Verification 
Email addresses must be unique across all user accounts (including candidates, recruiters, and admins). Only verified accounts may log in.  
Only verified recruiter accounts may log in. 
BR-02 
Token
Verification token password expires after 24 hours.
Each password reset token must be unique. 
A password reset token can only be used once. 
Email verification tokens expire after 15 minutes. 
Password reset tokens are unique, single-use, and expire after 24 hours. 
BR-03 
Single Application Limit 
A Candidate can only submit an application to a specific job posting once to prevent system spam. 
BR-04 
Data Privacy for Applications 
Only recruiters belonging to the company that posted the job can view and manage its applications. 
BR-05 
Notification Delivery 
Notifications must be sent in real-time or near real-time. 
BR-06 
Moderation Processing Time 
All verifications must be processed within 24 hours of submission. 
BR-07 
Administrative Privileges 
Only Admins can modify platform-wide settings or view global statistical reports. 
BR-08 
CV Storage Limit 
A Candidate can store a maximum of 5 different CVs in their profile. 
BR-09 
CV File Constraints  
CV uploads must not exceed 5MB and must be in PDF format. Invalid files are rejected with an error message. Avatar uploads must not exceed 2MB and must be in JPG/PNG/WEBP formats. 
BR-10 
Password Security 
All user passwords must be hashed and salted before saving to the database. Storing plain-text passwords is prohibited. 
Multiple failed login attempts may trigger account lockout according to security policies. 
Password must satisfy the system password policy:
Minimum 8 characters.
At least one uppercase letter.
At least one lowercase letter.
At least one numeric character.
The admin cannot perform the forgotten password function.


BR-11
Company Verification
Only verified accounts may continue to company verification.
company can have only one active verification request at a time.
Recruiters cannot access recruiter-specific functions until their email is verified. 
Only verification requests with status Rejected or Revision Required may be resubmitted verification information of company. 
The following fields are classified as Critical Verification Information:
Company Name
Business License
Tax Code
Company Verification Documents
Changes to Critical Verification Information require moderator approval before becoming effective. 
When a Critical Verification Information update request is submitted, the company status is changed to Updating. 
While the company status is Updating:
Existing job postings remain active.
Recruiters may continue using the system.
Candidates may continue applying for jobs.
The following fields are classified as Non-Critical Information and are updated immediately:
Company Description
Company Website
Company Address
Company Size
Company Industry
Contact Information
Company Logo
Only Moderators may approve or reject Critical Verification Information updates. 
BR-12
Job Status
Recruiters whose company verification status is Pending or Rejected may only access company verification functions. 
Only Moderators and Admin can approve or reject updated verification job requests.
Each job posting has two independent status attributes:
Job Status:Draft, Published, Unpublished, Closed, Expired
Job Validation Status:Pendding, Approved, Rejected ,Updated
Every newly created job posting shall be assigned:
 Job Status = Draft
Job Validation Status = Pending 
A company whose verification status is Updating may continue using all job management functions. 
A job posting may be published only when:
Job Validation Status = Approved
A Published job posting may be changed to:
Unpublished
Closed 
Only Published job postings may be changed to Unpublished. 
Only Published job postings may be changed to Closed. 
The Expired status shall be assigned automatically by the system when the application deadline is reached. Users cannot manually assign the Expired status.
When a published job posting is modified, the current published version shall remain visible to candidates until the updated version is approved. 
Editing an approved and published job posting shall change Job Validation job Status to Updated. 
If the updated version is approved, the system shall replace the currently published version with the approved version. 
If the updated version is rejected, the currently published version shall remain active and unchanged.   
BR-13
User Account Administration
Only Admin can view, search, filter, and manage platform user accounts.
 A user account status changes only along valid transitions: Active to Inactive (deactivate); Inactive or Banned to Active (activate); Active or Inactive to Banned (ban).
 Deactivate (set to Inactive) is a temporary, reversible measure used for administrative reasons such as a user request to pause the account or a temporary suspension of activity.
 Ban (set to Banned) is applied only when the user violates platform policies (for example: spam, fraudulent job postings, fake account, or repeated abuse).
 Every deactivate or ban action must record a reason for audit purposes.
 An Admin cannot deactivate or ban their own account.
 A banned account cannot log in until an Admin removes the ban.
BR-14
Job Category Management
Only Admin can create, view, update, and delete job categories.
 A job category name must be unique.
 A job category cannot be deleted while it is still linked to active job postings.
BR-15
Admin Audit Logging
All Admin actions (user account changes and job category changes) must be recorded in an audit log with actor, timestamp, target, and action for security traceability.
BR-16 
Job Search Visibility 
Candidates and unauthenticated Guests can only browse, search, and view job postings that have Job Status = Published and Job Validation Status = Approved. 
BR-17 
Application Withdrawal Constraints 
Candidates can only withdraw an active application if its status has not reached a final decision state (Hired or Rejected). Withdrawn applications are permanent and cannot be reactivated. 
