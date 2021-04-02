package com.example.store.cart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CartController {
	private CartRepository CartRepo;
	private CartDetailRepository CartdetailRepo;
	private final Path FILE_PATH = Paths.get("Purchase_image");

	@Autowired
	public CartController(CartRepository CartRepo, CartDetailRepository detailRepo) {

		this.CartRepo = CartRepo;
		this.CartdetailRepo = detailRepo;
	}

	@RequestMapping(value = "/Cart", method = RequestMethod.POST)
	public Cart addCart(@RequestBody Cart order) {
		order.setUserId("2021");
		order.setUserName("yeeun");

		order.setCreatedTime(new Date().getTime());
		CartRepo.save(order);
		return order;
	}

	@RequestMapping(value = "/Cart", method = RequestMethod.GET)
	public List<Cart> getProducts(HttpServletRequest req) {
		return CartRepo.findAll(Sort.by("id").descending());
	}

	@RequestMapping(value = "/Cart/{id}/Cart-Details", method = RequestMethod.GET)
	public List<CartDetail> getFeedFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (CartRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<CartDetail> CartDetails = CartdetailRepo.findBypurchaseOrderId(id);
		System.out.println(CartDetails);

		return CartDetails;
	}

	@RequestMapping(value = "/Cart/{id}/Cart-images", method = RequestMethod.POST)
	public CartDetail addPurchaseImage(@PathVariable("id") long id, @RequestPart("data") MultipartFile image,
			HttpServletResponse res, @RequestParam("name") String name, @RequestParam("quantity") long quantity,
			@RequestParam("price") long price) throws IOException {
		if (CartRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		// 디렉토리가 없으면 생성
		if (!Files.exists(FILE_PATH)) {
			Files.createDirectories(FILE_PATH);
		}

		// 파일 저장
		FileCopyUtils.copy(image.getBytes(), new File(FILE_PATH.resolve(image.getOriginalFilename()).toString()));
		// 파일 메타데이터 저장
		CartDetail purchaseOrderDetail = CartDetail.builder().productname(name).quantity(quantity).price(price)
				.purchaseOrderId(id).imageName(image.getOriginalFilename()).contentType(image.getContentType()).build();

		CartdetailRepo.save(purchaseOrderDetail);

		return purchaseOrderDetail;

	}

}
