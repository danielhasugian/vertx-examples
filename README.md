# vertx-examples
vertx learning

Sample routing for web
- localhost:portNumber/
- localhost:portNumber/employees (get : list employes form reponse body)
- localhost:portNumber/employees/setup (get : setup list employes form reponse body)
- localhost:portNumber/employees/clear (get : clear list employes form reponse body)
- localhost:portNumber/employees/:id (get : get employe form reponse body)
- localhost:portNumber/employees/:id (put : add employe form reponse body)

Import this project and try run with this command
- mvn package exec:exec@run-app
or create jar first
- mvn package
then run with java
java -jar nameofjar.jar
