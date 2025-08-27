package com.man.CartService.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.man.CartService.entity.Cart;
import com.man.CartService.entity.CartItem;
import com.man.CartService.repository.CartRepository;
import com.man.CartService.repository.ItemCartRepository;
import com.man.CartService.request.AddProductToCartRequest;
import com.man.CartService.request.CartCreateRequest;
import com.man.CartService.request.DeleteProductToCartRequest;
import com.man.CartService.response.AddProductToCartResponse;
import com.man.CartService.response.CartCreateResponse;
import com.man.CartService.response.CartItemResponse;
import com.man.CartService.response.DeleteProductToCartResponse;
import com.man.CartService.service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository repository;
	@Autowired
	private ItemCartRepository itemCartRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CartCreateResponse createCart(CartCreateRequest cartReq) {

		Cart cart = mapper.map(cartReq, Cart.class);

		Long total = 0L;
		if (cart.getItems() != null) {
			for (CartItem item : cartReq.getItems()) {
				item.setCart(cart);
				total += item.getSubTotal();
			}
		}

		cart.setTotal(total);
		cart = repository.save(cart);

		List<CartItemResponse> cartCreateResponses = cart.getItems().stream()
				.map(item -> mapper.map(item, CartItemResponse.class)).toList();

		CartCreateResponse cartCreateResponse = mapper.map(cart, CartCreateResponse.class);
		cartCreateResponse.setItems(cartCreateResponses);

		return cartCreateResponse;
	}

	@Override
	public AddProductToCartResponse addProductToCart(AddProductToCartRequest itemReqs) {
		Cart cart = repository.findById(itemReqs.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));

		Long total = cart.getTotal();
		List<CartItem> cartItemResponses = itemReqs.getItems().stream().map(item -> mapper.map(item, CartItem.class))
				.toList();

		for (CartItem item : cartItemResponses) {
			total += item.getSubTotal();
			item.setCart(cart);
			cart.getItems().add(item);
			itemCartRepository.save(item);
		}
		cart.setTotal(total);
		cart = repository.save(cart);

		return mapper.map(cart, AddProductToCartResponse.class);
	}

	@Override
	@Transactional
	public DeleteProductToCartResponse removeProductFromCart(DeleteProductToCartRequest cartReq) {
		// Lấy cart từ DB
		Cart cart = repository.findById(cartReq.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));

		// Lấy ra danh sách productId có trong cartReq
		List<Long> removeProuctId = cartReq.getItems().stream().map(CartItem::getProductId)
				.collect(Collectors.toList());

		// tính tổng total product trong cartReq
		Long removeTotal = cart.getItems().stream().filter(item -> removeProuctId.contains(item.getProductId()))
				.mapToLong(CartItem::getSubTotal).sum();

		// xóa productId trong cart
		cart.getItems().removeIf(item -> removeProuctId.contains(item.getProductId()));

		// cập nhật lại total
		cart.setTotal(cart.getTotal() - removeTotal);

		// lưu db
		Cart saveCart = repository.save(cart);
		
		//Trả về response
		DeleteProductToCartResponse response = mapper.map(saveCart, DeleteProductToCartResponse.class);
		response.setItems(saveCart.getItems().stream().map(item -> mapper.map(item, CartItemResponse.class))
				.collect(Collectors.toList()));

		return response;
	}

	@Override
	public void clearCart(Long cartId) {
		// TODO Auto-generated method stub
		Cart cart = repository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));

		repository.delete(cart);

	}

	@Override
	public List<Cart> getCartByUserId(Long userId) {
		// TODO Auto-generated method stub
		return repository.findByUserId(userId);
	}

}
