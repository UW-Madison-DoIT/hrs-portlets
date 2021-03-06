# Benefit Information widget

The Benefit Information Portlet includes
a resource URL
implementing the Benefit Information widget
as a remote-content type widget.

This widget supports Annual Benefits Enrollment.

## Architecture

The Benefit Information widget participates in a Model View Controller
architecture. The controller relies upon services and repositories.

### Services

The `AnnualBenefitEnrollmentDatesService` (sic)
[encodes the dates for Annual Benefits Enrollment][AnnualBenefitEnrollmentDatesService]
and provides a handy API for understanding the meaning of these dates.

The `BenefitsLearnMoreLinkGenerator` service
[determines the best URL for the learn more link ][BenefitsLearnMoreLinkGenerator]
depending upon

+ the viewing user's roles,
+ whether the user is a Madison user, and
+ answers from the `AnnualBenefitEnrollmentDatesService`.

### Repositories

Data Access Objects (sic) model access to HRS data and
are implemented as a light caching layer on top of
PeopleSoft HRS SOAP web services.

### Controller

The `BenefitInformationController` [implements the logic for the widget][BenefitInformationController benefitInformationWidget method].
The controller relies upon the `AnnualBenefitEnrollmentDatesService`
to understand how the current moment relates to the annual benefits enrollment
event lifecycle.
The controller also relies upon the `BenefitsLearnMoreLinkGeneratator` to
determine the appropriate learn more hyperlink for the viewing user.
The controller dispatches to a view, passing along a model it builds up.

### Views

JSPs implement the views for this widget.

## Widget states with example screenshots

Here are the widget states with example screenshots.

The first of these states that applies will display.

## New hire benefits enrollment opportunity

Users with the MyUW HRS Portlets role `ROLE_VIEW_NEW_HIRE_BENEFITS`
see the `benefitInformationWidgetPersonalEnrollmentEvent`
[view][benefitInformationWidgetPersonalEnrollmentEvent JSP].

![Screenshot of Annual Benefits Enrollment widget stating a benefit enrollment opportunity with Enroll now button and Learn more link](./benefit-information-widget-personal-enrollment-event.png)

The "Learn more" link is to

+ the [UW-Madison new employee benefits enrollment page][], for Madison users
+ the [University of Wisconsin System employee benefits page][], for non-Madison users.

## Foreshadowing Annual Benefits Enrollment

If the `AnnualBenefitEnrollmentDatesService` believes that it is currently the
right time to foreshadow Annual Benefits Enrollment,
the viewing user will see
the `benefitInformationWidgetAnnualEnrollmentForeshadowing`
[view][benefitInformationWidgetAnnualEnrollmentForeshadowing JSP].

![Screenshot of Annual Benefits Enrollment widget stating benefit enrollment begins September 30 Learn more link](./benefit-information-widget-abe-foreshadowing.png)

The "Learn more" link is to

+ the [UW-Madison annual benefits enrollment page][], for Madison users
+ the [University of Wisconsin System annual benefits enrollment page][], for non-Madison users

## Last day of Annual Benefits Enrollment for eligible users

If the viewing user has the role `ROLE_VIEW_OPEN_ENROLL_BENEFITS`
and the `AnnualBenefitEnrollmentDatesService` believes that it is the last day
of Annual Benefits Enrollment,
the viewing user will see
the `benefitInformationWidgetAnnualEnrollmentLastDay`
[view][benefitInformationWidgetAnnualEnrollmentLastDay JSP].

**Note that otherwise eligible users lose the `ROLE_VIEW_OPEN_ENROLL_BENEFITS`,
typically overnight, after exercising their annual benefit enrollment
opportunity.**

![Screenshot of Annual Benefits Enrollment widget stating annual benefit enrollment ends today with Enroll now button and learn more link](./benefit-info-widget-abe-last-day.png)

The "Learn more" link is to

+ the [UW-Madison annual benefits enrollment page][], for Madison users
+ the [University of Wisconsin System annual benefits enrollment page][], for non-Madison users

## During ABE for eligible people

If the viewing user has the role `ROLE_VIEW_OPEN_ENROLL_BENEFITS`
and the `AnnualBenefitEnrollmentDatesService` believes that it is during the
Annual Benefits Enrollment period but not the last day of that period,
the viewing user will see
the `benefitInformationWidgetAnnualEnrollmentDuringCountdown`
[view][benefitInformationWidgetAnnualEnrollmentDuringCountdown JSP].

**Note that otherwise eligible users lose the `ROLE_VIEW_OPEN_ENROLL_BENEFITS`,
typically overnight, after exercising their annual benefit enrollment
opportunity.**

![Screenshot of Annual Benefits Enrollment widget stating 12 days left in annual benefit enrollment with Enroll now button and learn more link](./benefit-info-widget-abe-12-days-left.png)

The "Learn more" link is to

+ the [UW-Madison annual benefits enrollment page][], for Madison users
+ the [University of Wisconsin System annual benefits enrollment page][], for non-Madison users

## Feedback period after annual benefits enrollment

If the `AnnualBenefitEnrollmentDatesService` believes that it is during the
ABE feedback period, the viewing user will see
the `benefitInformationWidgetAnnualEnrollmentFeedback`
[view][benefitInformationWidgetAnnualEnrollmentFeedback JSP].

![Screenshot of Annual Benefits Enrollment widget stating date annual benefits enrollment ended and offering link to give feedback](./benefit-info-widget-abe-feedback.png)

## Default Benefit Information widget

If none of the above cases applies,
the viewing user will see
the `benefitInformationWidget`
[view][benefitInformationWidget JSP].

![Screenshot of Annual Benefits Enrollment widget offering a Learn more link](./benefit-info-widget-default.png)

The "Learn more" link is to

+ the [UW-Madison benefits page][], for Madison users
+ the [University of Wisconsin System employee benefits page][], for non-Madison users

[AnnualBenefitEnrollmentDatesService]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-api/src/main/java/edu/wisc/hr/service/benefits/AnnualBenefitEnrollmentDatesService.java
[BenefitsLearnMoreLinkGenerator]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/java/edu/wisc/portlet/hrs/web/benefits/BenefitsLearnMoreLinkGenerator.java
[BenefitInformationController benefitInformationWidget method]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/java/edu/wisc/portlet/hrs/web/benefits/BenefitInformationController.java#L165

[benefitInformationWidgetPersonalEnrollmentEvent JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidgetPersonalEnrollmentEvent.jsp
[UW-Madison new employee benefits enrollment page]: https://hr.wisc.edu/benefits/new-employee-benefits-enrollment/
[University of Wisconsin System employee benefits page]: https://www.wisconsin.edu/ohrwd/benefits/

[benefitInformationWidgetAnnualEnrollmentForeshadowing JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidgetAnnualEnrollmentForeshadowing.jsp
[UW-Madison annual benefits enrollment page]: https://hr.wisc.edu/benefits/annual-benefits-enrollment/
[University of Wisconsin System annual benefits enrollment page]: https://www.wisconsin.edu/abe/

[benefitInformationWidgetAnnualEnrollmentLastDay JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidgetAnnualEnrollmentLastDay.jsp
[benefitInformationWidgetAnnualEnrollmentDuringCountdown JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidgetAnnualEnrollmentDuringCountdown.jsp
[benefitInformationWidgetAnnualEnrollmentFeedback JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidgetAnnualEnrollmentFeedback.jsp

[benefitInformationWidget JSP]: https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-webapp/src/main/webapp/WEB-INF/jsp/benefitInformationWidget.jsp
[UW-Madison benefits page]: http://benefits.wisc.edu/
