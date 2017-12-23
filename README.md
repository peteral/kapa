# kapa

## Introduction

This tool aims to help to give transparency to current commitment of the development team and help with the decisions in the planning and budgeting process.

This goal is acchieved by displaying a visual **Roadmap** containing the current commitment including feature-unrelated tasks like software maintenance.

This tool was created for a specific need so it only addresses a very specific organisation setup.

### Software

The developed software is two-fold:

* **Product / Platform** - standard software sold per license
* **Customer-specific development** - the company provides tailred services for their customers. Sometimes a one-customer-only feature is needed like for example SAP-connector, special reports, customer-specific workflow support,...

### Organisation

The organization is a result of the struggle of a company who tries to do agile software development in a market which requires fixed price / schedule and therefore is not agile.

When we imagine the process as a pipe line, then it might look like this:

<table>
    <tr>
        <td>Product management
        <td>RE
        <td>Epic Backlog
        <td>PO 1
        <td>Backlog 1
        <td>Dev Team 1
        <td>Platform
        <td>
    <tr>
        <td>Customer A
        <td>
        <td>SRS
        <td>PO 2
        <td>Backlog 2
        <td>Dev Team 2
        <td>Platform, Project B
        <td>Integration B
    <tr>
        <td>Customer B
        <td>
        <td>
        <td>PO 3
        <td>Backlog 3
        <td>Dev Team 3
        <td>Project A
        <td>Integration A        
</table>

* Feature stakeholders are external customers or internal product management
* Requirements Engineering supports the product management and sales team in rough feature definition and cost estimation. Goal - quotation in sales process or rentability decision in product development.
* These roughly estimated features land in the Epic Backlog. Epics for customer specific development have a order-intake probability estimated by the sales manager.
* The Epic backlog priorisation determines the order in which the Requirement Engineering processes the Epics.
* The requirements engineering works with system requirement specifications, click dummies, etc. in order to understand the new feature, clarify interfaces and collest as much information before the development starts as possible.
* RE creates user stories together with the product owner and presents them to the team responsible for given module and gets team feedback.
* The priorised team backlog the epics broken up into stories as well as bugs, maintenance and infratructure stories.
* More teams can be responsible for the platform, usually only one team is responsible for a customer-specific solution.
* The development works in a classical SCRUM process.
* The software is integrated and deployed by customer-specific teams.

The scope of this software is high-level planning of the roadmaps of the requirement engineering and development teams.

**Input:**

* Teams with their capabilities (velocity, %-maintenance work, sprint-length)
* Epics with total complexity estimation ordered by priority
* Team-Tasks with dependencies (delay from another team's task), estimation, speed and probability of intake

**Output:**
* Roadmap for each team