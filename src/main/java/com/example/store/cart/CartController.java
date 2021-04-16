package com.example.store.cart;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.store.configuration.ApiConfiguration;

@RestController
public class CartController {
	private CartRepository CartRepo;
	private CartDetailRepository CartdetailRepo;
	private final Path FILE_PATH = Paths.get("Purchase_image");

	private CartService service;

	@Autowired
	private ApiConfiguration apiConfig;

	@Autowired
	public CartController(CartRepository CartRepo, CartDetailRepository detailRepo, CartService service) {

		this.CartRepo = CartRepo;
		this.CartdetailRepo = detailRepo;
		this.service = service;
	}

	// 장바구니 전체조회
	@RequestMapping(value = "/Cart", method = RequestMethod.GET)
	public List<Cart> getProducts(HttpServletRequest req) {
//		CartRepo.findAll();
		List<Cart> list = CartRepo.findAll();
//		for (Cart cart : list) {
//			for (CartDetail file : cart.getDetails()) {
//				file.setDataUrl(apiConfig.getBasePath() + "/cart-picture/" + file.getId());
//			}
//		}
		return list;
	}

	// 이름으로 조회
	@GetMapping(value = "/Cart/keyword")
	public List<Cart> cart(@RequestParam("name") String name) {
		List<Cart> lists = CartRepo.findByProductName(name);
		return lists;
	}

	// 정보 저장
	@RequestMapping(value = "/Cart", method = RequestMethod.POST)
	public Cart addCart(@RequestBody Cart order) {

		order.setPurchaseState("결제대기");
		CartRepo.save(order);
		return order;
	}

	// 사진 저장
	@RequestMapping(value = "/Cart/{id}/Cart-images", method = RequestMethod.POST)

	public CartDetail addCartImage(@PathVariable("id") long id, @RequestPart("data") MultipartFile file,
			HttpServletResponse res) throws IOException { // 가격

		if (CartRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		// 디렉토리가 없으면 생성
		if (!Files.exists(FILE_PATH)) {
			Files.createDirectories(FILE_PATH);
		}

		// 파일 저장
		FileCopyUtils.copy(file.getBytes(), new File(FILE_PATH.resolve(file.getOriginalFilename()).toString()));
		// 파일 메타데이터 저장
		CartDetail detail = CartDetail.builder().cartId(id).imageName(file.getOriginalFilename())
				.contentType(file.getContentType()).build();

		CartdetailRepo.save(detail);

		return detail;
	}

	// id로 삭제
	@RequestMapping(value = "/Cart/{id}", method = RequestMethod.DELETE)
	public boolean deleteById(@PathVariable("id") long id, HttpServletResponse res) {

		Cart cart = CartRepo.findById(id).orElse(null);

		if (cart == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		List<CartDetail> details = CartdetailRepo.findBycartId(id);
		for (CartDetail detail : details) {
			CartdetailRepo.delete(detail);
		}
		CartRepo.deleteById(id);

		return true;
	}

	// 결제 상태 수정
	@RequestMapping(value = "/Cart/{id}/order", method = RequestMethod.PATCH)

	public Cart modifyCart(@PathVariable("id") long id, HttpServletResponse res) {

		Cart cart = CartRepo.findById(id).orElse(null);

		if (cart == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		cart.setUserName("김예은");
		cart.setUserAddress("서울시 중랑구 봉화산로 56, 301호");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd-");

		Calendar c1 = Calendar.getInstance();
		String Today = sdf.format(c1.getTime());
		String Today2 = sdf2.format(c1.getTime());

		cart.setOrderDate(Today);

		Random rand = new Random();
		int rand_int1 = rand.nextInt(1000000);
		cart.setOrderNumber(Today2 + rand_int1);

		cart.setPurchaseState("결제완료");
		CartRepo.save(cart);

		PurchaseOrderList();

		deleteById(id, res);

		return null;

	}

	public List<Cart> PurchaseOrderList() {
		List<Cart> orderLists = CartRepo.findByPurchaseState("결제완료");
		for (Cart a : orderLists) {
			service.sendOrder(a);
		}

		return orderLists;
	}

	// 수량 갯수
	@RequestMapping(value = "/Cart/{id}/quantity", method = RequestMethod.PATCH)

	public Cart increaseCart(@PathVariable("id") long id, HttpServletResponse res, @RequestParam("num") long num,
			@RequestParam("price") long price) {

		Cart cart = CartRepo.findById(id).orElse(null);

		if (cart == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		cart.setQuantity(cart.getQuantity() + num);
		cart.setPrice(cart.getPrice() + price);
		CartRepo.save(cart);

		return null;
	}

	// 백엔드와 프론트 연동할때
	@RequestMapping(value = "/Cart-image/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getCart(@PathVariable("id") long id, HttpServletResponse res) throws IOException {
		CartDetail cartDetail = CartdetailRepo.findById(id).orElse(null);

		if (cartDetail == null) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", cartDetail.getContentType() + ";charset=UTF-8");

		responseHeaders.set("Content-Disposition",
				"inline; filename=" + URLEncoder.encode(cartDetail.getImageName(), "UTF-8"));

		return ResponseEntity.ok().headers(responseHeaders)
				.body(Files.readAllBytes(FILE_PATH.resolve(cartDetail.getImageName())));
	}
}
