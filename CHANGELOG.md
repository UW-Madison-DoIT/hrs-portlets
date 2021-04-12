# MyUW hrs-portlets change log

## HRS Portlets 9 series

The v9 major version was occasioned by the breaking change of the
new, required `fname` `portlet-preference` in Benefit Information Portlet.

## Next

Putatively 9.2.1, thefore currently building 9.2.1-SNAPSHOT.

+ Breaking change:
  Removes undocumented,
  not-working-as-expected "tab" parameter feature from Benefit Information.
+ In Benefit Information,
  now supports portlet request parameter `requestedContent`,
  as in Payroll Information. Values "ETF WRS Statements of Benefits" and
  "Benefit Enrollment Confirmation Statements" select the tabs of those names.

### 9.2.0

2021-03-19

+ In Personal Information,
  employees lacking the HRS role `UW_DYN_PY_DIRDEP_SS`
  see descriptive text accurately reflecting
  the smaller set of things they can adjust on a self-service basis
  and the larger set of things they will need to contact their HR office to update.
  (By policy, users lacking multifactor authentication are ineligible for self-service contact updating).
  Employees with the role will continue to
  see descriptive text accurately reflecting
  the larger set of things they can adjust on a self-service basis
  and the smaller set of things they will need to contact their HR office to update.

### 9.1.2

2021-02-16

+ fix: in Payroll Information list-of-links widget,
  for users without the `UW_EMPLOYEE_ACTIVE` HRS role,
  include the earnings statements link as a link to KB doc
  instead of omitting the link entirely.

### 9.1.1

2021-02-01

+ In Payroll Information list-of-links widget,
  for users without the HRS role `UW_EMPLOYEE_ACTIVE`,
  omits the direct deposit and withholdings links and
  changes the earnings statements and tax statements links
  to link to a KB article rather than into the Portlet or into HRS self-service.

### 9.1.0

2021-01-12

