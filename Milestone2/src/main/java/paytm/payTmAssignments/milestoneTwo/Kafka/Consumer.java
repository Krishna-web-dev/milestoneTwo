package paytm.payTmAssignments.milestoneTwo.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
  
	@KafkaListener(topics="mytopic", groupId="mygroup")
	public void consumeFromTopic(String message) {



		System.out.println("Consummed message "+message);
	}
	@KafkaListener(topics="mytopic2", groupId="mygroup")
	public void consumeFromTopic2(String message) {



		System.out.println("Consummed message"+message);
	}

}
