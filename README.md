# hrs-portlets README

## Roles

hrs-portlets maps roles reported from a SOAP web service from HRS to role names local to hrs-portlets. This is to normalize roles and make them look like roles one might expect to use with Spring Security.

Role mapping happens in `hrs-portlets-ps-impl/src/main/resources/app-context/psAppContext.xml`. That configuration is the authoritative documentation of what HRS roles map to what portlet roles, since that configuration is doing the mapping.

## Configuring the portlets as published

The hrs-portlets offer configuration opportunities at the portlet-definition ("publication") layer.

### Applicable to all hrs-portlets

#### `helpUrl` portlet preference

Every HRS integration portlet, individually, supports this portlet preference.
(That is, it can be configured differently for different publications of the portlets).

+ Sets the href of the "Help" link displayed within the portlet.

#### `notification` portlet preference (optional)

Every HRS integration portlet, individually, supports this portlet preference.
(That is, it can be configured differently for different publications of the portlets).

+ For each value of this portlet preference, the configured portlet will render a user-facing message near the top of the portlet content.
+ When not set, this message block disappears from the UI.

This is intended as a solution for

+ Making employees aware of known issues or service degradations (e.g., Benefit Information is omitting some dependents.)
+ Service notices about e.g. upcoming outages (e.g. This app will be unavailable on Tuesday.)
+ Notes about recent changes to the app (e.g. Sabbatical is now termed Banked Leave.)
+ etc.

### Specific to Benefit Information

#### `benefitsSummaryUrl` portlet preference (optional)

+ When set, Benefit Information uses this URL as the href of the "View Benefits Summary Detail" link.
+ When not set, Benefit Information uses the URL from the HRS URLs SOAP web service as the href of the "View Benefits Summary Detail" link.
+ If the URL is available from neither source, Benefit Information drops this link from the UI.

### Specific to ManagerLinks

(Published as "Manager Time and Approval" in MyUW.)

#### `approveAbsenceLabel` portlet preference (optional)

+ Sets the label for the approve absence hyperlink.
+ When not set, the label defaults to "Approve Absence" (as defined in
  `ManagerLinksController.DEFAULT_APPROVE_ABSENCE_LABEL`).

#### `approveTimeLabel` portlet preference (optional)

+ Sets the label for the approve time hyperlink.
+ When not set, the label defaults to "Approve Time" (as defined in
  `ManagerLinksController.DEFAULT_APPROVE_TIME_LABEL`).

#### `approvalsDashboardLabel` portlet preference (optional)

+ Sets the label for the approvals dashboard hyperlink.
+ When not set, the label defaults to "Time/Absence Dashboard" (as defined in
  `ManagerLinksController.DEFAULT_DASHBOARD_LABEL`).

#### `approvalsDashboardUrl` portlet preference (optional)

+ When set, Manager Time and Approval uses this URL as the href for a list-of-links link shown to
  to employees with a particular role.
+ When not set, this link is not shown.

### Specific to Payroll Information

#### `directDepositSelfServiceUrl` portlet preference (optional)

+ When set, configures the href of the "Update your Direct Deposit" link, as experienced by employees with the role required for this access.
+ When not set, all employees will use the hard-coded href linking to a PDF that employees without the role experience.

#### `understandingEarningUrl` portlet preference (optional)

+ When set, configures the href of the "Understanding Your Earnings Statement" link.
+ When not set, the link does not display.

### Specific to Personal Information

While branded as "Personal Information" as published in MyUW,
technically the underlying portlet name is `ContactInfo`.

#### `notice` portlet preference (optional)

+ When set, Personal Information shows the value of this preference as a user-facing message in the context of editing preferred name. This is intended as a spot for documentation nuancing what edits are and are not permitted.
+ When not set, this message does not appear.

#### `updateMyPersonalInfoUrl` portlet preference (optional)

+ When set, uses its value as the href for the link to HRS self-service management of personal information.
+ When set, turns on "PUM22 mode", which tweaks the display a bit. The deep links specific to self-service updating of ethnicity, disability, and veteran status reporting disappear (in favor of the linked UI being the omnibus UI for updating all personal information).

### Specific to Time and Absence

#### `editCancelAbsenceUrl` portlet preference (optional)

+ When set, adds a "Edit/Cancel Absence" link to this URL that shows to employees who see the nearby "Enter Absence" link.
+ When not set, the "Edit/Cancel Absence" link does not appear.

#### `leaveReportingNotice` portlet preference (optional)

+ When set, adds an in-app-message near the top of Time and Absence showing to employees who see the
  Outstanding Missing Leave Reports link.
+ When not set, does not show. For employees who do not see the Outstanding Missing Leave Reports
  link, does not show.
+ Intended for messaging to employees subject to annual leave reporting requirements


