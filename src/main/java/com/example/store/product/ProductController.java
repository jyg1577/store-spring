package com.example.store.product;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
	private ProductRepository productRepo;
	private ProductImageRepository productImageRepo;
	private final Path FILE_PATH = Paths.get("product_image");

	@Autowired
	public ProductController(ProductRepository productRepo, ProductImageRepository productImageRepo) {
		this.productRepo = productRepo;
		this.productImageRepo = productImageRepo;
	}

	// id로 조회
	@GetMapping(value = "/products/{id}")
	public Product getproductsId(@PathVariable("id") long id, HttpServletResponse res) {
		if (productRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		Product reviewText = productRepo.findById(id).orElse(null);

		return reviewText;
	}

	// product 목록 조회
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProducts(HttpServletRequest req) {
		return productRepo.findAll(Sort.by("id").descending());
	}

	// category 목록조회 //
	// products/search/category?keyword=육류
	@RequestMapping(value = "/products/search/category", method = RequestMethod.GET)
	public List<Product> getProductsByCategory(@RequestParam("keyword") String keyword, HttpServletRequest req) {
		return productRepo.findByCategory(keyword);
	}

	// {id}인 product에 product-image 목록 조회
	@RequestMapping(value = "/products/{id}/product-images", method = RequestMethod.GET)
	public List<ProductImage> getProductImages(@PathVariable("id") long id, HttpServletResponse res) {

		if (productRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<ProductImage> productImages = productImageRepo.findByProductId(id);

		return productImages;
	}

}