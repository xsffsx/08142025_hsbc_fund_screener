<!-- PROJECT LOGO -->
<br />
<p align="center">
  <!--<a href="https://github.com/othneildrew/Best-README-Template">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>-->

  <h3 align="center">Market Data Service</h3>

  <p align="center">
    An awesome README to jumpstart HASE MDS!
    <br />
    <a href="https://alm-github.systems.uk.hhhh/pages/mobile-price-org/hase-apidoc/"><strong>MDS API Doc »</strong></a>
    <br />
    <br />
    <!-- <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Report Bug</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">MDS API Doc</a> -->
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#mDS-innovation">MDS Innovation</a></li>
    <li><a href="#production-support-models">Production Support Models</a></li>
    
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

HASE MDS provides the service to support CSC/HK/US equity trading. The relevant APIs provide the data of real-time/delay equity quotes.

#### Feature Highlights
* Predictive Search - It provides the retrieve product list from LabCI, such as the product M code, production Ric Code, exchange code, product short name, product full name and symbol.
* Quotes - It provides the stock price info to view the real-time/delay stock prices and stock quotes for a financial overview (Supported SEC, ETF and ADR).
* Index Quotes -  It provides the index price info and its financial overview (Supported index list and world index list)
* Top Mover - It provides the top 10 securities in CSC/HK/US market with specific ranking, such as VOL, GAINPCT, LOSEPCT and RATEUP.
* News Detail - It provides the latest news on the stock market and events that moving stocks, with analyses to help you make investing and trading decisions.
* News Headlines - It provides the headlines of the news (Refer to new details)
* Charts Performance - It provides the chart raw data with a certain period.

### Built With

* [SpringBoot](https://spring.io/projects/spring-boot)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [Spring Could](https://spring.io/projects/spring-cloud)
* [Jenkins](https://www.jenkins.io)
* [Kubernetes](https://kubernetes.io/)
* [Amazon Web Services](https://aws.amazon.com/)


<!-- GETTING STARTED -->
## Getting Started

* Try the [Quickstart Guide](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)
* Try [Running MDS componments in your local env](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)
* Try [Building MDS componments by Jenkins](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)
* Try [Deploying MDS componments to AWS](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)

### Prerequisites

* IDEA
* Nexus Access Rights
* Git

If you are not ready, please refer to [New Joiner Guide](https://wpb-confluence.systems.uk.hhhh/display/HASEWP/New+Staff+On+Board#NewStaffOnBoard-6.SetupWorkspacefordevelopment) to setup local DEV env.

### Installation

1. Get the Github access and the MDS repository R/W permissions.
2. Clone the repo
   ```sh
   git clone https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app.git
   ```
3. To download the dependencies and relevant plugins.
   ```sh
   mvn -f wmds-equity\pom.xml clean install -Dmaven.test.skip=true
   ```


<!-- USAGE EXAMPLES -->
## IT Verification

Currently we are using [Postman](https://www.postman.com/) to test/verify MDS APIs.(Please download it by rasing ServiceNow.)

_For more examples, please refer to the [Documents](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)_

## MDS Innovation

As we know, MDS is using RESTful API to support multiple systems/services, the API response message is getting bigger and bigger. Currenly we are considering using [GraphQL](https://graphql.org/) as a runtime API for fulfilling those exact requseted field(s).

## Production Support Models
_(To be supplemsnted)_

<!-- CONTACT -->
## Contact

* [Maybach - maybach.z.h.fang@hhhh.com](https://alm-github.systems.uk.hhhh/45059926)  
* Jerry - jerry.h.j.mo@hhhh.com.cn

Project Link: [https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws](https://alm-github.systems.uk.hhhh/RBWM-Wealth-Engineering-IT/wealth-wp-price-app/edit/hk_hase_aws)