+ In Payroll Information,
  for users without the HRS role `UW_EMPLOYEE_ACTIVE`,
  replaces the "View W-2 forms" button
  with a button that links to [KB article](https://kb.wisc.edu/helpdesk/page.php?id=90392)
  explaining how to request documents.
  Users without the `UW_EMPLOYEE_ACTIVE` role
  cannot use the regular version of that button
  because such users lack access to HRS employee self-service.
+ Links to the Preferred Name portlet for preferred name editing,
  rather than duplicating that UI inline in Personal Information.
+ Copy edit for active voice and brevity:
  uses "Update your preferred name in HRS using the 'Update My Personal Information' link below."
  instead of "Your preferred name in HRS will haver to be updated separately using the 'Update My Personal Information' link below."

### 9.0.1

2020-09-10

+ Configures for Annual Benefits Enrollment period in 2020,
  enrolling in 2021 benefits.

### 9.0.0

2020-08-02

+ Separates Benefit Enrollment Confirmation Statements
  and ETF Statements of Benefit into separate tabs.
+ Switches from Fluid Infusion JavaScript-enhanced paged table
  to plain old HTML tables generated server-side for these statements
+ BREAKING CHANGE: *requires* that Benefit Information portlet-definitions
  declare a portlet-preference `fname` with their own `fname` as value.
  This is so that the Benefit Information portlet can use its own fname in generating resource URLs linking to those statements.

## HRS Portlets 8 series

The v8 major version was occasioned by the breaking change of the `updateMyPersonalInfoUrl`
portlet-preference no longer having any effect, and thereby no longer supporting unused-since-2017
before-PUM22-mode presentation of the Personal Information portlet.

### 8.0.0

2020-04-16

+ Adds "Preferred Name" to the employee-facing text description of what an
  employee can adjust in HRS self-service. ( [HRSPLT-468][] )
+ Personal Information no longer supports unused pre-PUM-22 mode.
+ `updateMyPersonalInfoUrl` portlet-preference no longer has any effect.
  Instead, Personal Information now links to the HRS URL named
  `Personal Information` rather than indulging in a layer of indirection via
  that portlet-preference.

## HRS Portlets 7 series

The v7 major version was occasioned by the breaking change of no longer honoring
`enrollmentFlag`, instead relying upon HRS roles to indicate whether and what
benefit enrollment opportunities are available to an employee.

### 7.4.1

2020-04-10

+ Add user-facing text to Personal Information acknowledging that Madison
  employees have to take additional step to adjust local-to-HRS preferred name
  in addition to configuration Madison campus preferred name. ( [HRSPLT-467][] )

### 7.4.0

2020-03-30

+ Include `eduWisconsinETFMemberID` header on benefit statement requests to
  Cypress, with the user attribute of the same name as its value.

### 7.3.0

2020-03-02

+ Uses new `ESS W-4` HRS URL when present. ( [HRSPLT-466][] )

### 7.2.5

2020-01-10

+ Update W-2 and 1095-C hyperlinks in Payroll Information to reflect that these
  no longer will link specifically to 2018 documents.
  ( [HRSPLT-461][], [#216][] )
+ Hide old tax statements by default, with a checkbox for showing the old
  statements. This is intended to help users focus on the more recent statements
  that more likely address their current needs while continuing to make access
  to old statements possible. ( [HRSPLT-463][], [#217][] )

### 7.2.4

2020-01-03

+ Style "Web Clock" link as a button for consistency with other
  links-that-look-like-buttons in Time and Absence. ( [HRSPLT-460][] )

### 7.2.3

2019-10-21

+ Implement after-annual-benefits-enrollment-closes feedback mode for
  Benefit Information Widget

### 7.2.2

2019-10-21

+ Use `Time and Absence Edit/Cancel` HRS URL when set; continue to honor
  `editCancelAbsenceUrl` portlet-preference when HRS URL not set.
+ Predicate "Approve Time" link in "Manager Time and Approval" app on presence
  of `Approve Payable time` (sic) HRS URL.
+ Remove logging as error case where `Approve Payable time` (sic) HRS URL is not
  present, as removal of this HRS URL is expected in a forthcoming PeopleSoft
  Update Manager update ("PUM").
+ Differentiated default labels for approvals dashboard link in Manager Time and
  Approval widget vs app, with the label in the app changing to append
  " (Approve Time)".
+ Always label the button "View/Update Dependent Information",
  regardless of whether it is linking to
  `Dependent/Beneficiary Info` or to
  `Dependent Information`. ( [HRSPLT-459][])
+ deprecated `approvalsDashboardLabel` portlet preference

### 7.2.1

2019-09-23

+ Implement the during-Annual-Benefits-Enrollment (ABE)
  countdown-days-to-end-of-ABE widget.
+ Tweak last-day-of-ABE widget to open link to HRS self-service in new window.

### 7.2.0

2019-09-16

+ Foreshadow 2019 annual benefits enrollment in Benefit Information widget
+ Prefer to source manager "Time/Absence Dashboard" URL from new HRS URLs
  service key `Time/Absence Dashboard`, falling back on now-deprecated portlet
  preference for this URL. [HRSPLT-454][]
+ Remove "View Time Entry Exceptions" link. [HRSPLT-453][]
+ Add `enrollmentRole` JSON controller ( [MUMMNG-4833][] [#205] )

#### Deprecated in 7.2.0

+ Time and Absence portlet preference `approvalsDashboardUrl` is deprecated.
  Provision the time and absence approvals dashboard URL via new HRS URLs DAO
  key `Time/Absence Dashboard`.

### 7.1.3 Really fix Benefit Information widget "Learn more" link

2019-06-20

Fixes a bug in 7.1.2 that referenced `learnMoreLink` by the wrong name and so
did not include the learn more link in the widget.

### 7.1.2 Fix Benefit Information widget learn more link

2019-06-20

+ fix: In Benefit Information widget and app, use `aria-label` to mitigate the
  screenreader usability issues of hyperlinks with bare "Learn more" labels.
  `aria-label`ing the links enables users jumping to those hyperlinks in a
  screen reader to more readily discover what topic they might learn more about
  by following the link. (  [#202][] )
+ fix: In Benefit Information widget, for Madison employees, use the most
  relevant of 3 URLs as the "Learn more" link rather than always using the URL
  specifically about *annual* benefit enrollment. ( [#201][] )

### 7.1.1 Fix Benefit Information widget enroll link

2019-06-17

+ fix: In `benefitInformationWidget`, inline the markup that `<md-button>` would
  have generated, since AngularJS directive `<md-button>` does not work as
  sourced in a `remote-content` type widget. ( [#200][] )

### 7.1.0 Generate Benefit Information widget server-side

2019-06-07

+ feat: add `benefitInformationWidget` resource URL that provides markup
  suitable for use in new `remote-content` type widget
  ( [HRSPLT-434][], [#198][] )
+ fix: computes Madison-ness based on `wiscEduHRSEmplid` rather than on
  `wiscEduSORName`. While looking for SORName worked in prod (but not in
  non-prod), basing this on `wiscEduHRSEmplid` bases it on something closer to
  the core of and otherwise depended upon by hrs-portlets.

### 7.0.0 Enrollment opportunities via roles rather than enrollmentFlag

2019-05-31

+ feat: reflect new HRS roles indicating enrollment opportunities, with
  tightened up UI messaging supported by contextual "Learn more" links.
  ( [HRSPLT-436][], [#194][] )
+ fix: removes bugged no-op code in Time and Absence. It was bugged in a few
  ways. The controlling bug is that it was predicated on an HRS URL key that
  MyUW is not receiving from HRS, and so was a no-op. This release removes the
  bugged code so no one has to worry going forward about whether it is (wrongly)
  displaying to employees. ( [#197][] )

## HRS Portlets 6 series

The v6 major version was occasioned by introducing dependency on a new
publication of Payroll Information as fname `earnings-statement-for-all`.

### 6.4.1 Tweak help-link style

2019-05-28

+ fix: line up the launch icon at the end of the help link with the help link
  label text ( [HRSPLT-449][] )

### 6.4.0 Add Performance list-of-links

2019-05-28

+ feat: add Performance Portlet vending `performanceListOfLinks` resource URL.
  ( [HRSPLT-437][] )

### 6.3.0 Increase help link affordance

2019-05-03

+ feat: improve affordance of upper-right help hyperlinks. Implementation
  introduces a `helpLink` JSP tag. ( [HRSPLT-405][] )

### 6.2.1 Mitigate former employee loss of HRS self-service access

2019-03-15

+ fix: change message `label.yearEndLeaveBalance` and its default value to
  "University Staff end of year leave balance" ( [HRSPLT-418][], [#179][] )
+ fix: in Personal Information, predicate the edit hyperlink (into HRS
  self-service) on `ROLE_UW_EMPLOYEE_ACTIVE` ( [HRSPLT-424][], [#181][] )
+ fix: in Payroll Information and in Personal Information, show message to users
  without `ROLE_UW_EMPLOYEE_ACTIVE` mitigating their lack of access to HRS
  self-service content and functions (W-2s and earnings statements issued in
  2019; self-service home mailing address updating).
  ( [HRSPLT-425][], [#182][], [HRSPLT-429][], [#185][] )
+ fix: in Benefit Information, mitigate the case where an employee has an HRS
  emplid and so has access to Benefit Information, but no longer has roles in
  HRS, and so the links to HRS self-service will not work for the employee.
  ( [HRSPLT-423][], [#180][] )
+ fix: in Payroll Information, characterize 2019 as the present rather than as
  the future in the microcopy about 2019 and beyond earnings statements not
  including leave balances. ( [HRSPLT-426][], [#183][] )

### 6.2.0 Absence tab for all

2019-01-29

+ feat: change `psAppContext.xml` role mapping config to grant MyUW HRS Portlets
  role `ROLE_VIEW_ABSENCE_HISTORIES` to employees with HRS role
  `UW_EMPLOYEE_ACTIVE`, to the effect that all employees will see the "Absence"
  tab in "Time and Absence". ( [HRSPLT-415][], [#178][] )

### 6.1.0 Payroll Information dynamic list-of-links

2019-01-14

+ feat: add Payroll Information list-of-links resource URL (path: `listOfLinks`)
  that honors `ROLE_VIEW_DIRECT_DEPOSIT` in the same way as the Payroll
  Information portlet content honors that role (linking employee self-service
  Direct Deposit iff the employee has that role and the `Direct Deposit` HRS URL
  is set, otherwise linking the PDF form for adjusting direct deposit.) (
  [HRSPLT-369][], [#176][] )

+ fix: clarify label of link to HRS-vended W-2s, that these are *2018* W-2s. (
  [HRSPLT-411][], [#174][] )
+ fix: clarify label of link to HRS-vended 1095-Cs, that these are *2018*
  1095-Cs. ( [HRSPLT-412][], [#175][] )
+ fix: log at DEBUG level when encountering error sending-and-receiving a SOAP
  message. At TRACE level log the raw response. Special handling when the error
  is a SOAP fault and hrs-portlets understands that it's a SOAP fault. (
  [HRSPLT-408][], [#172][], [#173][] )

### 6.0.5 avoid popups in surplus earnings statement hyperlinks

2018-12-10

+ fix: open surplus earnings statements like opening the first ten earnings
  statements, not with JavaScript that'll trigger pop-up blockers.
+ style: obsessively 2-space-indent `payrollInformation.jsp` in the hope of
  sooner detecting future bugs like [HRSPLT-404][].

### 6.0.4 fix tax statements tab

2018-12-07

+ Fix div matching bug that under some circumstances would break the "Tax
  Statements" tab in Payroll Information ( [HRSPLT-404][], [#169][] )

### 6.0.3 fix troubleshooter

2018-12-06

+ Fix earnings statement service configuration in the Troubleshooter portlet.

### 6.0.2 fix show all toggle and predicate HRS earnings statements on URL

2018-12-04

+ Only query for HRS earnings statements when the HRS URL "Earning Statement"
  (sic) is set. This allows promoting a hrs-portlets version that supports HRS
  earnings statements in advance of HRS supporting earnings statements, without
  showing errors to employees (that is, differentiating the
  HRS-earnings-statements-should-be-working-but-are-broken case from the
  HRS-earnings-statements-are-not-yet-expected-to-be-working case). (
  [HRSPLT-402][], [#167][] )
+ Fix reference to the show all earnings statements toggle ( [HRSPLT-399][],
  [#166][] )
+ Add UI control to reload page in case where earnings statements table failed
  ( [HRSPLT-403][] , [#168][] )

### 6.0.1 fix earnings statements sort

2018-11-30

+ Correct earnings statements sort to reverse chronological order rather than
  chronological order, so that most recent appear by default in and at top of
  table.

### 6.0.0 Cypress earnings statements back in the table

2018-11-30

Breaking change in 6.0.0

+ Introduces dependency on `earnings-statement-for-all` as a
  works-for-both-Madison-and-System-employees `fname` for a Payroll Information
  that can render Cypress earnings statement PDFs. This makes feasible the
  converter from the old Cypress statement model to the new universal statement
  model (which includes the URL of the statement as part of the domain model).

Features in 6.0.0:

+ Include legacy Cypress earnings statements in new Payroll Information
  earnings statements table.
+ Limit initial earnings statement table render to at most 10 earnings
  statements. Add a toggle control, modeled on the existing toggle for showing
  earnings amounts, to show the truncated table rows. ( [HRSPLT-399][],
  [#161][], [#162][] )
+ Support the case where one or the other source of earnings statements (HRS,
  Cypress) succeeds and the other source fails, both acknowledging the error and
  listing those statements that MyUW did successfully retrieve.

Fixes in 6.0.0:

+ Fix Time and Absence to correctly render in cases where it omits the Leave
  Balances tab. Had been broken since 5.5.0. ( [HRSPLT-400][], [#163][] )
+ Fix Payroll Information earnings statements table to stop trying to open
  earnings statements via `javascript:window.open({url})` and instead use a more
  typical `<a href="{url}" target="_blank" rel="noopener noreferrer">`
  ( [HRSPLT-398][], [#160][])
+ Fix URLs troubleshooter to sort the URLs alphabetically by URL key. This makes
  the tool more usable for confidently checking whether a particular key is
  present. ( [HRSPLT-401][], [#164][])

## HRS Portlets 5 series

The HRS Portlets 5 major version was occasioned by the breaking change of
removing access to the direct deposit and W4 update forms, and with these the
`ROLE_VIEW_DIRECT_DEPOSIT` role and the `directDepositSelfServiceUrl`
portlet-preference. (There's no harm in continuing to set this preference; it
just no longer has any effect.)

However, in HRS Portlets 5.2 the links to the W4 update form and the direct
deposit intructions form were restored to service, with support for
`ROLE_VIEW_DIRECT_DEPOSIT` and the `Direct Deposit` URL from the HRS URLs
DAO. (Each of these links (tax withholdings, direct deposit) only appears on a
single tab within Payroll Information as of 5.2.) Support for
`directDepositSelfServiceUrl` `portlet-preference` was *not* restored; the
direct deposit button either uses its hard coded URL linking to the PDF form or
it sources its URL from the HRS URLs web service.

### 5.8.5 fix troubleshooter

2018-11-09

+ Fix troubleshooter.jsp to reflect changes to names of JavaBean properties on
  SimpleEarningsStatement

### 5.8.4 fix JSP syntax error

2018-11-09

Fix:

+ Close a c:choose tag

### 5.8.3 Open PeopleSoft earnings statements using window.open

2018-11-09

Fix:

+ The PeopleSoft PDF code opens the PDF file in a new tab. So the PeopleSoft page has some
  JavaScript on it that tries to close itself so that the user does not have 2 tabs open for each
  earnings statement link they click. However, that JavaScript is only able to close the tab if the
  tab was opened via JavaScript. So, open PeopleSoft earnings statement PDF links via
  `javascript:window.open('https://...?pacheck_nbr=12456)`

### 5.8.2 Earnings Statements UI fixes

2018-11-09

Fixes:

+ Remove stray angle brace in Payroll Information UI.
+ Reflect error and empty earnings statements cases in Payroll Information UI.
+ Predicate checkbox about showing earnings amounts on there being earnings amounts to show.
+ Add hint about emplids to demonstrate earnings statements in QA.

### 5.8.1 include `earningsStatements` in model for `payrollInformation.jsp`

2018-11-09

Fix:

+ 5.8.1 `payrollInformation` JSP expected a model attribute `earningsStatements` but the controller
  did not include that attribute in the mode. This fixes the controller to include the expected
  data.

### 5.8.0 Generate earnings statements table server-side rather than client-side

2018-11-09

Changed:

+ Radically changed earnings statements table implementation to render server-side rather than
  client-side and thereby get out from under Fluid and DataList complexity. There is JSON available
  for this data so a future modern-libraries client-side UI is possible. In the meantime, moving
  this table generation server-side eases current changes to support HRS earnings statements and
  their per-statement URLs.
+ Changed `SimpleEarningsStatement` property names to match those in legacy
  Cypress-specific `EarningStatement` and so to require less change in the front
  end to integrate with the new JSON.

### 5.7.1 Fix EarningStatementDataController

2018-11-09

Fix:

+ The new resource URL introduced in 5.7.0 was broken because the JavaBean setter for injecting the
  dependency on the new earnings statement DAO was broken. This fixes it.

### 5.7.0 JSON resource URL for new HRS-sourced earnings statements

2018-11-09

New feature:

+ Adds JSON resource URL exposing the new HRS-sourced earnings statements.

Fix:

+ Added audit logging to HRS integration troubleshooter. ( [HRSPLT-394][] )

### 5.6.0 HRS Earnings Statements in Troubleshooter

2018-11-08

New feature:

+ Implements getting earnings statements from HRS, but just Troubleshooter-deep.

Fixes:

+ Consistently refer to earnings statements as "earnings" statements, not
  "earning" statements. ( [HRSPLT-391][] )
+ Sort roles in troubleshooter ( [HRSPLT-390][] )
+ Remove `@version` annotations in code that had been populated with stale
  metadata from the previous, no-longer-in-use versioning system (SVN). (
  [#155][] )

### 5.5.0 - ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL, timeAndAbsenceFName

2018-11-06

#### New features in 5.5.0

+ Add supporting text in Payroll Information cueing visiting Time and Absence to
  view leave balances, since leave balances will no longer be available on
  earnings statements starting in 2019. If the optional `portlet-preference`
  `timeAndAbsenceFName` is set, this supporting text includes a hyperlink easing
  employee navigation to Time and Absence. ( [HRSPLT-386][], [#153][] )
+ Predicate the preference for the link to `Classic ESS Abs Bal` introduced in
  hrs-portlets 5.3.0 upon the employee holding a newly invented hrs-portlets
  role `ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL`. If the employee does not hold this
  role, show the status quo leave balances table.
  This is moot for employees also holding `ROLE_REDIRECT_TO_HRS_FLUID_TIME`
  iff the `Fluid Time` HRS URL is set, since they'll be redirected to HRS
  self-service `Fluid Time` anyway. ( [HRSPLT-384][], [#152][] )
+ New button "View Leave Balances" in Time and Absence, implementing that link
  to `Classic ESS Abs Bal`, and predicated on that URL being defined. This
  replaces the button and supporting text that had been included in the Leave
  Balances tab when `Classic ESS Abs Bal` was defined, as of 5.3.0. (
  [HRSPLT-384][], [#152][] )
+ Map from HRS role `UW_DYN_AM_Employee` to newly invented hrs-portlets role
  `ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL`. That is, the HRS role `UW_DYN_AM_EMPLOYEE`
  is required to see the new link to `Classic ESS Abs Bal`. Employees lacking
  this role continue to see leave balances as of their most recent earnings
  statement directly included in the "Leave Balances" tab in "Time and Absence".
  ( [HRSPLT-384][], [#152][] )
+ Adjusted supporting text on Leave Balances tab to no longer set expectation
  as to whether earnings statements include leave balances, while continuting to
  clarify as of when the leave balances shown in the table are current.
  Enhanced with optional link to the corresponding Payroll Information iff a new
  `portlet-preference` `payrollInformationFName` is set. ( [HRSPLT-384][],
  [#152][] )

#### Removed features in 5.5.0

+ Employees with new `ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL` will no longer see the
  Leave Balances tab in Time and Absence, so long as that role was actualized by
  the URL for that link being set so the button rendered. ( [HRSPLT-384][],
  [#152][] )
+ The portlet-preference `dynamicLeaveBalancesLearnMoreUrl` introduced in 5.3.0
  to optionally define a supporting link for learning more about dynamic leave
  balances no longer has any effect, since the explanatory text it had been
  supporting has been removed from the UI. ( [HRSPLT-384][], [#152][] )

### 5.4.0 - redirect UW_DYN_AM_ESS_FLU_MONTHLY to Fluid Time

2018-11-02

+ Redirect employees with the new `ROLE_REDIRECT_TO_HRS_FLUID_TIME` MyUW
  hrs-portlets role from MyUW Time and Absence to the new HRS self-service
  `Fluid Time` page. That role signifies employees whose time and absence
  reporting needs are fully met by the new `Fluid Time` UI and so for whom the
  best available experience is to go directly to HRS `Fluid Time`. (
  [HRSPLT-381][] , [#150][] )
+ Map from new HRS role `UW_DYN_AM_ESS_FLU_MONTHLY` to new MyUW hrs-portlets
  role `ROLE_REDIRECT_TO_HRS_FLUID_TIME`. ( [HRSPLT-382][], [#150][] )

### 5.3.0 - leave balances and garnishments links

2018-10-30

+ Prefer to link new HRS URL `Classic ESS Abs Bal` rather than display leave
  balances directly on "Leave Balances" tab in "Time and Absence" (
  [HRSPLT-377][] , [#148][])
+ Add new `Garnishments/Wage Assignments` link in Payroll Information -->
  Earnings Statements tab, to new HRS URL `Fluid Garnishments`, conditioned on
  that `Fluid Garnishments` URL being set and the viewing employee having
  `ROLE_VIEW_OWN_GARNISHMENTS` ( [HRSPLT-378][] , [#149][] )
+ Map new `UW_DYN_PY_ESS_GARNISHMENTS` HRS role to new
  `ROLE_VIEW_OWN_GARNISHMENTS` hrs-portlets role ( [HRSPLT-379][] , [#149][] )
+ Document new `Fluid Garnishments` HRS URL as
  `HrsUrlDao.SELF_SERVICE_VIEW_GARNISHMENTS_KEY` ( [HRSPLT-378][] , [#149][] )

### 5.2.0 - Re-add Direct Deposit and W4 links

2018-10-22

+ "Update your Direct Deposit" link added to MyUW Payroll Information, on the
  "Earnings Statements" tab, since this is the form for adjusting where those
  earnings are deposited and so employees might expect to find it in an earnings
  statements context. When the user has `ROLE_VIEW_DIRECT_DEPOSIT` and the HRS
  URL `Direct Deposit` is present, links to that URL, otherwise links to static
  PDF form. ( [#147][], [HRSPLT-376][] )
+ "Update your W4" link added to MyUW Payroll Information, on the "Tax
  Statements" tab since W4 is the form for adjusting tax witholdings and so
  employees might expect to find it in a tax context. ( [#146][],
  [HRSPLT-375][])

### 5.1.2 - overcome artifact publication glitch

2018-10-19

It's 5.1.1 except actually published to artifact repositories.

### 5.1.1 - fix benefitInformation.jsp syntax error

2018-10-18

Bugged release: not actually published to artifact repositories.

+ Fix syntax error in `benefitInformation.jsp`

### 5.1.0 - add View/Update Dependent Information link

2018-10-18

#### New feature in 5.1.0

+ Adds "View/Update Dependent Information" link in Benefit Information, as the
  preferred link that when present replaces the legacy "View Dependent Details"
  link. The "View/Update Dependent Information" link appears iff the
  "Dependent/Beneficiary Info" HRS URL is present (and links to that URL). (
  [#145][], [HRSPLT-372][] )

### 5.0.0 - remove Direct Deposit and W4 links

2018-10-18

#### New features in 5.0.0

+ Adds Payroll Information resource URL for an employee to download their latest earnings statement.
  ( [#142][], [HRSPLT-368][] )

#### Fixes in 5.0.0

+ Fix Payroll Information table of tax statements to stop offering a sort UI
  control to sort on the name of the statement. Sorting by name is more
  confusing than it is helpful. Retains sort by statement year. ( [#141][],
  [HRSPLT-365][] )

#### Removed features in 5.0.0

+ "Update your Direct Deposit" link removed from Payroll Information (
  [#143][], [HRSPLT-370][])
+ "Update your W4" link removed from Payroll Information ( [#144][],
  [HRSPLT-371][])

## HRS Portlets 4 series

The HRS Portlets 4 major version was occasioned by the breaking change of
changing the meaning of `ROLE_VIEW_WEB_CLOCK`.

### 4.1.1

2018-10-15

+ Fix syntax error that caused the new Payroll Information links in 4.1.0 not
  to show.

### 4.1.0 Payroll Information deep links to tabs and links into HRS

2018-10-15

#### New features in 4.1.0

+ Payroll Information shows links to `View W-2`, `View 1095-C`, `W-2 Consent`,
  and `1095-C Consent`, iff it reads those URLs from the HRS URLs DAO. (No
  interdependency - each handled individually.) ( [#137][], [HRSPLT-362][],
  [HRSPLT-363][] )
+ Optionally deep link to a specific tab in Payroll Information by setting the
  `requestedContent` *Portlet* request parameter. Specifically, setting this to
  `Tax Statements` will select the "Tax Statements" tab, whereas omitting it or
  any other value will continue the default behavior of initially selecting the
  "Earning Statements" tab. Note that this is only practically useful when
  invoking the Portlet in a way that conveys portlet request parameters. i.e.
  the status quo naive `/web/exclusive/{fname}` in uPortal App Framework does
  not support this feature, whereas the legacy `/portal/p/{fname}` does *if* you
  indulge a modest abstraction violation and rely on knowledge of how uPortal
  names *Portlet* request parameters, i.e.
  `/portal/p/{fname}?pP_requestedContent=Tax%20Statements` . This is all by way
  of temporizing until the HRS Portlets can be replaced by implementations that
  are not Portlets, e.g. uPortal Application Framework applications.

### 4.0.0: `ROLE_VIEW_HRS_APPROVALS_WIDGET`, role refactor and apis, redirector

2018-10-04

#### Breaking changes in 4.0.0

This release refactors roles in "Time and Absence", inventing in-hrs-portlets
roles more explicitly mapping to UI controls and functions in hrs-portlets.
( [#125][] )

This is a breaking change in the sense that some previous roles disappear or
or change meaning, and new roles appear.

This is a non-breaking change, indeed a
no-discernible-impact-to-HRS-or-to-employees change in the sense that the
mapping from PeopleSoft roles to portlet roles is carefully ported forward such
that the same PeopleSoft roles result in the same privileges and experiences in
hrs-portlets.

Invents:

+ `ROLE_TIMESHEET_BUTTON`, gating display of the Timesheet button
+ `ROLE_VIEW_TIME_ENTRY_HISTORY`, gating access to Time Entry history data and
  display of this tab in Time and Absence

Removes:

+ `ROLE_VIEW_TIME_SHEET`
+ `ROLE_VIEW_TIME_CLOCK`

both of which had previously granted both access to the Timesheet button and
access to time entry history.

Changes the meaning of:

+ `ROLE_VIEW_WEB_CLOCK`, which now only grants the "Web Clock" link, but
  previously had also granted access to the Timesheet button and time entry
  history.

#### New features in 4.0.0

+ Adds `ROLE_VIEW_HRS_APPROVALS_WIDGET`, initially mapped from HRS roles
  `UW_DYN_PY_ADDL_PAY_APP` and `UW_UNV_TL Supervisor` ( [#128][], [#132][] )
+ As a *Servlet* in the HRSPortlets web application, adds `/go` redirector that
  takes a `urlKey` request parameter. When this maps to a URL known to the
  HrsUrlsDao, redirects to that URL. When this does not map to a URL known to
  that DAO, responds 404 not found. ( [#126][] )
+ Adds a JSON resource URL for asking whether the user has a specific HRS
  Portlets role. Intended for use in `switch` widget type to switch widget
  behavior on whether user has role. ( [#127][] )
+ Adds a JSON resource URL for asking what portlet roles the user does and does
  not have. Intended for use in uPortal App Framework message filtering.
  Structured similarly to `enrollmentFlag` for this reason. ( [#129][] )

## HRS Portlets 3 series

The HRS Portlets 3 major version was occasioned by the breaking change of
changing the meaning of `ROLE_VIEW_ABSENCE_HISTORIES`.

### 3.1.0: targeted notifications and notices for PHIT

2018-07-27

#### New features in 3.1.0

+ In Time and Absence, a new optional `portlet-preference`
  `dynPunchTimesheetNotification` drives a new in-HRS-app message (presented
  just like `notification`) to employees with the new
  `ROLE_UW_DYN_AM_PUNCH_TIME` role. ( [HRSPLT-346][], [#123][] )
+ In Time and Absence, a new optional `portlet-preference`
  `nonDynPunchTimesheetNotification` drives a new in-HRS-app message (presented
  just like `notification`) to employees who see the Timesheet button but who
  do not have the new `ROLE_UW_DYN_AM_PUNCH_TIME` role. ( [HRSPLT-346][],
  [#123][] )
+ In Time and Absence, a new optional `portlet-preference`
  `dynPunchTimesheetNotice` drives a new message near the Timesheet button
  (presented similarly to `timesheetNotice`) to employees with the new
  `ROLE_UW_DYN_AM_PUNCH_TIME` role. ( [HRSPLT-346][], [#123][] )

#### Changes in 3.1.0

+ In Time and Absence, the `leaveReportingNotice` when present is now ordered
  ahead of general `hrs:notifications` in-app notifications when present.
  ( [HRSPLT-348][], [#122][] )

### 3.0.0: split `ROLE_VIEW_ABSENCE_HISTORIES`

2018-07-06

#### Breaking changes in 3.0.0

The `ROLE_VIEW_ABSENCE_HISTORIES` portlet role now *only*
grants access to view absence histories and does *not* grant access to the
"Enter absence" and "Edit/cancel absence" buttons. (Access to those buttons is
now granted by new portlet role `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES`).

Migration path: to the extent that employees ought to continue to have all the
privileges of the prior implementation of `ROLE_VIEW_ABSENCE_HISTORIES`, also
grant those employees `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES`. ( #121 )

#### New features in 3.0.0

+ Add new portlet role `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES`, granting access to
  the "Enter absence" and "Edit/cancel absence buttons". This access is a subset
  of what `ROLE_VIEW_ABSENCE_HISTORIES` historically granted. ( #121 )
+ Add role mapping for new `UW_DYN_AM_PUNCH_TIME` HRS role. This HRS role grants
  `ROLE_VIEW_ABSENCE_HISTORIES` portlet role but does *not* grant new
  `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES` portlet role. ( #121 )

#### Changes in 3.0.0

+ Update role mapping for `UW_DYN_AM_EMPLOYEE` HRS role to grant the new
  `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES` portlet role in addition to the
  `ROLE_VIEW_ABSENCE_HISTORIES` portlet role it was already granting. This
  yields *no functional change* for employees with the `UW_DYN_AM_EMPLOYEE` HRS
  role. While `ROLE_VIEW_ABSENCE_HISTORIES` is losing privileges in this
  release, these employees regain those very same privileges via the
  `ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES` role. ( #121 )

## HRS Portlets 2 series

The HRS Portlets 2 major version was occasioned by the breaking change of
adding two methods to the Roles DAO API.

### 2.1.1 : Eagerly resolve leave reporting notice div conditional

2018-06-25

Fixed

+ Eagerly resolve whether to show leave reporting notice div ( #119 )

### 2.1.0

2018-06-04

New features

+ Add `leaveReportingNotice` feature in Time and Absence ( #118 )
+ Style HRS roles as <em>emphasized</em> as used as sentences in Troubleshooter
  ( #116 )

### 2.0.0 : Troubleshooter with more detail

2018-05-16

#### Breaking change in 2.0.0

Changes the Roles DAO API to add two methods, supporting

 + Enhanced Troubleshooting portlet, now with what HRS roles are known, the
   general mappings from HRS roles to Portlet roles, and a link to documentation
   ( #115 )

## HRS Portlets 1 series

### 1.12.0 : Roles troubleshooter

2018-05-14

#### New features in 1.12.0

+ Add `Troubleshooting` portlet ( #114 )
+ Require secure database connections by setting
  `;oracle.net.encryption_client=REQUIRED;` on `connectionProperties`
  ( 1b749187006099bea1448ac8a29e5cd69eb3f62a )

### 1.11.0 : Configurable Manager Time and Approval link labels

2018-04-16

Adjusts defaults for Manager Time and Approval dynamic hyperlink labels, and
makes these labels configurable via `portlet-preference`.

+ Title case and make configurable Manager "Approve Absence" and "Approve Time"
  link labels ( #111 )
+ shorten "Time/Absence Dashboard" label ( #112 )

### 1.10.0 : Dynamic Manager Time and Approval fixes and improvements

2018-04-13

Feature changes

+ Re-orders "HRS Manager Time and Approval" links to put "Approve absence" ahead
  of "Approve time". This is intended to better match typical manager approval
  workflows and a customization also made in the new HRS self-service Fluid
  approvals dashboard. ( #109 )
+ Shortens the default label for the new manager approval dashboard link in
  "Manager Time and Approval" to "Time & Absence Dashboard" so that it will not
  run over the 24 character limit and be truncated by the `list-of-links` widget
  type. ( #110 )

Bug fixes

+ Fix the "Approve time" link in the new dynamic Manager Time and Approval
  manager links ( #107 )
+ Fix URLs JSON to actually include the URLs (values) and not just the keys.
  That is, to make the list-of-link representation of this links that are actual
  hyperlinks that link to the URLs. ( #108 )

### 1.9.1 : Fix display of a button in Time and Absence

2018-04-12

Patch release to fix a display issue on a button in the Time And Absence app.

### 1.9.0 : Dynamic manager links widget

2018-03-29

New features: ManagerLinks portlet

+ `ManagerLinks` portlet, exposing a resource URL vending JSON in
  `list-of-links` format, representing role-filtered links available to the
  manager to launch into HRS self-service to approve time and/or absences ( #97,
  #100, #101, #102 )
+ Optional approvals dashboard link, with its label and URL specified via
  portlet preference, predicated on new `UW_DYN_TIME_ABS_DASH_USER` HRS role (
  #99, #103,  #104 )
+ Also offers equivalent rendered view, suitable for linking from the generated
  `list-of-links` widget.

New features: Roles portlet

+ `Roles` portlet, exposing a resource URL vending JSON in `list-of-links`
  format, representing the user's HRS roles as understood by (mapped to) the HRS
  Portlets (  #96 ).
+ Also offers equivalent rendered view, suitable for linking from the generated
  `list-of-links` widget.

New features:: Urls portlet

+ `Urls` portlet, exposing a resource URL vending JSON in `list-of-links`
  format, representing the HRS URLs as understood by the HRS Portlets
+ Also offers equivalent rendered view, suitable for linking from the generated
  `list-of-links` widget.

Style

+ Trivial whitespace fixes ( #98 )

### 1.8.0 : Approvals count JSON API

2018-02-14

New features:

+ JSON API to read quantity of viewer's pending approvals ( #94 )
+ Configurable link for "your human resource contact" in Personal Information (
  #95 )

### 1.7.0 : Tweaks including updated year-end-leave-balances label

2018-01-12

Changes:

+ Update year-end-leave-balances label for end of 2017. ( #92 )

Fixes:

+ Removes duplicate zero-balance vacation carryover entries in leave balances (
  #87 )
+ Changes advice about changing "Business/Office Address" to contact "your human
  resources office" rather than "your Payroll Office". ( #91 )
+ Revise Benefit Statement ordering ( #93 )

### 1.6.5 : Change spacing

2017-11-03

+ Fixes spacing of paragraphs ( #86 )

### 1.6.4 : More conservative Personal Information presentation

2017-10-26

There are no functional or configuration changes in this release.

+ Makes more conservative the HRS PUM22 changes previously made in ( #75 )
  characterizing and linking the self-service UI for updating personal
  information in HRS. ( #85 )

### 1.6.3 : further fix new direct deposit self-service link feature

2017-10-24

1.6.0's new feature ( #80 ) was bugged. 1.6.1 tried to fix this, but it was
still bugged. Further fixes with ( #82 )

### 1.6.2 : unpublished

This release did not successfully publish. Its fix is documented above in 1.6.3.

### 1.6.1 : failed attempt at fixing bugged self-service direct deposit linking

2017-10-24

Attempted to fix bugged ( #80 ) with ( #81 ), but it didn't work.

### 1.6.0 : bugged link to HRS self-service direct deposit UI

2017-10-24

BUGGED. (Use `1.6.3` or later instead.)

This release adds one feature: support for an optional HRS role granting access
to the new self-service direct deposit management UI in PeopleSoft 9.2 PUM22. (
#80 ).

This is entirely backwards compatible and fails gracefully, falling back on
v1.5.0 behavior:

+ If the viewing employee does not have the new role (HRS role
  `UW_DYN_PY_DIRDEP_SS`, mapped to an hrs-portlets role of
  `ROLE_VIEW_DIRECT_DEPOSIT`), the UI is unchanged, continuing to link a PDF
  form for direct deposit updating (as hardcoded in the JSP).
+ If the new portlet-preference `directDepositSelfServiceUrl` is not set,
  *even if the employee has the new role*, the UI is unchanged, continuing to
  link a PDF form for direct deposit updating (as hardcoded in the JSP).

If the employee has the role and the portlet-preference is set, the direct
deposit link in the UI uses the URL configured in the portlet preference
(intention: the HRS self-service URL) rather than the previously hard-coded URL
to the PDF form.

(The portlet preference could be configured with any URL, including with the URL
to the PDF form, so that's another way to turn off this new feature in a pinch.)

### 1.5.0 : Benefit Summary URL portlet preference

2017-10-12

New feature:

+ Optionally, the benefit summary URL link in Benefit Information can be
  specified via the new `benefitsSummaryUrl` portlet-preference, overriding any
  value coming through the HRS URLs SOAP integration. ( #78 as fixed in #79 ).

Backwards compatible: don't set the new preference and the portlet continues to
function as in `1.4.0`.

### 1.4.0 : add Edit/Cancel Absences button

New feature:

+ Adds support in Time and Absence for new optional `editCancelAbsenceUrl`
  portlet-preference . When set, shows new Edit/Cancel Absence button to launch
  the URL described by that portlet-preference value. When not set, no-op.
  Intended to support graceful upgrade to HRS PUM22 self-service UIs. ( #76 )

Fix:

+ `1.3.0` was bugged bug such that when the Personal Information self-service
  launch URL was configured via the new-in-that-release optional
  `updateMyPersonalInfoUrl` portlet-preference, the resulting UI included broken
  hyperlinks. This release fixes that bug. ( #77 )

### 1.3.0 : add Personal Information self-service URL portlet-preference

2017-10-06

New feature:

+ honor optional `updateMyPersonalInfoUrl` portlet preference superseding
  corresponding URL from HRS SOAP web service ( #75 )

This release adds support for overriding the URL linking to HRS self-service UI
for updating personal information, via a new `updateMyPersonalInfoUrl` portlet
preference.

+ When the preference is set, the UI reflects the new HRS PUM22 context (text,
  other links around) for the link.
+ When the preference is not set, the portlet continues to get this URL from the
  HRS URLs SOAP web service with its prior context (text, other links around)
  the link.
+ This release also adds an error state for when the URL is available neither
  from the portlet preference nor from the HRS URLs web service. This edge case
  is not expected to actually happen in practice.

### 1.2.0 : enrollmentFlag API and service window message

2017-09-29

New features:

+ Add enrollmentFlag resource mapping (portlet resource URL) ( #74 )
+ Note HRS service window in footers within the portlet UIs ( #72 )
+ Add trace logging in enrollment flag parsing ( #73 )


### 1.1.3 : workaround fixing Leave Reports column disappearance

2017-09-21

Fix:

+ Workaround to resolve race condition affecting display of Leave Reports column
  ( #71 )


### 1.1.2 : add enrollmentFlag to benefitSummary JSON API; rm furlough balance

2017-07-24

New features:

+ Adds `enrollmentFlag` to `benefitSummary` resource URL ( #69 )
+ Filters furlough balance from leave balances, if furlough balance is zero (
  #70 )
+ Documents `enrollmentFlag` values ( #68 )

### 1.1.1 : style a link

2017-03-20

+ Changes styling of a link to look like a button ( #67 )

### 1.1.0 add HRS URLs API

2017-01-09

New features:

+ Adds endpoint to retrieve HRS URLs ( #63 )
+ Adds log statement when interpreting a null response as an empty set when
  retrieving and translating roles ( #65 )

Fixes:

+ Updates year-end leave balances label to 2016 text ( #64 )
+ Fixes bug where color labels didn't appear ( #63 )

### 1.0.0.38...

This and many more earlier releases exist as [releases in the GitHub repo][].


[releases in the GitHub repo]: https://github.com/UW-Madison-DoIT/hrs-portlets/releases

[#122]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/122
[#123]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/123
[#125]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/125
[#126]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/126
[#127]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/127
[#128]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/128
[#129]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/129
[#132]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/132
[#137]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/137
[#141]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/141
[#142]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/142
[#143]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/143
[#144]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/144
[#145]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/145
[#146]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/146
[#147]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/147
[#148]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/148
[#149]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/149
[#150]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/150
[#152]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/152
[#153]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/153
[#155]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/155
[#160]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/160
[#161]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/160
[#162]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/162
[#163]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/163
[#164]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/164
[#166]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/166
[#167]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/167
[#168]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/168
[#169]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/169
[#172]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/172
[#173]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/173
[#174]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/174
[#175]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/175
[#176]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/176
[#178]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/178
[#179]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/179
[#180]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/180
[#181]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/181
[#182]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/182
[#183]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/183
[#185]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/185
[#194]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/194
[#197]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/197
[#198]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/198
[#200]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/200
[#201]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/201
[#202]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/202
[#205]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/205
[#216]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/216
[#217]: https://github.com/UW-Madison-DoIT/hrs-portlets/pull/217

[HRSPLT-346]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-346
[HRSPLT-348]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-348
[HRSPLT-362]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-362
[HRSPLT-363]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-363
[HRSPLT-365]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-365
[HRSPLT-368]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-368
[HRSPLT-369]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-369
[HRSPLT-370]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-370
[HRSPLT-371]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-371
[HRSPLT-375]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-375
[HRSPLT-376]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-376
[HRSPLT-377]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-377
[HRSPLT-378]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-378
[HRSPLT-379]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-379
[HRSPLT-381]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-381
[HRSPLT-382]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-382
[HRSPLT-384]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-384
[HRSPLT-386]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-386
[HRSPLT-390]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-390
[HRSPLT-391]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-391
[HRSPLT-394]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-394
[HRSPLT-398]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-398
[HRSPLT-399]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-399
[HRSPLT-400]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-400
[HRSPLT-401]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-401
[HRSPLT-402]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-402
[HRSPLT-403]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-403
[HRSPLT-404]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-404
[HRSPLT-405]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-405
[HRSPLT-408]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-408
[HRSPLT-411]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-411
[HRSPLT-412]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-412
[HRSPLT-415]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-415
[HRSPLT-418]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-418
[HRSPLT-423]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-423
[HRSPLT-424]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-424
[HRSPLT-425]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-425
[HRSPLT-426]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-426
[HRSPLT-429]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-429
[HRSPLT-434]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-434
[HRSPLT-436]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-436
[HRSPLT-437]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-437
[HRSPLT-449]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-449
[HRSPLT-453]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-453
[HRSPLT-454]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-454
[HRSPLT-459]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-459
[HRSPLT-460]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-460
[HRSPLT-461]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-461
[HRSPLT-463]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-463
[HRSPLT-466]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-466
[HRSPLT-467]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-467
[HRSPLT-468]: https://jira.doit.wisc.edu/jira/browse/HRSPLT-468

[MUMMNG-4833]: https://jira.doit.wisc.edu/jira/browse/MUMMNG-4833
