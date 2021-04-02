package com.example.store.order;

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
public class PurchaseOrderController {
	private PurchaseOrderRepository PurchaseOrderRepo;
	private PurchaseOrderDetailRepository detailRepo;
	private final Path FILE_PATH = Paths.get("Purchase_image");

	@Autowired
	public PurchaseOrderController(PurchaseOrderRepository PurchaseOrderRepo,
			PurchaseOrderDetailRepository detailRepo) {

		this.PurchaseOrderRepo = PurchaseOrderRepo;
		this.detailRepo = detailRepo;
	}

	@RequestMapping(value = "/PurchaseOrder", method = RequestMethod.POST)
	public PurchaseOrder addPurchaseOrder(@RequestBody PurchaseOrder order) {
		order.setUserId("2021");
		order.setUserName("yeeun");
		order.setUserAddress("jayaung");
		order.setCreatedTime(new Date().getTime());
		PurchaseOrderRepo.save(order);
		return order;
	}

	@RequestMapping(value = "/PurchaseOrder", method = RequestMethod.GET)
	public List<PurchaseOrder> getProducts(HttpServletRequest req) {
		return PurchaseOrderRepo.findAll(Sort.by("id").descending());
	}

	@RequestMapping(value = "/PurchaseOrder/{id}/PurchaseOrder-Details", method = RequestMethod.GET)
	public List<PurchaseOrderDetail> getFeedFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (PurchaseOrderRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<PurchaseOrderDetail> PurchaseOrderDetails = detailRepo.findBypurchaseOrderId(id);
		System.out.println(PurchaseOrderDetails);

		return PurchaseOrderDetails;
	}

	@RequestMapping(value = "/PurchaseOrder/{id}/purchase-images", method = RequestMethod.POST)
	public PurchaseOrderDetail addPurchaseImage(@PathVariable("id") long id, @RequestPart("data") MultipartFile image,
			HttpServletResponse res, @RequestParam("name") String name, @RequestParam("quantity") long quantity,
			@RequestParam("price") long price) throws IOException {
		if (PurchaseOrderRepo.findById(id).orElse(null) == null) {
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
		PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder().productname(name).quantity(quantity)
				.price(price).purchaseOrderId(id).imageName(image.getOriginalFilename())
				.contentType(image.getContentType()).build();

		detailRepo.save(purchaseOrderDetail);

		return purchaseOrderDetail;

	}

}
