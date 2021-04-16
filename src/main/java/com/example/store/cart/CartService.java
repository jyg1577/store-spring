package com.example.store.cart;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CartService {

	private RabbitTemplate rabbit;

	@Autowired
	public CartService(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
	}

	public void sendOrder(Cart cart) {
		System.out.println("------ STORE LOG ------");
		rabbit.convertAndSend("store.order", cart);
		System.out.println(cart);
	}

}
