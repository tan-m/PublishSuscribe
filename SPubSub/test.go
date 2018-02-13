package main
import (
  "fmt"
  "log"
  "os"
  "simplepubsub"
// Imports the Google Cloud Pub/Sub client package.
  "cloud.google.com/go/pubsub"
  "golang.org/x/net/context"
)

type ClientTest struct {
    c *pubsub.Client
    ctx context.Context
}

func main() {

  c,ctx := simplepubsub.Join()
  // Sets the name for the new topic.
  topicName := "Jenka2"
  if len(os.Args) == 2 {
    topicName = os.Args[1]
  }
  // Creates the new topic.
  topic, err := simplepubsub.CreateTopic(c, ctx, topicName)
  if err != nil {
    fmt.Println("Failed to create topic: %v", err)
  }

  // create a list of subscriptions
  // Returning a subscription is not important but usefule during testing
  // instead of taking a convoluted path of extracting the Topic and generating
  // the subscriptions
  sub, err := simplepubsub.Subscribe(c,ctx, topic)
  if err != nil {
    fmt.Println("Failed to subscribe as topic doesn't exist")
  }

  _ = simplepubsub.Publish(c,ctx, topic, "lafoot")
  err = simplepubsub.Publish(c,ctx, topic, "lafoot2")
  if err != nil {
    fmt.Println(err)
  }
  // Consume the message that was sent by the publish messages
  err = simplepubsub.Consume(c, ctx, sub)
  if err != nil {
    fmt.Println(err)
  }
  // delete the subscritption based on the topic
  err = simplepubsub.Unsubscribe(c,ctx, topic)
  if err != nil {
    log.Fatalf("Failed to Unsubscribe as topic doesn't exist")
  }

}
