package main
import (
  "fmt"
  "log"
  "os"
  "simplepubsub"
  "cloud.google.com/go/pubsub"
  "math/rand"
)

func main() {
  c := simplepubsub.Join()
  // Sets the name for the new topic.
  topicName := "Jenka2"
  if len(os.Args) == 2 {
    topicName = os.Args[1]
  }
  // Creates the new topic.
  topic := c.CreateTopic(topicName)
  // create a list of subscriptions
  // Returning a subscription is not important but usefule during testing
  // instead of taking a convoluted path of extracting the Topic and generating
  // the subscriptions
  sub, err := c.Subscribe(topic)
  if err != nil {
    fmt.Println("Failed to subscribe as topic doesn't exist")
  }

  _ = c.Publish(topic, "simple message")
  err = c.Publish(topic, "complex message")
  if err != nil {
    fmt.Println("Failed to publish to the server")
  }
  // Consume the message that was sent by the publish messages
  err = c.Consume(sub)
  if err != nil {
    fmt.Println("Failed to consume the messages at the subscribers")
  }

  // delete the subscritption based on the topic
  err = c.Unsubscribe(topic)
  if err != nil {
    log.Fatalf("Failed to Unsubscribe as topic doesn't exist")
  }

  if c.Cleanup() != nil {
    log.Fatalf("Failed to close all the resources of a client")
  }

  // Run a set of clients instead of a single client
  var clist [3]simplepubsub.Client
  for i:=0; i < len(clist); i++ {
    clist[i] = simplepubsub.Join()
  }

  var t []*pubsub.Topic
  t = append(t,clist[1].CreateTopic("Roxette"))
  t = append(t,clist[1].CreateTopic("ManUtd"))
  t = append(t,clist[1].CreateTopic("TaylorSwift"))
  t = append(t,clist[0].CreateTopic("DaysofThunder"))
  t = append(t,clist[0].CreateTopic("ChelseaFC"))
  t = append(t,clist[0].CreateTopic("SandraBullock"))
  t = append(t,clist[2].CreateTopic("KatyPerry"))
  t = append(t,clist[2].CreateTopic("Somethingsomething"))
  t = append(t,clist[2].CreateTopic("MinnesotaUnited"))

  //Randomly subscribe to a different topic for a different client
  for i:=0; i < 6; i++ {
    client := rand.Int() % len(clist)
    top_rand := rand.Int()% len(t)
    clist[client].Subscribe(t[top_rand])
  }


}