#### `dynPunchTimesheetNotification` portlet preference (optional)

+ When set, adds an in-app-message near the top of Time and Absence showing to
  employees with `ROLE_UW_DYN_AM_PUNCH_TIME`.
+ When not set, does not show. For employees who do not have
  `ROLE_UW_DYN_AM_PUNCH_TIME`, does not show.
+ Intended for messaging to employees for whom the UI changed with the launch of
  PHIT, such that the functions they previously accessed via purpose-specific
  absence-related buttons they will now access via the timesheet button.

#### `nonDynPunchTimesheetNotification` portlet preference (optional)

+ When set, adds an in-app-message near the top of Time and Absence showing to
  employees who see the Timesheet button but who do not have
  `ROLE_UW_DYN_AM_PUNCH_TIME`.
+ When not set, does not show.
+ For employees who do have `ROLE_UW_DYN_AM_PUNCH_TIME`, does not show.
+ For employees who do not see the Timesheet button, does not show.
+ Intended for messaging to employees for whom the MyUW UI did not change with
  the launch of PHIT, but for whom the experience they get when they click the
  Timesheet button may or may not have changed.

#### `dynPunchTimesheetNotice` portlet preference (optional)

+ When set, adds text to the UI near the timesheet launch button, shown only
  to employees with `ROLE_UW_DYN_AM_PUNCH_TIME`.
+ When not set, does not show.
+ For employees without `ROLE_UW_DYN_AM_PUNCH_TIME`, does not show.
+ When both this preference and `timesheetNotice` are set, this text appears
  before and separated with a horizontal rule from the text specified in
  `timesheetNotice`.

Intended for easing the transition for employees who lose the absence-specific
buttons and instead will now access absence-related functions directly in the
HRS self-service timesheet linked via the Timesheet button.

#### `timesheetNotice` portlet preference (optional)

+ When set, adds text to the UI near the timesheet launch button.
+ When not set, omits this text.

Intended for adding a reminder to employees who by policy are not to work so
many hours that they must be offered health insurance. (This text shows to all)
employees with a role to see the timesheet launch button, regardless of whether
this hour limiting policy applies to them.)

#### `UnclassifiedLeaveReportForSummerUrl` portlet preference (optional)

(Note the PascalCase rather than camelCase capitalization.)

+ Configures the href of the "Unclassified Summer Session/Service Leave Report" link.
+ When not set, this link does not appear.

#### `UnclassifiedLeaveReportUrl` portlet preference (REQUIRED)

(Note the PascalCase rather than camelCase capitalization.)

+ Configures the href of the "Unclassified Leave Report" link.
+ If not set, this link will still appear but will be broken.

## JSON resource URLs

(Not a comprehensive listing.)

### Manager Links

+ `managerListOfLinks` : JSON suitable for driving a `list-of-links` widget representing links
  appropriate to the employee's roles for approving others' time and absences.

### Roles

+ `rolesAsListOfLinks` : JSON suitable for driving a `list-of-links` widget representing the user's
  HRS roles. Useful for troubleshooting.

### Urls

+ `hrsUrlsAsListOfLinks` : JSON suitable for driving a `list-of-links` widget representing the HRS
  URLs. Useful for troubleshooting.

## Redirector

The servlet path `/go` takes a request parameter `urlKey`. When this is the key to a URL known to
HrsUrlsDao, this path redirects the user to that HRS URL. When not known, responds 404 NOT FOUND.
This is a handy way to statically refer in widgets, notifications, etc., to the HRS URLs while
getting the tier-appropriate, potentially changing over time implementation of that URL, rather than
peppering implementation details of HRS deep links into content.

## Local Setup Instructions

Several property files need to be configured for your local environment before the Portlet will run in your local uPortal server.

* /hrs-portlets-webapp/src/main/resources/logback.xml
  * Update paths in file to where HRS portlet should write it's log files to.
* /hrs-portlets-bnsemail-impl/src/mail/resources/
  * Copy EXAMPLE_bnsemail-placeholder.properties to **bnsemail-placeholder.properties**
  * Copy **smime-keystore.jks** into directory
  * Do Not add bnsemail-placeholder.properties OR smime-keystore.jks to source control (should be automatically ignored via local .gitignore)
* /hrs-portlets-cypress-impl/src/mail/resources/
  * Copy EXAMPLE_cypress-placeholder.properties to **cypress-placeholder.properties**
  * Do Not add cypress-placeholder.properties to source control (should be automatically ignored via local .gitignore)
* /hrs-portlets-ps-impl/src/mail/resources/
  * Copy EXAMPLE_ps-placeholder.properties to **ps-placeholder.properties**
  * Do Not add ps-placeholder.properties to source control (should be automatically ignored via local .gitignore)
