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
	private ProductImageRepository imageRepo;

	@Autowired
	public ProductService(ProductRepository repo, ProductImageRepository imageRepo) {
		this.repo = repo;
		this.imageRepo = imageRepo;
		System.out.println(repo);
	}

	@RabbitListener(bindings = {
			@QueueBinding(exchange = @Exchange(name = "amq.topic", type = "topic"), value = @Queue(value = "dw.product.2"), key = {
					"dw.product" }),

	})
	public void receiveProduct2(Product product) {
		System.out.println("-- product.2 --");
		System.out.println(product);

		Product storeProduct = Product.builder().id(product.getId()).code(product.getCode())
				.category(product.getCategory()).name(product.getName()).price(product.getPrice())
				.quantity(product.getQuantity()).shortDescription(product.getShortDescription())
				.description(product.getDescription()).build();

		System.out.println(storeProduct);
		repo.save(storeProduct);
	}

	@RabbitListener(queues = "product.image")
	public void receiveProductImage(ProductImage productImage) {
		System.out.println("-- product.3 --");
		System.out.println(productImage);

		ProductImage storeProductImage = ProductImage.builder().id(productImage.getId())
				.imageName(productImage.getImageName()).productId(productImage.getProductId())
				.contentType(productImage.getContentType()).dataUrl(productImage.getDataUrl()).build();

		System.out.println(storeProductImage);
		imageRepo.save(storeProductImage);

	}

}
