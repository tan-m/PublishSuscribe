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


func Join() (c *pubsub.Client, ctx context.Context) {
  ctx = context.Background()
  // Sets your Google Cloud Platform project ID.
  projectID := "pubsub-194903"
  // Creates a client.
  c, err := pubsub.NewClient(ctx, projectID)
  if err != nil {
    log.Fatalf("Failed to create client: %v", err)
  }
  return c, ctx
}

func CreateTopic( c *pubsub.Client,ctx context.Context, 
         topicName string) (topic *pubsub.Topic, err error) {
  // Creates the new topic.
  topic, err = c.CreateTopic(ctx, topicName)
  if err != nil {
    log.Fatalf("Failed to create topic: %v", err)
  }
  fmt.Println("Successful in creating topic :%v", topic)
  return topic,err
}

//Subscribe to one or more topics at the same time
func Subscribe( c *pubsub.Client,ctx context.Context,
              topic *pubsub.Topic) (sub *pubsub.Subscription,err error) {
  ok, err := topic.Exists(ctx)
  if err == nil {
    val := topic.ID()
    fmt.Println("subscribing to ", val)
    sub,err =  c.CreateSubscription(ctx, val, pubsub.SubscriptionConfig {
                Topic:topic,AckDeadline: 10 * time.Second,
                })
  }
  if !ok {
    log.Fatalf("Failed to subscribe as topic doesn't exist")
  }
  return sub, err
}

//Unsubscribe from one or more topics at the same time 
func Unsubscribe( c *pubsub.Client,ctx context.Context, 
                  topic *pubsub.Topic) (err error){
  _, err = topic.Exists(ctx)
  if err != nil {
    log.Fatalf("Topic doesn't exist to unsubscribe")
  }
  for subs := topic.Subscriptions(ctx) ;; {
    sub, err := subs.Next()
    if err == iterator.Done {
      break
    }
    if err = sub.Delete(ctx); err == nil {
      fmt.Println("unsubscribing from ",topic.ID())
    }
  }
  return err
}

//PUblish message msg to the Subscribers
func Publish(c *pubsub.Client, ctx context.Context, topic *pubsub.Topic, 
                        msg string) (err error) {
  result := topic.Publish(ctx, &pubsub.Message{
            Data: []byte(msg),
          })
  id, err := result.Get(ctx)
  fmt.Printf("Published a message; msg ID: %v\n", id)
  return err
}

func Consume(c *pubsub.Client, ctx context.Context, 
              sub *pubsub.Subscription) (err error) {
  var mu sync.Mutex
  received := 0
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
