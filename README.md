# exchange-rates
Exchange Rates information

Prerequisites:

1.Use Proper access key in application.properties file that has subscription to access the exchangeratesapi timeseries url.
2.Use ssl access for accessing the exchangeratesapi timeseries https url as application is verified using http access with free subscription.

Sample Request

http://localhost:8080/exchange-rates/load-exchange-rates

http://localhost:8080/exchange-rates/date/2020-10-03

http://localhost:8080/exchange-rates/date-range?fromDate=2020-09-01&toDate=2020-10-03
