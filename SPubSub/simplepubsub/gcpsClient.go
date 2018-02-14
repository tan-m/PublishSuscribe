package simplepubsub
import (
  "fmt"
  "log"
  "time"
  "sync"
// Imports the Google Cloud Pub/Sub client package.
  "cloud.google.com/go/pubsub"
  "golang.org/x/net/context"
  "google.golang.org/api/iterator"
)

type Client struct {
    cli *pubsub.Client
    ctx context.Context
}

func  Join() (client Client) {
  ctx := context.Background()
  // Sets your Google Cloud Platform project ID.
  projectID := "pubsub-194903"
  // Creates a client.
  cli, err := pubsub.NewClient(ctx, projectID)
  if err != nil {
    log.Fatalf("Unable to create a client")
  }
  return  Client{cli, ctx}
}

func (client *Client) CreateTopic(topicName string) (topic *pubsub.Topic) {
  // Creates the new topic.
  topic, err := client.cli.CreateTopic(client.ctx, topicName)
  if err != nil {
    log.Fatalf("Failed to create topic: %v", err)
  }
  fmt.Println("Successful in creating topic :%v", topic)
  return topic
}

//Subscribe to one or more topics at the same time
func (client *Client) Subscribe(topic *pubsub.Topic) (sub *pubsub.Subscription,
                                                      err error) {
  ok, err := topic.Exists(client.ctx)
  if err == nil {
    val := topic.ID()
    fmt.Println("subscribing to ", val)
    sub,err =  client.cli.CreateSubscription(client.ctx, val, pubsub.SubscriptionConfig {
                Topic:topic,AckDeadline: 10 * time.Second,
                })
  }
  if !ok {
    log.Fatalf("Failed to subscribe as topic doesn't exist")
  }
  return sub, err
}

//Unsubscribe from one or more topics at the same time 
func (client *Client) Unsubscribe(topic *pubsub.Topic) (err error){
  _, err = topic.Exists(client.ctx)
  if err != nil {
    log.Fatalf("Topic doesn't exist to unsubscribe")
  }
  for subs := topic.Subscriptions(client.ctx) ;; {
    sub, err := subs.Next()
    if err == iterator.Done {
      break
    }
    if err = sub.Delete(client.ctx); err == nil {
      fmt.Println("unsubscribing from ",topic.ID())
    }
  }
  return err
}

//PUblish message msg to the Subscribers
func (client *Client) Publish(topic *pubsub.Topic, msg string) (err error) {
  result := topic.Publish(client.ctx, &pubsub.Message{
            Data: []byte(msg),
          })
  id, err := result.Get(client.ctx)
  fmt.Printf("Published a message; msg ID: %v\n", id)
  return err
}


func (client *Client) Consume(sub *pubsub.Subscription) (err error) {
  var mu sync.Mutex
  received := 0
  ctx := client.ctx
  cctx, cancel := context.WithCancel(ctx)
  err = sub.Receive(cctx, func(ctx context.Context, msg *pubsub.Message) {
    mu.Lock()
    defer mu.Unlock()
    received++
    if received >= 2 {
      cancel()
      msg.Nack()
      return
    }
    fmt.Printf("Got message: %q\n", string(msg.Data))
    msg.Ack()
  })
  return err
}

//close the client to retrieve all resouces
func (client *Client) Cleanup() (err error) {
  err = client.cli.Close()
  return err
}
