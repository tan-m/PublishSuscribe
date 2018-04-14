# PublishSuscribe

Project 1 CSci 5105 - Implementing Publish Subscribe system.

Team Members:
 - Tanmay Mehta: mehta206@umn.edu
 - Aditya Pakki: pakki001@umn.edu

Folder Details:
  - design.pdf - The design document containing all the design choices
  - testcases.pdf - Test cases that we have attempted to run
  - src/ - source code for all the from Scratch PubSub implementation
  - SPubSub - directory containing the Google PubSub implementation and test cases
  - begin.sh - empty shell file that should have been used as a startup script. Will fill
  given some time

Usage:
In src/ folder perform
### Single Client test runs:
W1:  In a new terminal window run ./registry_server_test
W2:  javac \*.java
W2:  java StartServer
W3:  java Client <ip address> <port>

### Multi Client test runs:
Assuming that the number of clients are  more than one. In the 
first two windows, start the server, and test_registry_server. In third window
run the clients by calling ClientDriver program.

W1:  In a new terminal window run ./registry_server_test
W2:  javac \*.java
W2:  java StartServer
W3:  java ClientDriver ip

For SPubSub directory:
- Assuming that go is installed.
- To start running the google pubsub server run the following:
gcloud beta emulators pubsub start --host-port=csel-kh4250-35.cselabs.umn.edu:46839 &
- To run the go programs - go build [pgm].go && go run [pgm].go
- Build a client.go file 
