package com.example.store.product;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private ProductRepository repo;

	@Autowired
	public ProductService(ProductRepository repo) {
		this.repo = repo;
		System.out.println(repo);
	}

	@RabbitListener(bindings = {
			@QueueBinding(exchange = @Exchange(name = "amq.topic", type = "topic"), value = @Queue(value = "mdm.product.2"), key = {
					"mdm.product" }),

	})
	public void receiveProduct2(Product product) {
		System.out.println("-- product.2 --");
		System.out.println(product);

		Product storeProduct = Product.builder().code(product.getCode()).category(product.getCategory())
				.name(product.getName()).price(product.getPrice()).quantity(product.getQuantity())
				.shortDescription(product.getShortDescription()).description(product.getDescription()).build();

		System.out.println(storeProduct);
		repo.save(storeProduct);

	}

}
